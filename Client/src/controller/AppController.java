package controller;

import model.User;

import java.io.*;
import java.net.*;
import java.util.*;

public class AppController {

    private static ArrayList<User> allUsers = new ArrayList<>();
    private static User currentUser;

    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;

    public void initializeNetwork() {
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
