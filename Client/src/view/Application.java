package view;

import controller.*;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static view.MenuHandler.*;

public class Application {
    //fields

    public static User user;
    public static AppController appController;

    // handling application menus.

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
            default -> {
            }
        }
    }

    private static void serverMenuHandler() {
        int choice = showServerMenu();
        switch (choice) {
            //case 1 -> addNewServer();
            //case 2 -> listOfAllServer();
            default -> inApplication();
        }
    }

    private static void friendMenuHandler() {
        int choice = showFriendMenu();
        switch (choice){
            case 1 -> System.out.println(appController.friendRequest(user.getUsername(), getFriendName()));
            //case 2 -> chatWithFriend();x
            case 3 -> listOfFriends();
            default -> inApplication();
        }
    }

    private static void settingMenuHandler() {
        int choice = showSettingMenu();
        switch (choice) {
            //case 1 -> changePass();
            //case 2 -> changeAvatar();
            default -> inApplication();
        }
    }


    //Required methods for parts of friendMenuHandler and serverMenuHandler.


    private static void listOfFriends() {
        ArrayList<User> friends = user.getFriends();
        printFriends(friends);
        User friend = getFriendForChat(friends);
        //user.goToDirectChat(friend);
    }


    private static void printFriends(ArrayList<User> friends) {
        int i = 1;
        for (User friend : friends) {
            System.out.println(i++ + ". " + friend.toString());
        }
    }


    private static User getFriendForChat(ArrayList<User> friends) {
        System.out.print("chose friend you want to chat with (Enter number): ");
        int friendToChat = 0;
        User friend = null;
        try {
            friendToChat = Integer.parseInt(sc.nextLine());
            friend = friends.get(friendToChat - 1);
        } catch (NumberFormatException e) {
            System.out.println("Oops! something is wrong with your input.");
            getFriendForChat(friends);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Oops! input is out of array bound.");
            getFriendForChat(friends);
        }

        return friend;
    }

    //Running Application.

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
