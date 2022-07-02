package view;

import controller.*;
import model.*;
import model.guild.*;

import java.io.FileInputStream;
import java.io.IOException;
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
            System.out.println("username/password is wrong");
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
            case 4 -> {
                statusMenuHandler();
            }
            case 5 -> {}
            default -> inApplication();
        }
    }

    private static void statusMenuHandler() {
        ArrayList<String> statusList = new ArrayList<>(Arrays.asList("online", "offline", "idle", "do not disturb"));
        System.out.println("enter number of your new status: ");
        int i = 1;
        for (String status : statusList) {
            System.out.println(i++ + ". " + status);
        }
        int choice = returnChoice() - 1;
        if (!(choice >= 0 && choice <= 3)) {
            System.out.println("invalid number.");
        } else {
            String respond = appController.setStatus(statusList.get(choice),user.getUsername());
            System.out.println(respond);
        }
        inApplication();
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
                if (guild != null)
                    inSelectedServer(guild);
                else
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
            try {
                int choice = Integer.parseInt(sc.nextLine());
                Guild chosenGuild = guilds.get(choice - 1);
                guilds.clear();
                return chosenGuild;
            } catch (NumberFormatException e) {
                System.out.println("invalid input.");
                return null;
            }
        } else {
            System.out.println("You dont have any server...");
            return null;
        }

    }

    private static void inSelectedServer(Guild guild) {
        int choice = showInGuild();
        switch (choice) {
            case 1 -> {
                listOfTextChannel(guild);
                guild = appController.getGuild(guild.getOwnerName(), guild.getName());
                inSelectedServer(guild);
            }
            case 2 -> {
                ArrayList<VoiceChannel> voiceChannels = guild.getVoiceChannels();
                inSelectedServer(guild);
            }
            case 3 -> {
                System.out.println(addMemberToServer(guild));
                guild = appController.getGuild(guild.getOwnerName(), guild.getName());
                inSelectedServer(guild);
            }
            case 4 -> {
                addNewTextChannel(guild);
                guild = appController.getGuild(guild.getOwnerName(), guild.getName());
                inSelectedServer(guild);
            }

            case 5 -> {
                addNewVoiceChannel(guild);
                guild = appController.getGuild(guild.getOwnerName(), guild.getName());
                inSelectedServer(guild);
            }

            case 6 -> {
                //guild.removeTextChannel();
                guild = appController.getGuild(guild.getOwnerName(), guild.getName());
                inSelectedServer(guild);
            }

            case 8 -> {
                System.out.println(deleteMemberFromServer(guild));
                guild = appController.getGuild(guild.getOwnerName(), guild.getName());
                inSelectedServer(guild);
            }
            case 9 -> {
                guild = appController.getGuild(guild.getOwnerName(),guild.getName());
                HashSet<GuildUser> guildUsers = guild.getGuildUsers();
                int i = 1;
                for (GuildUser guildUser : guildUsers) {
                    System.out.println(i + ". " + guildUser.toString());
                    i++;
                }
                inSelectedServer(guild);
            }
            case 10 -> {
                int i = serverSetting();
                if (i == 1) {
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();
                    System.out.println(appController.changeGuildName(guild, newName));
                    guild.setName(newName);
                    guild = appController.getGuild(guild.getOwnerName(), newName);
                }
                inSelectedServer(guild);
            }
            default -> serverMenuHandler();
        }
    }

    private static void listOfTextChannel(Guild guild) {
        ArrayList<TextChannel> textChannels = guild.getTextChannels();
        int i = 1;
        System.out.println("enter number of textChannel you want to enter :");
        for (TextChannel textChannel : textChannels) {
            System.out.println(i++ + ". " + textChannel.getName());
        }
        int tChoice = returnChoice() - 1;
        TextChannel textChannel = textChannels.get(tChoice);
        System.out.println("you entered " + textChannel.getName());
        System.out.println("commands : ");
        System.out.println("""
                #exit : exit chat
                #pin>(msg index) : to pin message with given index
                #pins : show all pinned messages
                #file>(path to file): send a file
                #react>(msg index)>reaction(like-dislike-smile)
                """);
        appController.requestForGroupChat(guild, textChannel);
    }


    private static void addNewVoiceChannel(Guild guild) {
    }

    private static void addNewTextChannel(Guild guild) {
        String respone = appController.addNewTextChannel(guild);
        System.out.println(respone);
    }

    private static String addMemberToServer(Guild guild) {
        String name = getFriendName();
        String respond = appController.addMemberToServer(name, guild);
        return respond;
    }

    private static String deleteMemberFromServer(Guild guild) {
        String name = getFriendName();
        String respond = appController.deleteMemberFromServer(name, guild);
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
            case 1 -> {
                boolean isChanged = changePass();
                if (isChanged)
                    break;
                settingMenuHandler();
            }
            case 2 -> {
                changeAvatar();
                settingMenuHandler();
            }
            default -> inApplication();
        }
    }

    private static void changeAvatar() {
        try {
            FileInputStream img = getAvatar();
            if(img != null) {
                user.setAvatar(img.readAllBytes());
                System.out.println(appController.updateUser(user));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean changePass() {
        String pass = getPassword();
        if (Authentication.checkValidPass(pass)) {
            user.setPassword(pass);
            boolean updated = appController.updateUser(user);
            if (updated) {
                System.out.println("Password changed successfully. please login again");
                return true;
            } else {
                System.out.println("couldn't change your password");
            }

        } else {
            System.out.println("Oops, password is not valid");
            changePass();
        }
        return false;
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
        if (friend == null) {
            System.out.println("friend is null!");
            return;
        }
        System.out.println("You are in chat with: " + friend.getUsername());
        System.out.println("commands : ");
        System.out.println("""
                #exit : exit chat
                #pin>(msg index) : to pin message with given index
                #pins : show all pinned messages
                #file>(path to file): send a file
                #react>(msg index)>reaction(like-dislike-smile)
                """);
        appController.requestForDirectChat(friend);
        appController.removeFromDirectChat(user, friend);
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
