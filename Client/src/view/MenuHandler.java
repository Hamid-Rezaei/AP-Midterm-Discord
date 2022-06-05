package view;


import controller.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The type Menu handler.
 */
public class MenuHandler {

    static Scanner sc = new Scanner(System.in);

    public static boolean matchedInput(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


    public static int returnChoice() {
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
        return choice;
    }

    public static int showStartMenu() {
        System.out.println("""
                1) Signup
                2) Login
                3) Exit""");
        return returnChoice();
    }


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
    public static InputStream getAvatar()   {
        System.out.print("Enter image address:");
        String path = sc.nextLine();

        try {
            InputStream img = new FileInputStream(path);
            return img;
        } catch (IOException e) {
            try {
                return new FileInputStream("./src/defaultAvatar.png");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return null;

            }
        }
    }


    public static int loginMenu() {
        System.out.println("""
                1) Severs
                2) Friends
                3) Settings
                4) Exit""");

        return returnChoice();
    }


    public static int friendMenu() {
        System.out.println("""
                1) Add new friend
                2) Chat with a friend
                3) Exit""");

        return returnChoice();
    }


    public static int serverMenu() {
        System.out.println("""
                1) Add new server
                2) List of all servers
                3) Exit""");

        return returnChoice();
    }

    public static int settingMenu() {
        System.out.println("""
                1) Change password
                2) Change avatar
                3) Exit""");
        return returnChoice();
    }

}
