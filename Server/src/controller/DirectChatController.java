package controller;

import model.Chat;
import model.Message;
import model.User;

import java.io.*;
import java.util.*;

public class DirectChatController implements Runnable {

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
        saveMessages();
        broadcastMessage(message);
    }

    public void broadcastMessage(Message message) {
        for (Connection connection : usersInChatConnection) {
            connection.sendMessage(message);
        }
    }

    public synchronized void broadcastMessages(Connection connection) {
        if (this.messages.size() > 6) {
            for (int i = messages.size() - 6; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i));
            }
        } else {
            for (int i = 0; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i));

            }
        }

    }

    public synchronized void addConnection(Connection connection) {
        usersInChatConnection.add(connection);
    }

    public synchronized void removeConnection(String username){
        for(Connection connection : usersInChatConnection){
            if(connection.getUsername().equals(username)){
                usersInChatConnection.remove(connection);
            }
        }

    }
    public synchronized void saveMessages() {
        File theDir = new File("assets/direct_chat");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        try (FileOutputStream writeData = new FileOutputStream("assets/direct_chat/" + chatHashCode + ".bin");
             ObjectOutputStream writeStream = new ObjectOutputStream(writeData)) {
            writeStream.writeObject(messages);
            writeStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMessages() {
        try {
            FileInputStream readData = new FileInputStream("assets/direct_chat/" + chatHashCode + ".bin");
            ObjectInputStream readStream = new ObjectInputStream(readData);
            messages = (ArrayList<Message>) readStream.readObject();
        } catch (FileNotFoundException e) {
            //System.out.println("Here we have some bugs");
            saveMessages();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Chat getDirectChat() {
        return directChat;
    }

    public String getChatHashCode() {
        return chatHashCode;
    }

    @Override
    public void run() {
        while (true) {

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectChatController)) return false;
        DirectChatController that = (DirectChatController) o;
        return Objects.equals(getDirectChat(), that.getDirectChat()) && Objects.equals(messages, that.messages) && Objects.equals(getChatHashCode(), that.getChatHashCode()) && Objects.equals(participants, that.participants) && Objects.equals(usersInChatConnection, that.usersInChatConnection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDirectChat(), messages, getChatHashCode(), participants, usersInChatConnection);
    }
}
