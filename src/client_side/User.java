package client_side;
import guild.*;
import java.util.ArrayList;

import guild.Guild;

import java.util.*;

public class User {
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
<<<<<<< HEAD

=======
>>>>>>> af86eab3cd9f7e4b03048a928bea40cfefee744d
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
