package controller;

import model.User;

import java.io.*;
import java.net.*;
import java.util.*;

public class AppController {
    public enum ServerErrorType {
        NO_ERROR(1), USER_ALREADY_EXISTS(2), SERVER_CONNECTION_FAILED(3), DATABASE_ERROR(4), UNKNOWN_ERROR(404);

        private int code;

        ServerErrorType(int code) {
            this.code = code;
        }
    }

    private static User currentUser;
    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;

    public AppController() {
        setupConnection();
    }

    private void setupConnection() {
        try {
            socket = new Socket("localhost", 7777);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException x) {
            System.out.println("SERVER CONNECTION FAILED.");
            System.exit(0);
        }
    }


    public User login(String username, String password) {
        try {
            outputStream.writeUTF("login");
            outputStream.flush();
            outputStream.writeUTF(username + " " + password);
            outputStream.flush();
            User user = (User) inputStream.readObject();
            if(user == null){
                return user;
            }
            int avatarSize = inputStream.readInt();
            byte[] avatar = new byte[avatarSize];
            inputStream.readFully(avatar, 0, avatarSize);
            user.setAvatar(avatar);
            return user;
        } catch (IOException e) {
            System.out.println("Can not write for server.");
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Can not read from server.");
            return null;
        }
    }


    public String signUp(String username, String password, String email, String phoneNum, InputStream avatar) {

        try {
            outputStream.writeUTF("signUp");
            outputStream.flush();
            outputStream.writeUTF(username + " " + password + " " + email + " " + phoneNum);
            outputStream.flush();
            byte[] img = avatar.readAllBytes();
            outputStream.writeInt(img.length);
            outputStream.flush();
            outputStream.write(img, 0, img.length);
            outputStream.reset();
            return parseError(inputStream.readInt());
        } catch (IOException x) {
            x.printStackTrace();
            return "IOException";
        }
    }

    public String parseError(int errorCode) {
        String error;
        switch (errorCode) {
            case 1:
                error = "Success";
                break;
            case 2:
                error = "This user already exists.";
                break;
            case 3:
                error = "Connection with server failed.";
                break;
            case 4:
                error = "There was a problem with database.";
                break;
            default:
                error = "UNKNOWN ERROR.";
                break;

        }
        return error;
    }
}
