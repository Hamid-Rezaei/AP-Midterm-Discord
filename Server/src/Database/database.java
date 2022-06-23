package Database;

import controller.ServerErrorType;

import java.sql.*;

public class database {
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

}
