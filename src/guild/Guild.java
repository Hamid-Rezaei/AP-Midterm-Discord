package guild;

<<<<<<< HEAD
=======
import java.util.*;

>>>>>>> af86eab3cd9f7e4b03048a928bea40cfefee744d
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
