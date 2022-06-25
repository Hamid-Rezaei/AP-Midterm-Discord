package controller;

import model.*;

import java.io.IOException;
import java.util.ArrayList;

import static view.MenuHandler.sc;

public class DirectChatController implements Runnable {

    private ArrayList<Message> messages;
    private String chatHashCode;
    private Connection currUserConnection;
    private String username;


    public DirectChatController(Connection currUserConnection, String chatHashCode) {
        messages = new ArrayList<>();
        this.currUserConnection = currUserConnection;
        this.username = currUserConnection.getUsername();
        this.chatHashCode = chatHashCode;
    }

    public void sendMessage() {
        while (currUserConnection.isGood()) {
            String messageToSend = sc.nextLine();
            currUserConnection.sendMessage(new Message(messageToSend, username));
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msgFromFriend;
                while (currUserConnection.isGood()) {

                    msgFromFriend = (Message) currUserConnection.receiveMessage();
                    messages.add(msgFromFriend);
/*                    for (int i = 10; i >= 0; i--) {
                        System.out.println((messages.get(messages.size() - 1 - i)));
                    }*/
                    System.out.println(msgFromFriend);
                }
            }
        }).start();

    }

    @Override
    public void run() {
        sendMessage();
        listenForMessage();

    }
}