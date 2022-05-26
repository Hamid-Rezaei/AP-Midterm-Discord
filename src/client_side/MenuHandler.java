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



}
