package client_side;

import client_side.*;


public class Application {


    public static void main(String[] args) {
        int choice = MenuHandler.showMenu();
        User user;
        while (choice != 3) {
            switch (choice) {
                case 1 -> MenuHandler.onSignUpButton();
                case 2 -> user = MenuHandler.onLoginButton();
                default -> System.out.println("Entered Input wasn't valid.");
            }
            choice = MenuHandler.showMenu();
        }

    }
}
