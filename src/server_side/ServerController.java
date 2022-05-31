package server_side;

import database.Database;

import java.io.*;
import java.net.*;

public class ServerController implements Runnable{

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ServerController(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }



    @Override
    public void run() {
        int choice = 0;
        try {
            choice = inputStream.readInt();
            if(choice == 1){
                outputStream.writeObject(Database.retrieveFromDB());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
