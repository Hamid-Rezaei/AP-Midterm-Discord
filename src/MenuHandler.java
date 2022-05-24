import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.*;
import java.sql.*;
public class MenuHandler {
    static Scanner sc = new Scanner(System.in);

    public static boolean matchedInput(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static void showMenu() {
        System.out.println("""
                1)Sign up
                2)Login
                3)Exit""");
        int choice = 10;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entered Input wasn't valid.");
            showMenu();
        }
        switch (choice){
            case 1 -> onSignUpButton();
            case 2 -> onLoginButton();
            case 3 -> System.exit(0);
            default -> {
                System.out.println("Entered Input wasn't valid.");
                showMenu();
            }
        }
    }

    public static void onSignUpButton() {
        insertToDB();
    }


    public static void onLoginButton() {

    }

    public static void insertToDB() {
        String username = getUsername();
        String password = getPassword();
        String email = getEmail();
        System.out.print("Enter Phone number(optional): ");
        String phoneNumber = sc.nextLine();
        getAvatar();
        //User user = new User(username, password, email, phoneNumber);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/discord","root","177013");

            String query = "insert into users (userName, password, email, phoneNumber,userID,avatar) values(?, ?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2,password);
            statement.setString(3,email);
            statement.setString(4,phoneNumber);
            statement.setString(5,"1475");
            statement.setString(6,"");
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public static void insertToDB(String username, String password, String email, String phoneNumber){
//
//    }
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

    public static void getAvatar(){
        System.out.print("Enter image address(phg format):");
        String path  = sc.nextLine();

        try {
            BufferedImage image = ImageIO.read(new File(path));
            ImageIO.write(image , "png", new File("F:\\Projects\\Java_work_space\\Projects\\Discord\\AP-Midterm-Discord\\image.png"));
        } catch (IOException e) {

        }


    }


}
