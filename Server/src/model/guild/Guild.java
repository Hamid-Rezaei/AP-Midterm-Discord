package model.guild;

import model.User;

import java.io.Serializable;
import java.util.*;


public class Guild implements Serializable {
    private String name;
    private GuildUser owner;
    private HashSet<GuildUser> guildUsers;
    private ArrayList<TextChannel> textChannels;
    private ArrayList<VoiceChannel> voiceChannels;

    public Guild(String name, GuildUser owner) {
        this.name = name;
        this.owner = owner;
        guildUsers = new HashSet<>();
        textChannels = new ArrayList<>();
        voiceChannels = new ArrayList<>();
        addUser(owner);
    }

    public boolean hasUser(String username) {
        for (GuildUser guildUser : guildUsers) {
            if (guildUser.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void removeMember(GuildUser guildUser){
        guildUsers.remove(guildUser);
    }

    public ArrayList<TextChannel> getTextChannels() {
        return textChannels;
    }

    public ArrayList<VoiceChannel> getVoiceChannels() {
        return voiceChannels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return owner.getUsername();
    }

    public HashSet<GuildUser> getGuildUsers() {
        return guildUsers;
    }

    public void addUser(GuildUser user) {
        guildUsers.add(user);
    }

    public void addTextChanel(TextChannel textChannel) {
        textChannels.add(textChannel);
    }

    public void addVoiceChannel(VoiceChannel voiceChannel) {
        voiceChannels.add(voiceChannel);
    }

    public void removeTextChannel(TextChannel textChannel){
        textChannels.remove(textChannel);
    }

    public void removeVoiceChannel(VoiceChannel voiceChannel) {
        voiceChannels.remove(voiceChannel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guild)) return false;
        Guild guild = (Guild) o;
        return Objects.equals(getName(), guild.getName()) && Objects.equals(owner, guild.owner) && Objects.equals(getGuildUsers(), guild.getGuildUsers()) && Objects.equals(getTextChannels(), guild.getTextChannels()) && Objects.equals(getVoiceChannels(), guild.getVoiceChannels());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), owner, getGuildUsers(), getTextChannels(), getVoiceChannels());
    }
}
