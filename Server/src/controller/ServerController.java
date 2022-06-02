package controller;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerController implements Runnable {

    public static ArrayList<ServerController> serverControllers = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String userToken;


    public ServerController(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        startService();
    }


    public void startService() {
        try {
            String task = inputStream.readUTF();
            if (task.equals("signUp")) signUp();
            else login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signUp() {
        try {
            String[] parts = inputStream.readUTF().split(" ");
            String username = parts[0];
            String pass = parts[1];
            String email = parts[2];
            String phoneNum = parts[3];
            String token = UUID.randomUUID().toString();
            //allUsers.add(new User(parts[0], parts[1], parts[]));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() {

    }
/*
    public static synchronized boolean register(String username, String password, String fullName) {
        for (User user: allUsers) {
            if (user.getUsername().equals(username)) return false;
        }
        allUsers.add(new User(username, password, fullName));
        return true;

    }
*/


    @Override
    public void run() {
        startService();
    }
}

