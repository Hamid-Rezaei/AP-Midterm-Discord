package controller;

import model.Message;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DirectChatController implements Runnable{

    public static HashMap<String, DirectChatController> directChatControllers = new HashMap<>();
    private ArrayList<Message> messages;
    private int messagesSize;
    private String chatHashCode;
    private ArrayList<Connection> usersInChatConnection;


    public DirectChatController(String chatHashCode, ArrayList<Connection> usersInChatConnection) {
        messages = new ArrayList<>();
        this.usersInChatConnection = usersInChatConnection;
        this.chatHashCode = chatHashCode;
        directChatControllers.put(this.chatHashCode, this);
    }

    public synchronized void addMessage(Message message){
        messages.add(message);
    }

    public void broadcastMessages(){
        for(Connection connection: usersInChatConnection){
            for(Message message : messages){
                if(!message.getAuthorName().equals(connection.getUsername())){
                    connection.sendMessage(message);
                }
            }
        }

    }



    @Override
    public void run() {
        while (true){
           for(Connection connection : usersInChatConnection){
               addMessage(connection.receiveMessage());
               broadcastMessages();
           }
        }
    }
}
