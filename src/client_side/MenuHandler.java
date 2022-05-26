package client_side;

import java.util.Scanner;

import static client_side.Authentication.*;
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
    public static int showStartMenu() {
        System.out.println("""
                1)Signup
                2)Login
                3)Exit""");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entered Input wasn't valid.");
            return showStartMenu();
        }
        return choice;
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

    
    public static int loginMenu(){
        System.out.println("""
                1)Sever
                2)Friend
                3)Exit""");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entered Input wasn't valid.");
            return loginMenu();
        }
        return choice;
    }


    public static int friendMenu(){
        System.out.println("""
                1)Add new friend
                2)List of all friends
                3)Exit""");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entered Input wasn't valid.");
            return loginMenu();
        }
        return choice;
    }


    public static int serverMenu(){
        System.out.println("""
                1)Add new server
                2)List of all servers
                3)Exit""");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entered Input wasn't valid.");
            return loginMenu();
        }
        return choice;
    }

}
