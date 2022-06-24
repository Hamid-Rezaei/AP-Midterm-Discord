package model;

import model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;



public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Status status;
    private String token;
    private transient BufferedImage avatar;
    private ArrayList<User> friends;
    private ArrayList<String> friendRequests;
    // private ArrayList<Guild> guilds;
    // private ArrayList<DirectChat> directChats;


    /**
     * Instantiates a new User.;
     *
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     */
    public User(String username, String password, String email, String phoneNumber, String token,BufferedImage avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = Status.ONLINE;
        this.token = token;
        friends = new ArrayList<>();
        //   guilds = new ArrayList<>();
        //   directChats = new ArrayList<>();
        this.avatar = avatar;
    }


    public void addFriend(User friend) {
        friends.add(friend);

    }

    public void addFriendRequest(String request){
        friendRequests.add(request);
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void setFriendRequests(ArrayList<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public void goToDirectChat(int friend){
        // TODO: create direct chat
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }

    public void setAvatar(byte[] avatar) throws IOException {
        this.avatar = ImageIO.read(new ByteArrayInputStream(avatar));
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return status + username + Status.RESET;
    }
}
