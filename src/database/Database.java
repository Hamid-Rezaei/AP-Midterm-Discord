package database;

import client_side.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

import static client_side.controller.Authentication.generateUniqueRandomId;
import static client_side.view.MenuHandler.*;

public class Database {
    static Scanner sc = new Scanner(System.in);

    public static Connection connectToDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/discord", "root", "177013");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void insertToDB(String username, String password, String email, String phoneNumber, InputStream avatar) {


        try {
            Connection connection = connectToDB();

            String query = "insert into users (userName, password, email, phoneNumber,userID,avatar) values(?, ?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setString(5, String.valueOf(generateUniqueRandomId(connection)));
            statement.setBlob(6, avatar);
            statement.execute();
            connection.close();
            if (avatar != null) {
                avatar.close();
            }
            System.out.println(username + " was signed up successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static User retrieveFromDB() {
        try {

            Connection connection = connectToDB();
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

    public static boolean updateUser(User user){
        Connection connection = connectToDB();
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE Users SET password = " + "'" + user.getPassword() + "'" + "WHERE userName = " + "'" + user.getUsername() + "';");
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

}
