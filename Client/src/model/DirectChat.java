package model;

import model.*;

import java.net.Socket;
import java.util.ArrayList;

public class DirectChat implements Runnable{

    private ArrayList<Message> messages;
    private String chatId;
    private ArrayList<User> participants;
    private ArrayList<Socket> usersInChat;


    public DirectChat(int userId1, int userId2) {
        messages = new ArrayList<>();
        this.chatId = userId1 + "" + userId2;
    }


    @Override
    public void run() {

    }
}