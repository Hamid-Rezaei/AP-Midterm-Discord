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
    ArrayList<Message> pinnedMessages;

    public TextChannel(String name, GroupChat groupChat, String guildName) {
        super(name, groupChat);
        messages = new ArrayList<>();
        this.guildName = guildName;
        usersInChat = new HashSet<>();
        pinnedMessages = new ArrayList<>();
    }
    public TextChannel(String name, String guildName) {
        super(name);
        messages = new ArrayList<>();
        this.guildName = guildName;
        usersInChat = new HashSet<>();
        pinnedMessages = new ArrayList<>();
    }


    public synchronized void addMessage(Message message) {
        this.messages.add(message);
        saveMessages();
        broadcastMessage(message);
    }

    public int getMessageIndex(Message message) {
        return messages.indexOf(message);
    }

    public void broadcastMessage(Message message) {
        int index = getMessageIndex(message);
        if(message.isFile())
            index = -2;
        for (Connection connection : usersInChat) {
            connection.sendMessage(message, index + 1);
        }
    }

    public synchronized void broadcastMessages(Connection connection) {
        if (this.messages.size() > 15) {
            for (int i = messages.size() - 15; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i), i + 1);
            }
        } else {
            for (int i = 0; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i), i + 1);

            }
        }

    }

    public void broadcastExitMessage(String message, Connection userConnection) {
        for (Connection connection : usersInChat) {
            if (connection.getUsername().equals(userConnection.getUsername())) {
                connection.sendMessage(message);
            }
        }
    }

    public synchronized void pinMessage(int index) {
        pinnedMessages.add(messages.get(index - 1));
        saveMessages();
    }

    public synchronized void showPinnedMessages(Connection connection) {
        for (int i = 0; i < pinnedMessages.size(); i++) {
            connection.sendMessage(pinnedMessages.get(i), i + 1);
        }
    }

    public void addUser(Connection user) {
        if (usersInChat == null) {
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
            writeStream.close();
            writeData.close();
            FileOutputStream pinnedData = new FileOutputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + "-" + "pin.bin");
            ObjectOutputStream write = new ObjectOutputStream(pinnedData);
            write.writeObject(pinnedMessages);
            write.flush();
            write.close();
            pinnedData.close();
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
            FileInputStream pinData = new FileInputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + "-" + "pin.bin");
            ObjectInputStream readPinned = new ObjectInputStream(pinData);
            pinnedMessages = (ArrayList<Message>) readPinned.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Here we have some bugs");
            saveMessages();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reactToMessage(int index, String reactionType, String reactor) {
        messages.get(index).setReaction(reactionType,reactor);
        saveMessages();
    }
}
