package client_side.view;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import client_side.controller.AppController;
import database.Database;
import model.user.*;

import javax.xml.crypto.Data;

import static client_side.controller.Authentication.getInfo;
import static server_side.database.Database.retrieveFromDB;


/**
 * The type Menu handler.
 */
public class MenuHandler {

    static Scanner sc = new Scanner(System.in);

    /**
     * Show menu int.
     *
     * @return the int
     */

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


    /**
     * On sign up button.
     */
    public static void onSignUpButton() {
        getInfo();
    }


    /**
     * On login button user.
     *
     * @return the user
     */
    public static User onLoginButton() {
        return Database.retrieveFromDB();
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

    public static void addFriend(User user) {
        ArrayList<User> parsedUsers = Database.userList();
        int i = 1;
        System.out.println("enter the username of the user you want to add as your friend : ");
        for (User member : parsedUsers) {
            if (!member.getUsername().equals(user.getUsername())) {
                System.out.println(i + ". " + member.getUsername());
                i++;
            }
        }
        String username = sc.nextLine();
        User chosenUser = null;
        for (User member : parsedUsers) {
            if (member.getUsername().equals(username)) {
                chosenUser = member;
            }
        }
        if (chosenUser == null) {
            System.out.println("Wrong username.");
            return;
        }
        user.addFriend(chosenUser);
        System.out.println("Successfully added " + chosenUser.getUsername() + " to your friends list.");

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
