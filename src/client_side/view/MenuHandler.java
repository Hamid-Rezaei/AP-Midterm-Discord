package client_side.view;

import java.util.Scanner;

import model.user.*;

import static client_side.controller.Authentication.getInfo;
import static database.Database.retrieveFromDB;


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
        int choice = 0;
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
        return retrieveFromDB();
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
                2) List of all friends
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
