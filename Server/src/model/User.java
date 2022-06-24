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


    public void addFriend(User user) {
        friends.add(user);
    }

    public void printFriends() {
        int i = 1;
        for (User friend : friends) {
            System.out.println(i++ + ". " + friend.toString());
        }
    }

    public void goToDirectChat(int friend){
    // TODO: create direct chat
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
        return username;
    }
}
