package controller;

import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;
    private String username;

    public Connection(Socket socket,ObjectOutputStream outputStream, ObjectInputStream inputStream, String username) throws IOException {
        this.socket = socket;
        this.username = username;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Message receiveMessage() {
        try {
            return (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendAllMessages(ArrayList<Message> messages) {
        try {
            outputStream.writeObject(messages);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Message> receiveAllMessages() {
        try {
            return (ArrayList<Message>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isGood(){
        return socket.isConnected();
    }
}
