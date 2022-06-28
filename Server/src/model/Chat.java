package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Chat implements Runnable, Serializable {
    private ArrayList<Message> messages;
    private User currUser;
    private transient ObjectOutputStream outputStream;
    private transient ObjectInputStream inputStream;
    private boolean exit = false;

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


    public String saveFileInDownloads(Message message) {
        try {
            byte[] bytes = message.getFile();
            String name = message.getFileName();
            String path = "downloads/" + currUser.getUsername() + "/from_" + message.getAuthorName();
            File theDir = new File(path);
            if(!theDir.exists()){
                theDir.mkdirs();
            }
            File file = new File(path + "/" + name);
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return "couldn't save file.";
        }

    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Thread listenForMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!exit) {
                    try {
                        Message msg = (Message) inputStream.readObject();
                        if (msg.isFile()) {
                            if(!msg.getAuthorName().equals(currUser.getUsername()))
                                System.out.println("file saved in: " + saveFileInDownloads(msg));

                        } else {
                            System.out.println(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        listenForMsg.start();
        String input = scanner.nextLine();
        while (!input.equals("#exit")) {
            Message message;
            if (input.startsWith("#file")) {
                message = new Message(input, currUser.getUsername(), LocalDateTime.now(), true);
            } else {
                message = new Message(input, currUser.getUsername(), LocalDateTime.now());
            }
            try {
                outputStream.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            messages.add(message);
            input = scanner.nextLine();
        }
        exit = true;
    }
}