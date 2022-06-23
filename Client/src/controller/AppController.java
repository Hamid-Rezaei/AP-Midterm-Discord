package controller;

import model.User;

import java.io.*;
import java.net.*;
import java.util.*;

public class AppController {
    public enum ServerErrorType {
        NO_ERROR, INVALID_USER_NAME, INVALID_PASS_WORD, INVALID_EMAIL, SERVER_CONNECTION_FAILED, UNKNOWN_ERROR
    }

    private static User currentUser;
    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;

    public AppController(){
        setupConnection();
    }

    private void setupConnection() {
        try {
            socket = new Socket("localhost", 7777);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException x) {
            x.printStackTrace();
        }
    }


    public boolean signUp(String username, String password, String email, String phoneNum, InputStream avatar) {

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
            String result = inputStream.readUTF();
            if (result.equals("true")) return true;
            else return false;
        } catch (IOException x) {
            x.printStackTrace();
            return false;
        }
    }
}
