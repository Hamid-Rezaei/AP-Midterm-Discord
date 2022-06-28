package model.guild;

import java.io.Serializable;

public abstract class Channel implements Serializable {
    String name;
    GroupChat groupChat;

    public Channel(String name, GroupChat groupChat) {
        this.name = name;
        this.groupChat = groupChat;
    }

    public String getName() {
        return name;
    }

    public GroupChat getGroupChat() {
        return groupChat;
    }
}
