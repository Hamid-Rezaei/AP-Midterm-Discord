package view;

import controller.*;
import model.*;
import model.guild.*;

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
            case 1 -> {
                addNewServer();
                serverMenuHandler();
            }
            case 2 -> {
                Guild guild = listOfAllServer();
                inSelectedServer(guild);
                serverMenuHandler();
            }
            default -> inApplication();
        }
    }


    // server handling section

    private static void addNewServer() {
        System.out.print("Enter server name: ");
        String name = sc.nextLine();
        GuildUser owner = new GuildUser(user, new Role("owner"));
        System.out.println(appController.addServer(new Guild(name, owner)));
    }


    private static Guild listOfAllServer() {
        ArrayList<Guild> guilds = appController.listOfJoinedServers();
        if (guilds != null) {
            int i = 1;
            for (Guild guild : guilds) {
                System.out.println(i + ". " + guild.getName());
                i++;
            }
            System.out.print("Enter server number to login: ");
            int choice = Integer.parseInt(sc.nextLine());
            return guilds.get(choice - 1);
        } else {
            System.out.println("You dont have any server...");
            return null;
        }

    }

    private static void inSelectedServer(Guild guild) {
        int choice = showInGuild();
        switch (choice) {
            case 1 -> {
                ArrayList<TextChannel> textChannels = guild.getTextChannels();
                int i = 1;
                for(TextChannel textChannel: textChannels){
                    System.out.println(i++ + ". " + textChannel.getName());
                }
                //for (TextChannel : )

            }
            case 2 -> {
                ArrayList<VoiceChannel> voiceChannels = guild.getVoiceChannels();
                inSelectedServer(guild);
            }
            case 3 -> {
                System.out.println(addMemberToServer(guild));
                guild = appController.getGuild(guild.getOwnerName(),guild.getName());
                inSelectedServer(guild);
            }
            case 4 -> {
                addNewTextChannel(guild);
                guild = appController.getGuild(guild.getOwnerName(),guild.getName());
                inSelectedServer(guild);
            }

            case 5 -> {
                addNewVoiceChannel(guild);
                guild = appController.getGuild(guild.getOwnerName(),guild.getName());
                inSelectedServer(guild);
            }

            case 6 -> {
                //guild.removeTextChannel();
                guild = appController.getGuild(guild.getOwnerName(),guild.getName());
                inSelectedServer(guild);
            }
            case 8 ->{
                HashSet<GuildUser> guildUsers = guild.getGuildUsers();
                int i = 1;
                for(GuildUser guildUser: guildUsers){
                    System.out.println(i + ". " + guildUser.getUsername());
                    i++;
                }
                inSelectedServer(guild);
            }
            case 9 ->{
                int i = serverSetting();
                inSelectedServer(guild);
            }
            default -> serverMenuHandler();
        }
    }

    private static void addNewVoiceChannel(Guild guild) {
    }

    private static void addNewTextChannel(Guild guild) {
        String respone = appController.addNewTextChannel(guild);
        System.out.println(respone);
    }

    private static String addMemberToServer(Guild guild) {
        String name = getFriendName();
        String respond = appController.addMemberToServer(name,guild);
        return respond;
    }


    private static void friendMenuHandler() {
        int choice = showFriendMenu();
        switch (choice) {
            case 1 -> {
                System.out.println(appController.friendRequest(user.getUsername(), getFriendName()));
                friendMenuHandler();
            }
            case 2 -> {
                listOfFriendRequests();
                friendMenuHandler();
            }
            case 3 -> {
                listOfFriends();
                friendMenuHandler();
            }
            case 4 -> {
                blockUser();
                friendMenuHandler();
            }
            case 5 -> {
                showBlockList();
                friendMenuHandler();
            }
            default -> inApplication();
        }
    }

    private static void blockUser() {
        System.out.print("enter username of the person you want to block:");
        String username = sc.nextLine();
        System.out.println(appController.blockUser(username));
    }

    private static void showBlockList() {
        HashSet<String> blockedList = appController.blockedList();
        if (blockedList == null) {
            System.out.println("something went wrong while retrieving blocked list.");
        } else {
            for (String user : blockedList) {
                System.out.println(user);
            }
            System.out.print("Enter username of the person you want to unblock(if none, just press enter.): ");
            String unblockTarget = sc.nextLine();
            if (unblockTarget.length() > 0) {
                System.out.println(appController.unblockUser(unblockTarget));
            }
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


    //Required methods for parts of friendMenuHandler.

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
        if (friend == null)
            return;
        Chat directChat = appController.requestForDirectChat(friend);
        // what's happened here with directChat...
    }


    private static void printFriends(HashSet<String> friends) {
        int i = 1;
        for (String friend : friends) {
            System.out.println(i++ + ". " + friend);
        }
    }


    private static User getFriendForChat() {
        System.out.print("chose friend you want to chat with (Enter username, Enter\"#exit\"to get back): ");
        String friendToChat = sc.nextLine();
        System.out.println("You are in chat with: " + friendToChat);
        return appController.getUser(friendToChat);
    }


    //Running Application.

    private static void runApp() {
        runLoop:
        while (true) {
            int choice = MenuHandler.showStartMenu();
            switch (choice) {
                case 1 -> signUpMenu();
                case 2 -> {
                    loginMenuHandler();
                    appController = new AppController();
                }
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
