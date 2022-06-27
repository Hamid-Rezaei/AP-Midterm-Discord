package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Chat implements Runnable, Serializable {
    private ArrayList<Message> messages;
    private User currUser;
    private transient ObjectOutputStream outputStream;
    private transient ObjectInputStream inputStream;

    public Chat() {
        messages = new ArrayList<>();
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Chat(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Thread listenForMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Message msg = (Message) inputStream.readObject();
                        System.out.println(msg.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        listenForMsg.start();
        while (!input.equals("#exit")) {
            try {
                Message message = new Message(input, currUser.getUsername());
                outputStream.writeObject(message);
                messages.add(message);
                input = scanner.nextLine();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        listenForMsg.interrupt();
    }
}
