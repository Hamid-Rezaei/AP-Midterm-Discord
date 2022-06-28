package view;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


/**
 * The type Menu handler.
 */
public class MenuHandler {

    public static Scanner sc = new Scanner(System.in);


    public static int returnChoice() {
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
        return choice;
    }

    public static String getUsername() {
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        return username;
    }


    /**
     * Gets password.
     *
     * @return the password
     */
    public static String getPassword() {
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        return password;
    }


    public static String getEmail() {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        return email;
    }

    public static String getPhoneNumber(){
        System.out.print("Enter Phone number(optional): ");
        String phoneNumber = sc.nextLine();
        if (phoneNumber == null) {
            phoneNumber = "";
        }
        return phoneNumber;
    }

    /**
     * Gets avatar.
     */
    public static InputStream getAvatar()   {
        System.out.print("Enter image address:");
        String path = sc.nextLine();

        try {
            InputStream img = new FileInputStream(path);
            return img;
        } catch (IOException e) {
            try {
                return new FileInputStream("./src/defaultAvatar.png");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return null;

            }
        }
    }

    public static int showStartMenu() {
        System.out.println("""
                1) Signup
                2) Login
                3) Exit""");
        System.out.print("> ");
        return returnChoice();
    }

    public static int inAppMenu() {
        System.out.println("""
                1) Severs
                2) Friends
                3) Settings
                4) Back""");
        System.out.print("> ");
        return returnChoice();
    }


    public static int showServerMenu() {
        System.out.println("""
                1) Add new server
                2) List of all servers
                3) Back""");
        System.out.print("> ");
        return returnChoice();
    }


    public static int showFriendMenu() {
        System.out.println("""
                1) Send friend request
                2) List of friend request
                3) List of friends
                4) Block a user
                5) Blocked list
                6) Back""");
        System.out.print("> ");
        return returnChoice();
    }


    public static int showSettingMenu() {
        System.out.println("""
                1) Change password
                2) Change avatar
                3) Back""");
        System.out.print("> ");
        return returnChoice();
    }

    public static int showInGuild(){
        System.out.println("""
                1) List Of text channel
                2) List Of voice channel
                3) Add member
                4) Add text channel
                5) Add voice channel
                6) Delete text channel
                7) Delete voice channel
                8) Delete member
                9) List of members
                10) Server setting
                11) Back""");
        System.out.print("> ");
        return returnChoice();
    }

    public static int serverSetting(){
        System.out.println("""
                1) Changing server name
                2) Back""");
        System.out.print("> ");
        return returnChoice();
    }


    public static String getFriendName(){
        System.out.print("Enter username: ");
        return sc.nextLine();
    }
}
