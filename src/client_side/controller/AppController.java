package client_side.controller;

import model.user.User;

import java.io.*;
import java.net.Socket;

public class AppController {
    public static User user;

    private static Socket socket;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;


    public static void initialNetwork(){
        try {
            socket = new Socket("localhost", 9988);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getUser(){
        try {
            user = (User) inputStream.readObject();
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
