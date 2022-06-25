package controller;


import Database.Database;
import model.User;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerController implements Runnable {

    public static ArrayList<ServerController> serverControllers = new ArrayList<>();
    // public static HashMap<String, Socket> usersSockets;
    public static HashSet<Connection> connections;

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

            if (answer == 1) {
                appUsername = username;
            }

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
            connections.add(new Connection(this.socket, this.appUsername));
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
            HashSet<String> accepted =  (HashSet<String>) inputStream.readObject();
            HashSet<String> rejected =  (HashSet<String>) inputStream.readObject();
            String username = inputStream.readUTF();
            String answer = Database.reviseFriendRequests(username,accepted,rejected);
            outputStream.writeUTF(answer);
            outputStream.flush();
        } catch (ClassNotFoundException | IOException e){
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


    private void chatWithFriend() {
        //TODO: check if we can load direct chat controller
        
    }


    @Override
    public void run() {
        while (!socket.isClosed()) {
            getService();
        }
    }
}

