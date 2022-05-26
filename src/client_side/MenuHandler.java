package client_side;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.*;
import java.sql.*;

import static database.Database.*;


/**
 * The type Menu handler.
 */
public class MenuHandler {
    /**
     * The Sc.
     */
    static Scanner sc = new Scanner(System.in);

    /**
     * Matched input boolean.
     *
     * @param regex the regex
     * @param input the input
     * @return the boolean
     */
    public static boolean matchedInput(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    /**
     * Show menu int.
     *
     * @return the int
     */
    public static int showMenu() {
        System.out.println("""
                1)Signup
                2)Login
                3)Exit""");
        int choice = 10;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entered Input wasn't valid.");
            return showMenu();
        }
        return choice;
    }

    /**
     * On sign up button.
     */
    public static void onSignUpButton() {
        insertToDB();
    }


    /**
     * On login button user.
     *
     * @return the user
     */
    public static User onLoginButton() {
        return retrieveFromDB();
    }


    /**
     * Check unique username.
     *
     * @param username the username
     */
    public static void checkUniqueUsername(String username) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/discord", "root", "177013");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (resultSet.next()) {
                System.out.println("Username Already Exists.");
                onSignUpButton();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Generate unique random id int.
     *
     * @param connection the connection
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int generateUniqueRandomId(Connection connection) throws SQLException {
        Random random = new Random();
        int id = random.nextInt(1000);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from users where userID= " + id);

        if (resultSet.next()) {
            return generateUniqueRandomId(connection);
        } else {
            return id;
        }

    }


    /**
     * Gets username.
     *
     * @return the username
     */
    public static String getUsername() {
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        if (matchedInput(usernameRegex, username)) {
            return username;
        } else {
            System.out.println("Username length must be at least 6 and contains numbers and characters.");
            return getUsername();
        }
    }


    /**
     * Gets password.
     *
     * @return the password
     */
    public static String getPassword() {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (matchedInput(passwordRegex, password)) {
            return password;
        } else {
            System.out.println("password length must be at least 8 and contains numbers and characters.");
            return getPassword();
        }
    }


    /**
     * Gets email.
     *
     * @return the email
     */
    public static String getEmail() {
        String emailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z]+(?:\\.[a-zA-Z]+)*$";
        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        if (matchedInput(emailRegex, email)) {
            return email;
        } else {
            System.out.println("Entered email wasn't valid.");
            return getEmail();
        }

    }

    /**
     * Gets avatar.
     */
    public static void getAvatar() {
        System.out.print("Enter image address(phg format):");
        String path = sc.nextLine();

        try {
            BufferedImage image = ImageIO.read(new File(path));
            ImageIO.write(image, "png", new File("F:\\Projects\\Java_work_space\\Projects\\Discord\\AP-Midterm-Discord\\image.png"));
        } catch (IOException e) {

        }


    }


}
