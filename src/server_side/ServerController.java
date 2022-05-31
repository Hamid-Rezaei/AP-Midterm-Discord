package server_side;


import model.user.User;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerController implements Runnable {

    public static ArrayList<ServerController> serverControllers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String appUsername;

    public ServerController(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        appUsername = inputStream.readUTF();
        serverControllers.add(this);
    }

    public void goToDirectChat(User me, User friend){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    @Override
    public void run() {

    }
}
