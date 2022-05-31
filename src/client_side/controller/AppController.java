package client_side.controller;

import model.user.User;

import java.io.*;
import java.net.Socket;

public class AppController {

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public AppController(String username){
        initialNetwork(username);
    }


    public void initialNetwork(String username){
        try {
            socket = new Socket("localhost", 9988);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeUTF(username);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }



}
