package model.user;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.*;


import model.guild.Guild;


public class User {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Status status;
    private ArrayList<User> friends;
    private ArrayList<Guild> guilds;
    private BufferedImage avatar;


    /**
     * Instantiates a new User.;
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     */
    public User(String username, String password, String email, String phoneNumber, BufferedImage avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = Status.ONLINE;
        friends = new ArrayList<>();
        guilds = new ArrayList<>();
        this.avatar = avatar;
    }


    public void addFriend(User user){
        friends.add(user);
    }

    public void printFriends(){
        int i = 1;
        for (User friend : friends){
            System.out.println(i++ + ". " + friend.toString());
        }
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
        return   username;
    }
}
