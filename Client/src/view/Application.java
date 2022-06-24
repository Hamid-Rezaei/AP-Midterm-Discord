package view;

import controller.*;
import model.*;

import java.io.InputStream;

import static view.MenuHandler.*;

public class Application {
    public static User user;
    public static AppController appController;

    private static void signUpMenu() {
        String username = MenuHandler.getUsername();
        String password = MenuHandler.getPassword();
        String email = MenuHandler.getEmail();
        String phoneNumber = getPhoneNumber();
        InputStream img = MenuHandler.getAvatar();
        String authenticationStatus = Authentication.checkValidationOfInfo(username, password, email);
        if (authenticationStatus.equals("Success")) {
            String signUpResult = appController.signUp(username, password, email, phoneNumber, img);
            if (signUpResult.equals("Success")) {
                System.out.println("SignUp successfully");
            } else {
                System.out.println(signUpResult);
                signUpMenu();
            }
        } else {
            System.out.println(authenticationStatus);
            signUpMenu();
        }

    }


    private static void loginMenuHandler() {
        String username = MenuHandler.getUsername();
        String password = MenuHandler.getPassword();
        user = appController.login(username, password);
        if (user == null) {
            System.out.println("Oops! something is wrong (user is null)");
        } else {
            System.out.println("Login Successfully.");
            inApplication();
        }
    }

    private static void inApplication() {
        int choice = inAppMenu();
        switch (choice) {
            case 1 -> serverMenuHandler();
            case 2 -> friendMenuHandler();
            case 3 -> settingMenuHandler();
            default -> {}
        }
    }

    private static void serverMenuHandler(){
        int choice = showServerMenu();
        switch (choice){
            //case 1 -> addNewServer();
            //case 2 -> listOfAllServer();
            default -> inApplication();
        }
    }

    private static void friendMenuHandler(){
        int choice = showFriendMenu();
        switch (choice){
            //case 1 -> addNewFriend();
            //case 2 -> chatWithFriend();
            default -> inApplication();
        }
    }

    private static void settingMenuHandler(){
        int choice = showSettingMenu();
        switch (choice){
            //case 1 -> changePass();
            //case 2 -> changeAvatar();
            default -> inApplication();
        }
    }

    private static void runApp() {
        runLoop:
        while (true) {
            int choice = MenuHandler.showStartMenu();
            switch (choice) {
                case 1 -> signUpMenu();
                case 2 -> loginMenuHandler();
                case 3 -> {
                    break runLoop;
                }
                default -> choice = MenuHandler.showStartMenu();
            }

        }
    }

    public static void main(String[] args) {
        appController = new AppController();
        runApp();
    }

}
