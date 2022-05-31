package database;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import model.user.*;

import javax.imageio.ImageIO;

import static client_side.controller.Authentication.generateUniqueRandomId;


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
        int uId = resultSet.getInt("userID");
        BufferedImage avatar = null;
        try {
            avatar = ImageIO.read(resultSet.getBlob("avatar").getBinaryStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new User(username, password, email, phoneNumber, avatar, uId);

    }

    public static ArrayList<User> userList() {
        Connection connection = connectToDB();
        try {
            Statement statement = connection.createStatement();

            ResultSet users = statement.executeQuery("select * from users");
            ArrayList<User> parsedUsers = new ArrayList<>();
            while(users.next()){
                parsedUsers.add(createUser(users));
            }
            connection.close();
            return parsedUsers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean updateUser(User user) {
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
