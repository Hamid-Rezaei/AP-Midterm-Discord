package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

class ListenForMsg implements Runnable {
    private volatile boolean exit;
    ObjectInputStream inputStream;
    User currUser;


    public ListenForMsg(ObjectInputStream objectInputStream, User currUser) {
        this.inputStream = objectInputStream;
        this.currUser = currUser;
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                Message msg = (Message) inputStream.readObject();
                if (msg.isFile()) {
                    if (!msg.getAuthorName().equals(currUser.getUsername()))
                        System.out.println("file saved in: " + saveFileInDownloads(msg));

                } else {
                    System.out.println(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
    }

    public void stop() {
        exit = true;
    }

    private String saveFileInDownloads(Message message) {
        try {
            byte[] bytes = message.getFile();
            String name = message.getFileName();
            String path = "downloads/" + currUser.getUsername() + "/from_" + message.getAuthorName();
            File theDir = new File(path);
            if (!theDir.exists()) {
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
}

public class Chat implements Runnable, Serializable {
    private ArrayList<Message> messages;
    private User currUser;
    private transient ObjectOutputStream outputStream;
    private transient ObjectInputStream inputStream;
    private volatile boolean exit = false;
    String input;
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
            if (!theDir.exists()) {
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
        exit = false;
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        Thread snedMsg = new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
        snedMsg.start();
//        ListenForMsg listenForMsg = new ListenForMsg(inputStream, currUser);
//        Thread listenThread = new Thread(listenForMsg, "listenForMsg");
//        listenThread.start();//new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!exit) {
//                    try {
//                        if (exit) {
//                            break;
//                        }
//                        Message msg = (Message) inputStream.readObject();
//                        if (msg.isFile()) {
//                            if (!msg.getAuthorName().equals(currUser.getUsername()))
//                                System.out.println("file saved in: " + saveFileInDownloads(msg));
//
//                        } else {
//                            System.out.println(msg);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        break;
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        break;
//                    }
//
//                }
//            }
//
//            @Deprecated
//            public void stop() {
//                exit = true;
//            }
//        });

        while (!input.equals("#exit")) {
            try {
                Message msg = (Message) inputStream.readObject();
                if (msg.isFile()) {
                    if (!msg.getAuthorName().equals(currUser.getUsername()))
                        System.out.println("file saved in: " + saveFileInDownloads(msg));
                } else {
                    System.out.println(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }

//        Thread thread = Thread.currentThread();
//        if (thread.getName().equals("listenForMsg")) {
//            thread.stop();
//        }
    }
}
