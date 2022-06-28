package controller;


import Database.Database;
import model.Chat;
import model.Message;
import model.User;
import model.guild.Guild;

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
    public static HashMap<String, ArrayList<Guild>> allGuilds = new HashMap<>();

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
                case "#addGuild" -> addGuild();
                case "#serverList" -> listOfUserServers();
                case "#blockUser" -> blockUser();
                case "#blockList" -> blockList();
                case "#unblockUser" -> unblockUser();
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

    private void blockUser() {
        try {
            String user = inputStream.readUTF();
            String blockTarget = inputStream.readUTF();
            String respone = Database.blockUser(user, blockTarget);
            outputStream.writeUTF(respone);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unblockUser() {
        try {
            String user = inputStream.readUTF();
            String unblockTarget = inputStream.readUTF();
            String respone = Database.unblockUser(user, unblockTarget);
            outputStream.writeUTF(respone);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void blockList() {
        try {
            String username = inputStream.readUTF();
           HashSet<String> blockedList =  Database.viewBlockedList(username);
           outputStream.writeObject(blockedList);
           outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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

    private DirectChatController getDirChatController(User currentUser, User friend) {
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


    public void addGuild() {
        //TODO:load
        loadGuilds();
        try {
            Guild guild = (Guild) inputStream.readObject();
            String name = guild.getOwnerName();
            ArrayList<Guild> guilds = allGuilds.get(name);
            if (guilds == null) {
                guilds = new ArrayList<>();
            }
            guilds.add(guild);
            allGuilds.put(name, guilds);
            //TODO:save
            saveGuilds();
            outputStream.writeUTF("Success");
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void listOfMyOwnServer() {
        ArrayList<Guild> guilds = allGuilds.get("HamidRezaei");
        for (Guild guild : guilds) {
            System.out.println(guild.getName());
        }
    }

    public void listOfUserServers() {
        ArrayList<Guild> userGuilds = new ArrayList<>();
        try {
            if (allGuilds.values().isEmpty()) {
                loadGuilds();
            }
            String username = inputStream.readUTF();
            for (ArrayList<Guild> guilds : allGuilds.values()) {
                for (Guild guild : guilds) {
                    if (guild.hasUser(username)) {
                        userGuilds.add(guild);
                    }
                }
            }
            outputStream.writeObject(userGuilds);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGuilds() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("guilds/all_guilds.bin"))) {
            try {
                os.writeObject(allGuilds);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadGuilds() {
        System.out.println("load:");
        try {
            File theFile = new File("guilds/all_guilds.bin");
            if (!theFile.exists()) {
                theFile.createNewFile();
            }
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("guilds/all_guilds.bin"));
            allGuilds = (HashMap<String, ArrayList<Guild>>) is.readObject();
            for (Map.Entry<String, ArrayList<Guild>> set :
                    allGuilds.entrySet()) {
                System.out.println(set.getKey() + " = "
                        + set.getValue().isEmpty());
            }
            //listOfMyOwnServer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

