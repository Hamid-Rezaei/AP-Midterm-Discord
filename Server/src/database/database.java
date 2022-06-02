package database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class database {
    public static Connection connectToDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/discord", "root", "177013");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static synchronized boolean insertToDB(String username, String password, String email, String phoneNumber, String token,InputStream avatar) {


        try {
            Connection connection = connectToDB();

            String query = "insert into users (userName, password, email, phoneNumber,userID,avatar) values(?, ?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setString(5, token);
            statement.setBlob(6, avatar);
            statement.execute();
            connection.close();
            if (avatar != null) {
                avatar.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
