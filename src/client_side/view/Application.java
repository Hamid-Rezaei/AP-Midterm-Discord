package client_side.view;

import client_side.controller.*;
import database.Database;
import model.user.User;

import model.user.User;

import javax.xml.transform.Result;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Application {
    public static User user;
    public static void sceneHandler() {

        int choice = MenuHandler.showStartMenu();
        while (choice != 3) {
            switch (choice) {
                case 1 -> MenuHandler.onSignUpButton();
                case 2 -> {
                    user = MenuHandler.onLoginButton();
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
                case 1 -> {
                    MenuHandler.addFriend(user);
                }
                case 2 -> {
                    user.printFriends();
                }
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