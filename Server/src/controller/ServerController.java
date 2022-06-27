package controller;


import Database.Database;
import model.Chat;
import model.Message;
import model.User;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerController implements Runnable {

    public static ArrayList<ServerController> serverControllers = new ArrayList<>();
    public static HashMap<String, DirectChatController> directChats = new HashMap<>();
    public static HashMap<String, Connection> connections = new HashMap<>();

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String appUsername;

    private String userToken;

    public ServerController(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        serverControllers.add(this);
    }


    public void getService() {
        try {
            String task = inputStream.readUTF();
            switch (task) {
                case "#signUp" -> signUp();
                case "#login" -> login();
                case "#friendRequest" -> friendRequest();
                case "#RequestList" -> friendRequestList();
                case "#FriendList" -> friendList();
                case "#getUser" -> getUserWithUsername();
                case "#requestForDirectChat" -> chatWithFriend();
                case "#revisedFriendRequests" -> revisedFriendRequests();
            }

        } catch (IOException e) {
            System.out.println("A user disconnected.");
            try {
                inputStream.close();
                outputStream.close();
                this.socket.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }


    public void signUp() {
        try {
            String[] parts = inputStream.readUTF().split(" ");
            String username = parts[0];
            String pass = parts[1];
            String email = parts[2];
            String phoneNum = null;
            if (parts.length == 4)
                phoneNum = parts[3];
            String token = UUID.randomUUID().toString();
            int avatarSize = inputStream.readInt();
            byte[] avatar = new byte[avatarSize];
            inputStream.readFully(avatar, 0, avatarSize);
            int answer = Database.insertToDB(username, pass, email, phoneNum, token, avatar).getCode();
            outputStream.writeInt(answer);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        try {
            String[] parts = inputStream.readUTF().split(" ");
            String username = parts[0];
            String pass = parts[1];
            User answer = Database.retrieveFromDB(username, pass);
            if (answer == null) {
                outputStream.writeObject(answer);
                return;
            }
            BufferedImage userAvatar = answer.getAvatar();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(userAvatar, "png", baos);
            byte[] byteAvatar = baos.toByteArray();

            outputStream.writeObject(answer);
            outputStream.flush();

            outputStream.writeInt(byteAvatar.length);

            outputStream.flush();
            outputStream.write(byteAvatar, 0, byteAvatar.length);
            outputStream.reset();
            appUsername = answer.getUsername();
            Connection connection = new Connection(this.socket, outputStream, inputStream, this.appUsername);
            connections.put(appUsername, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void friendRequest() {
        try {
            String fromUser = inputStream.readUTF();
            String targetUser = inputStream.readUTF();
            ServerErrorType answer = Database.sendFriendRequest(fromUser, targetUser);
            outputStream.writeInt(answer.getCode());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void friendRequestList() {
        try {
            String username = inputStream.readUTF();
            HashSet<String> reqList = Database.viewFriendRequestList(username);
            outputStream.writeObject(reqList);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void revisedFriendRequests() {
        try {
            HashSet<String> accepted = (HashSet<String>) inputStream.readObject();
            HashSet<String> rejected = (HashSet<String>) inputStream.readObject();
            String username = inputStream.readUTF();
            String answer = Database.reviseFriendRequests(username, accepted, rejected);
            outputStream.writeUTF(answer);
            outputStream.flush();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void friendList() {
        try {
            String username = inputStream.readUTF();
            HashSet<String> friendList = Database.viewFriendList(username);
            outputStream.writeObject(friendList);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserWithUsername() {
        try {
            String username = inputStream.readUTF();
            outputStream.writeObject(Database.retrieveFromDB(username));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String directChatHashCode(String user_1, String user_2) {
        String hash;
        if (user_1.length() < user_2.length()) {
            hash = user_1 + user_2;
        } else {
            hash = user_2 + user_1;
        }
        return hash;
    }

    private DirectChatController getDirChatController(User currentUser, User friend ){
        String chatHash = directChatHashCode(currentUser.getUsername(), friend.getUsername());
        DirectChatController directChatController = directChats.get(chatHash);
        if (directChatController == null) {
            Connection userConnection = connections.get(currentUser.getUsername());

            ArrayList<User> participants = new ArrayList<>();
            participants.add(currentUser);
            participants.add(friend);

            HashSet<Connection> directChatConnections = new HashSet<>();
            directChatConnections.add(userConnection);

            directChatController = new DirectChatController(directChatConnections, participants);
            directChatController.loadMessages();
            directChats.put(directChatController.getChatHashCode(), directChatController);
        }
        directChatController.addConnection(connections.get(currentUser.getUsername()));

        return directChatController;
    }

    private void chatWithFriend() {
        //TODO: check if we can load direct chat controller
        try {
            User friend = (User) inputStream.readObject();
            User currentUser = (User) inputStream.readObject();
            DirectChatController directChatController = getDirChatController(currentUser, friend);
            new Thread(directChatController).start();
            outputStream.writeUTF("Success");
            outputStream.flush();
            outputStream.writeObject(directChatController.getDirectChat());
            outputStream.flush();
            directChatController.broadcastMessages(connections.get(currentUser.getUsername()));

            Message message = (Message) inputStream.readObject();
            while (!message.getContent().equals("#exit")) {
                directChatController.addMessage(message);
                message = (Message) inputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        while (!socket.isClosed()) {
            getService();
        }
    }
}

