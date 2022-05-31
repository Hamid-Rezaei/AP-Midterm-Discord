package client_side.controller;

import client_side.model.*;
import client_side.view.Application;
import client_side.view.MenuHandler;

import java.io.*;
import java.net.Socket;

public class AppController {
    private static User currentUser;
    private static Socket socket;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;


    public static void initialNetwork(){
        try {
            socket = new Socket("localhost", 7788);
            System.out.println("sath");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void initialUser(){
        try {
            objectOutputStream.writeUTF("login");
            objectOutputStream.flush();
            currentUser = (User) objectInputStream.readObject();
            Application.setUser(currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




}
