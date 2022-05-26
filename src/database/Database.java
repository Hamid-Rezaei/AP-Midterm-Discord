package database;

import client_side.User;

import java.sql.*;
import java.util.Scanner;

import static client_side.MenuHandler.*;

public class Database {
    static Scanner sc = new Scanner(System.in);

    public static void insertToDB() {
        String username = getUsername();
        checkUniqueUsername(username);
        String password = getPassword();
        String email = getEmail();
        System.out.print("Enter Phone number(optional): ");
        String phoneNumber = sc.nextLine();
        getAvatar();
        //User user = new User(username, password, email, phoneNumber);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/discord", "root", "177013");

            String query = "insert into users (userName, password, email, phoneNumber,userID,avatar) values(?, ?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setString(5, String.valueOf(generateUniqueRandomId(connection)));
            statement.setString(6, "");
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(username + " was signed up successfully.");

    }


    public static User retrieveFromDB() {
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/discord", "root", "177013");
            Statement statement = connection.createStatement();
            System.out.print("Enter username : ");
            String username = sc.nextLine();
            ResultSet resultSet = statement.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (!resultSet.next()) {
                System.out.println("this username does not exist.");
            } else {
                System.out.print("Enter password : ");
                String password = sc.nextLine();
                String realPassword = resultSet.getString("password");
                if (password.equals(realPassword)) {
                    return createUser(resultSet);
                } else {
                    System.out.println("Wrong password.");
                    showMenu(); // Create a new LoginMenu
                }

            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static User createUser(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("userName");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phoneNumber");
        // get avatar = resultSet.getString("phoneNumber");

        return new User(username, password, email, phoneNumber);

    }


}
