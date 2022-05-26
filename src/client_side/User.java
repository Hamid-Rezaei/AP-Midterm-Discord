package client_side;
import guild.*;
import java.util.ArrayList;

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

    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
