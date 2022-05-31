package model.message;

import model.user.User;

import java.util.ArrayList;

public class DirectChat implements Runnable{

    private ArrayList<Message> messages;
    private String chatId;

    public DirectChat(int userId1, int userId2) {
        messages = new ArrayList<>();
        this.chatId = userId1 + "" + userId2;
    }


    @Override
    public void run() {

    }
}