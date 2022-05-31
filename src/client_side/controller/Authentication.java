package client_side.controller;

import database.Database;
import model.user.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static client_side.view.MenuHandler.*;
import static database.Database.insertToDB;

public class Authentication {

    /**
     * The Sc.
     */
    static Scanner sc = new Scanner(System.in);

    /**
     * Matched input boolean.
     *
     * @param regex the regex
     * @param input the input
     * @return the boolean
     */
    public static boolean matchedInput(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


    public static void getInfo() {
        String username = getUsername();
        checkUniqueUsername(username);
        String password = getPassword();
        String email = getEmail();
        System.out.print("Enter Phone number(optional): ");
        String phoneNumber = sc.nextLine();
        InputStream img = getAvatar();
        insertToDB(username, password, email, phoneNumber, img);
    }


    /**
     * Check unique username.
     *
     * @param username the username
     */
    public static void checkUniqueUsername(String username) {

        try {
            Connection connection = Database.connectToDB();
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (resultSet.next()) {
                System.out.println("Username Already Exists.");
                onSignUpButton();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Generate unique random id int.
     *
     * @param connection the connection
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int generateUniqueRandomId(Connection connection) throws SQLException {
        Random random = new Random();
        int id = random.nextInt(1000);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from users where userID= " + id);

        if (resultSet.next()) {
            return generateUniqueRandomId(connection);
        } else {
            return id;
        }

    }


    //public static boolean


    /**
     * Gets username.
     *
     * @return the username
     */
    public static String getUsername() {
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        if (matchedInput(usernameRegex, username)) {
            return username;
        } else {
            System.out.println("Username length must be at least 6 and contains numbers and characters.");
            return getUsername();
        }
    }


    public static boolean checkValidPass(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        if (matchedInput(passwordRegex, password)) {
            return true;
        } else {
            System.out.println("password length must be at least 8 and contains numbers and characters.");
            return false;
        }
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public static String getPassword() {

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (checkValidPass(password)) {
            return password;
        } else {
            return getPassword();
        }
    }

    public static void changePassword(User user) {
        System.out.print("Enter your current password: ");
        String currentPass = sc.nextLine();
        if (currentPass.equals(user.getPassword())) {
            System.out.print("(New password) ");
            String newPass = getPassword();
            user.changePassword(newPass);
            if (Database.updateUser(user)) {
                System.out.println("password changed successfully!");
            } else {
                System.out.println("something went wrong.");
            }
        } else{
            System.out.println("Wrong password.");
        }
    }


    /**
     * Gets email.
     *
     * @return the email
     */
    public static String getEmail() {
        String emailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z]+(?:\\.[a-zA-Z]+)*$";
        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        if (matchedInput(emailRegex, email)) {
            return email;
        } else {
            System.out.println("Entered email wasn't valid.");
            return getEmail();
        }

    }

    /**
     * Gets avatar.
     */
    public static InputStream getAvatar() {
        System.out.print("Enter image address:");
        String path = sc.nextLine();

        try {
            InputStream img = new FileInputStream(path);
            return img;
        } catch (IOException e) {
            return null;
        }
    }


}
