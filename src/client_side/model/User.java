package client_side.model;
import guild.*;

import java.io.Serializable;
import java.util.ArrayList;

import guild.Guild;

import java.util.*;

public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Status status;
    private ArrayList<User> friends;
    private ArrayList<Guild> guilds;



    /**
     * Instantiates a new User.;
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     */
    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = Status.ONLINE;
        friends = new ArrayList<>();
        guilds = new ArrayList<>();
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
}
