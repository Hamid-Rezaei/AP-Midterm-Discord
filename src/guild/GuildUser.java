package guild;

import client_side.User;

public class GuildUser extends User {


    Role role;
    /**
     * Instantiates a new User.
     *
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     */
    public GuildUser(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber);
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
