package model.guild;

import controller.Connection;
import model.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class TextChannel extends Channel {
    ArrayList<Message> messages;
    transient HashSet<Connection> usersInChat;
    String guildName;

    public TextChannel(String name, GroupChat groupChat, String guildName) {
        super(name, groupChat);
        messages = new ArrayList<>();
        this.guildName = guildName;
        usersInChat = new HashSet<>();
    }

    public synchronized void addMessage(Message message) {
        this.messages.add(message);
        saveMessages();
        broadcastMessage(message);
    }

    public void broadcastMessage(Message message) {
        for (Connection connection : usersInChat) {
            connection.sendMessage(message);
        }
    }

    public synchronized void broadcastMessages(Connection connection) {
        if (this.messages.size() > 15) {
            for (int i = messages.size() - 15; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i));
            }
        } else {
            for (int i = 0; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i));

            }
        }

    }

    public void addUser(Connection user) {
        if(usersInChat == null){
            usersInChat = new HashSet<>();
        }
        usersInChat.add(user);
        broadcastMessages(user);
    }

    public void removeUser(Connection user) {
        usersInChat.remove(user);
    }

    public synchronized void saveMessages() {
        File theDir = new File("assets/guilds/" + guildName + "/textchannels/");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        try (FileOutputStream writeData = new FileOutputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + ".bin");
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
            FileInputStream readData = new FileInputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + ".bin");
            ObjectInputStream readStream = new ObjectInputStream(readData);
            messages = (ArrayList<Message>) readStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Here we have some bugs");
            saveMessages();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
