//package model.guild;
//
//import model.Message;
//
package model.guild;

import controller.Connection;
import model.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GroupChat implements Serializable {
    transient HashSet<Connection> usersInChat;
    public GroupChat() {
        usersInChat = new HashSet<>();
    }

    public GroupChat(HashSet<Connection> usersInChat) {
        this.usersInChat = usersInChat;
    }

    public void addMessage(Message message){
        sendMessage(message);
    }
    public void sendMessage(Message message){
        for(Connection user: usersInChat){
            user.sendMessage(message);
        }
    }
    public void broadcastMessage(Message message) {
        for (Connection connection : usersInChat) {
            connection.sendMessage(message);
        }
    }


    public void addUser(Connection user) {
        usersInChat.add(user);

    }

    public void removeUser(Connection user) {
        usersInChat.remove(user);
    }


}

//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashSet;
//
//public class GroupChat implements Serializable {
//    ArrayList<Message> messages;
//    HashSet<GuildUser> usersInChat;
//
//    public GroupChat() {
//        messages = new ArrayList<>();
//        usersInChat = new HashSet<>();
//    }
//
//    public GroupChat(HashSet<GuildUser> usersInChat) {
//        this.messages = new ArrayList<>();
//        this.usersInChat = usersInChat;
//    }
//
//    public void addUser(GuildUser user) {
//        usersInChat.add(user);
//    }
//
//    public void removeUser(GuildUser user) {
//        usersInChat.add(user);
//    }
//
//
//}
