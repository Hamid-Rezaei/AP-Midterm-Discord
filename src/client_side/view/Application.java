package client_side.view;

import client_side.controller.*;

import static client_side.controller.AppController.user;

public class Application {

    public static void sceneHandler() {

        int choice = MenuHandler.showStartMenu();
        while (choice != 3) {
            switch (choice) {
                case 1 -> MenuHandler.onSignUpButton();
                case 2 -> {
                    MenuHandler.onLoginButton();
                    if (user != null) {
                        loginScene();
                    } else {
                        System.out.println("User is null");
                    }
                }
                default -> System.out.println("Entered Input wasn't valid.");
            }
            choice = MenuHandler.showStartMenu();
        }

    }


    private static void loginScene() {

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


    private static void settingScene() {
        int menuChoice;
        menuChoice = MenuHandler.settingMenu();
        while (menuChoice != 3) {
            switch (menuChoice) {
                case 1 -> Authentication.changePassword(user);
                case 2 -> friendScene();
                default -> System.out.println("Entered Input wasn't valid.");
            }
            menuChoice = MenuHandler.settingMenu();
        }

    }


    private static void friendScene() {

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
        Application.sceneHandler();
    }
}