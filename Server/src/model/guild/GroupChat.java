package model.guild;

import model.Message;

import java.util.ArrayList;
import java.util.HashSet;

public class GroupChat {
    ArrayList<Message> messages;
    HashSet<GuildUser> guildUsers;

    public GroupChat(HashSet<GuildUser> guildUsers) {
        this.messages = new ArrayList<>();
        this.guildUsers = guildUsers;
    }


}
