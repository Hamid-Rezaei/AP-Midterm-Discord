package client_side.view;


import client_side.model.*;
import client_side.controller.*;


public class Application {

    private static User user;

    public static void setUser(User user) {
        Application.user = user;
    }

    private static void sceneHandler() {

        int choice = MenuHandler.showStartMenu();
        while (choice != 3) {
            switch (choice) {
                case 1 -> MenuHandler.onSignUpButton();
                case 2 -> {
                    MenuHandler.onLoginButton();
                    AppController.initialUser();
                    if (user != null) {
                        loginScene();
                    }
                }
                default -> System.out.println("Entered Input wasn't valid.");
            }
            choice = MenuHandler.showStartMenu();
        }

    }

    public static void loginScene() {

        int menuChoice;
        menuChoice = MenuHandler.loginMenu();
        while (menuChoice != 4) {
            switch (menuChoice) {
                case 1 -> MenuHandler.serverMenu();
                case 2 -> friendScene();
                case 3 -> settingScene();
                default -> System.out.println("Entered Input wasn't valid.");
            }
            menuChoice = MenuHandler.loginMenu();
        }
    }


    public static void settingScene() {
        int menuChoice;
        menuChoice = MenuHandler.settingMenu();
        while (menuChoice != 3) {
            switch (menuChoice) {
                case 1 -> Authentication.changePassword(user);
                case 2 -> Authentication.changeAvatar();
                default -> System.out.println("Entered Input wasn't valid.");
            }
            menuChoice = MenuHandler.settingMenu();
        }

    }


    public static void friendScene() {

        int menuChoice;
        menuChoice = MenuHandler.friendMenu();
        while (menuChoice != 3) {
            switch (menuChoice) {
                //case 1 -> search for user;
                //case 2 -> print friend list to enter direct chat;
                default -> System.out.println("Entered Input wasn't valid.");
            }
            menuChoice = MenuHandler.friendMenu();
        }

    }


    public static void main(String[] args) {
        AppController.initialNetwork();
        sceneHandler();

    }


}
