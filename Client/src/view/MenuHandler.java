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

    /**
     * The constant sc.
     */
    public static Scanner sc = new Scanner(System.in);


    /**
     * Return choice int.
     *
     * @return the int
     */
    public static int returnChoice() {
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
        return choice;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
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


    /**
     * Gets email.
     *
     * @return the email
     */
    public static String getEmail() {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        return email;
    }

    /**
     * Get phone number.
     *
     * @return the string
     */
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
     *
     * @return the avatar
     */
    public static FileInputStream getAvatar()   {
        System.out.print("Enter image address:");
        String path = sc.nextLine();

        try {
            FileInputStream img = new FileInputStream(path);

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

    /**
     * Show start menu.
     *
     * @return the int
     */
    public static int showStartMenu() {
        System.out.println("""
                1) Signup
                2) Login
                3) Exit""");
        System.out.print("> ");
        return returnChoice();
    }

    /**
     * In app menu.
     *
     * @return the int
     */
    public static int inAppMenu() {
        System.out.println("""
                1) Severs
                2) Friends
                3) Settings
                4) change Status
                5) Back""");
        System.out.print("> ");
        return returnChoice();
    }


    /**
     * Show server menu.
     *
     * @return the int
     */
    public static int showServerMenu() {
        System.out.println("""
                1) Add new server
                2) List of all servers
                3) Back""");
        System.out.print("> ");
        return returnChoice();
    }


    /**
     * Show friend menu.
     *
     * @return the int
     */
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


    /**
     * Show setting menu.
     *
     * @return the int
     */
    public static int showSettingMenu() {
        System.out.println("""
                1) Change password
                2) Change avatar
                3) Back""");
        System.out.print("> ");
        return returnChoice();
    }

    /**
     * Show in guild.
     *
     * @return the int
     */
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

    /**
     * Server setting.
     *
     * @return the int
     */
    public static int serverSetting(){
        System.out.println("""
                1) Changing server name
                2) delete server
                3) Back""");
        System.out.print("> ");
        return returnChoice();
    }


    /**
     * Get friend name.
     *
     * @return the string
     */
    public static String getFriendName(){
        System.out.print("Enter username: ");
        return sc.nextLine();
    }
}
