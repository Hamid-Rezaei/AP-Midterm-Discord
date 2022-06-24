package Database;

import controller.ServerErrorType;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.plaf.nimbus.State;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;

public class Database {
    public static Connection connectToDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/discord", "root", "177013");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ServerErrorType insertToDB(String username, String password, String email, String phoneNumber, String token, byte[] avatar) {

        try {
            Connection connection = connectToDB();
            if (connection == null) {
                return ServerErrorType.SERVER_CONNECTION_FAILED;
            }
            Statement checkuserExists = connection.createStatement();
            ResultSet foundUser = checkuserExists.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (foundUser != null) {
                return ServerErrorType.USER_ALREADY_EXISTS;
            }
            foundUser = checkuserExists.executeQuery("select * from users where email = " + "'" + email + "'");
            if (foundUser != null) {
                return ServerErrorType.USER_ALREADY_EXISTS;
            }
            String insertQuery = "insert into users (userName, password, email, phoneNumber,userID,avatar) values(?, ?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setString(5, token);
            statement.setBytes(6, avatar);
            statement.execute();
            connection.close();
            return ServerErrorType.NO_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerErrorType.DATABASE_ERROR;
        }

    }


    public static User retrieveFromDB(String username, String password) {
        try {

            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (!resultSet.next()) {
                return null;
            } else {
                String realPassword = resultSet.getString("password");
                if (password.equals(realPassword)) {
                    return createUser(resultSet);
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User retrieveFromDB(String username) {
        try {

            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (!resultSet.next()) {
                return null;
            }
            connection.close();
            return createUser(resultSet);
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
        String uId = resultSet.getString("userID");
        BufferedImage avatar = null;
        try {
            avatar = ImageIO.read(new ByteArrayInputStream(resultSet.getBytes("avatar")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new User(username, password, email, phoneNumber, uId, avatar);

    }

    public static ServerErrorType sendFriendRequest(String fromUser, String targetUser) {
        try {
            Connection connection = connectToDB();
            {
                Statement checkIfAlreadyExists = connection.createStatement();
                ResultSet checkRules = checkIfAlreadyExists.executeQuery("select from_user, to_user from requests where from_user = " + "'" + fromUser + "'" + "and to_user = " + "'" + targetUser + "'");
                boolean firstRule = false;
                boolean secondRule = false;
                if (!checkRules.next()) {
                    firstRule = true;
                }
                checkRules = checkIfAlreadyExists.executeQuery("select from_user, to_user from requests where from_user = " + "'" + targetUser + "'" + "and to_user = " + "'" + fromUser + "'");
                if (!checkRules.next()) {
                    secondRule = true;
                }
                if (!(firstRule & secondRule)) {
                    return ServerErrorType.Duplicate_ERROR;
                }
            }
            String insertQuery = "insert into requests (to_user, from_user) values(?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, targetUser);
            statement.setString(2, fromUser);
            statement.execute();
            connection.close();
            return ServerErrorType.NO_ERROR;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            return ServerErrorType.Duplicate_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerErrorType.DATABASE_ERROR;
        }
    }


    public static HashSet<String> viewFriendRequestList(String targetUser) {
        HashSet<String> reqList = new HashSet<>();
        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select from_user from requests where to_user = " + "'" + targetUser + "'");
            while (resultSet.next()) {
                reqList.add(resultSet.getString(1));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reqList;
    }

    public static HashSet<String> viewFriendList(String username) {
        HashSet<String> friendList = new HashSet<>();
        try {
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select friends_id from friends where user_id = " + "'" + username + "'");
            while (resultSet.next()) {
                friendList.add(resultSet.getString(1));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendList;
    }
}

