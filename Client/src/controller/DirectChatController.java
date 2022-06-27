package controller;

import model.Message;
import model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DirectChatController implements Runnable {

    //    public static HashMap<String, DirectChatController> directChatControllers = new HashMap<>();
    private ArrayList<Message> messages;
    private int messagesSize;
    private String chatHashCode;
    private ArrayList<User> participants;
    private HashSet<Connection> usersInChatConnection;


    public DirectChatController(HashSet<Connection> usersInChatConnection, ArrayList<User> participants) {
        messages = new ArrayList<>();
        this.usersInChatConnection = usersInChatConnection;
        this.participants = participants;
        this.chatHashCode = generateHashCode();
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
        messages.add(message);
    }

    public void broadcastMessages() {
        for (Connection connection : usersInChatConnection) {
            for (int i = messages.size(); i > 0; i--) {
                //if(!message.getAuthorName().equals(connection.getUsername())){
                //  connection.sendMessage(message);
                // }
                connection.sendMessage(messages.get(i - 1));

            }
        }

    }

    public void addConnection(Connection connection) {
        usersInChatConnection.add(connection);
    }

    public String getChatHashCode() {
        return chatHashCode;
    }

    @Override
    public void run() {
        while (true) {
            for (Connection connection : usersInChatConnection) {
                addMessage(connection.receiveMessage());
                broadcastMessages();
            }
        }
    }
}
