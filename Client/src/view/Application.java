package view;

import controller.*;
import model.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
        switch (choice) {
            case 1 -> System.out.println(appController.friendRequest(user.getUsername(), getFriendName()));
            case 2 -> listOfFriendRequests();
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

    private static void listOfFriendRequests() {
        HashSet<String> friendRequests = appController.friendRequestList(user.getUsername());
        System.out.println("All friend requests: ");
        for (String friendRequest : friendRequests) {
            System.out.println(friendRequest);
        }
        System.out.print("Enter usernames you want to accept (user1-user2-...): ");
        HashSet<String> accepted = new HashSet<>(Arrays.asList(sc.nextLine().split("-")));

        System.out.print("Enter usernames you want to reject (user1-user2-...): ");
        HashSet<String> rejected = new HashSet<>();
        String[] rej = sc.nextLine().split("-");
        for (int i = 0; i < rej.length; i++) {
            if (!accepted.contains(rej[i])) {
                rejected.add(rej[i]); //(Arrays.asList(sc.nextLine().split("-")));
            }
        }
        String response = appController.revisedFriendRequests(user.getUsername(), accepted, rejected);
        System.out.println(response);

    }


    private static void listOfFriends() {
        HashSet<String> friends = appController.friendList(user.getUsername());
        printFriends(friends);
        User friend = getFriendForChat();
        appController.requestForDirectChat(friend);
    }


    private static void printFriends(HashSet<String> friends) {
        int i = 1;
        for (String friend : friends) {
            System.out.println(i++ + ". " + friend);
        }
    }


    private static User getFriendForChat() {
        System.out.print("chose friend you want to chat with (Enter username): ");
        String friendToChat = sc.nextLine();
        return appController.getUser(friendToChat);
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
