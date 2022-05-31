package model.guild;


import model.user.User;

import java.awt.image.BufferedImage;

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
    public GuildUser(String username, String password, String email, String phoneNumber, BufferedImage avatar) {
        super(username, password, email, phoneNumber,avatar);
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
