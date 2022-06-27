package controller;

import model.Chat;
import model.Message;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DirectChatController implements Runnable {

    //    public static HashMap<String, DirectChatController> directChatControllers = new HashMap<>();
    private Chat directChat;
    private ArrayList<Message> messages = new ArrayList<>();
    private String chatHashCode;
    private ArrayList<User> participants;
    private HashSet<Connection> usersInChatConnection;


    public DirectChatController(HashSet<Connection> usersInChatConnection, ArrayList<User> participants) {
        directChat = new Chat();
        this.usersInChatConnection = usersInChatConnection;
        this.participants = participants;
        this.chatHashCode = generateHashCode();
        directChat = new Chat(messages);
//        directChatControllers.put(this.chatHashCode, this);
    }


    public String generateHashCode() {
        String hash;
        String user_1 = participants.get(0).getUsername();
        String user_2 = participants.get(1).getUsername();
        if (user_1.length() < user_2.length()) {
            hash = user_1 + user_2;
        } else {
            hash = user_2 + user_1;
        }
        return hash;
    }

    public synchronized void addMessage(Message message) {
        this.directChat.addMessage(message);
        broadcastMessage(message);
    }

    public void broadcastMessage(Message message) {
        for (Connection connection : usersInChatConnection) {
            connection.sendMessage(message);
        }
    }

    public void broadcastMessages(Connection connection) {
        if (directChat.getMessages().size() > 6) {
            for (int i = directChat.getMessages().size() - 6; i < directChat.getMessages().size(); i++) {
                connection.sendMessage(directChat.getMessages().get(i));
            }
        } else {
            for (int i = 0; i < directChat.getMessages().size(); i++) {
                connection.sendMessage(directChat.getMessages().get(i));

            }
        }

    }

    public void addConnection(Connection connection) {
        usersInChatConnection.add(connection);
    }

    public Chat getDirectChat() {
        return directChat;
    }

    public String getChatHashCode() {
        return chatHashCode;
    }

    @Override
    public void run() {
        int msg = 0;
        while (true) {
//            int trueSize = directChat.getMessages().size();
//            if (trueSize > msg) {
//                for (int i = msg; i < trueSize; i++) {
//                    System.out.println(directChat.getMessages().get(i).toString());
//                }
//                msg = trueSize;
//            }
//            if(directChat.getMessages().size() > 0){
//                System.out.println(directChat.getMessages().get(0).toString());
//
//            }

        }
    }
}
