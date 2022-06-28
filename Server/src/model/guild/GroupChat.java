package model.guild;

import model.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class GroupChat implements Serializable {
    ArrayList<Message> messages;
    HashSet<GuildUser> usersInChat;

    public GroupChat() {
        messages = new ArrayList<>();
        usersInChat = new HashSet<>();
    }

    public GroupChat(HashSet<GuildUser> usersInChat) {
        this.messages = new ArrayList<>();
        this.usersInChat = usersInChat;
    }

    public void addUser(GuildUser user) {
        usersInChat.add(user);
    }

    public void removeUser(GuildUser user) {
        usersInChat.add(user);
    }


}
