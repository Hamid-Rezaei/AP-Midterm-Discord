package guild;

import client_side.*;
import guild.*;

import java.util.*;

public class Guild {
    private String name;
    private GuildUser owner;
    private ArrayList<GuildUser> guildUsers;
    private ArrayList<TextChannel> textChannels;
    private ArrayList<VoiceChannel> voiceChannels;

    public Guild(String name, GuildUser owner) {
        this.name = name;
        this.owner = owner;
        guildUsers = new ArrayList<>();
        textChannels = new ArrayList<>();
        voiceChannels = new ArrayList<>();
    }
}
