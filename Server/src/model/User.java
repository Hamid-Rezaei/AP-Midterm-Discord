package model;


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


    /**
     * Instantiates a new User.;
     *
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     */
    public User(String username, String password, String email, String phoneNumber, String token, BufferedImage avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = Status.ONLINE;
        this.token = token;
        friends = new ArrayList<>();
        this.avatar = avatar;
    }

    public User(String username, String password, String email, String phoneNumber, String token, BufferedImage avatar,Status status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.token = token;
        friends = new ArrayList<>();
        this.avatar = avatar;
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

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPhoneNumber(), user.getPhoneNumber()) && getStatus() == user.getStatus() && Objects.equals(getToken(), user.getToken()) && Objects.equals(getAvatar(), user.getAvatar()) && Objects.equals(friends, user.friends) && Objects.equals(friendRequests, user.friendRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail(), getPhoneNumber(), getStatus(), getToken(), getAvatar(), friends, friendRequests);
    }

    @Override
    public String toString() {
        return status + username + Status.RESET;
    }
}
