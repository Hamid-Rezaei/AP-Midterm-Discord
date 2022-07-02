package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
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
    private volatile boolean exit = false;
    String input = "";

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
            path = path + "/" + name;
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return "couldn't save file.";
        }

    }

    @Override
    public void run() {
        Clip clip = null;
        exit = false;
        Scanner scanner = new Scanner(System.in);
        Thread snedMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!input.equals("#exit")) {
                    input = scanner.nextLine();
                    Message message;
                    try {
                        if (input.startsWith("#file")) {
                            message = new Message(input, currUser.getUsername(), LocalDateTime.now(), true);
                            outputStream.writeObject(message);
                            messages.add(message);
                        } else if (input.startsWith("#music")) {
                            message = new Message(input, currUser.getUsername(), LocalDateTime.now(), true);
                            outputStream.writeObject(message);
                            messages.add(message);
                        } else if (input.startsWith("#pause")) {
                            message = new Message(input, currUser.getUsername(), LocalDateTime.now());
                            outputStream.writeObject(message);
                        } else {
                            message = new Message(input, currUser.getUsername(), LocalDateTime.now());
                            outputStream.writeObject(message);
                            messages.add(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        snedMsg.start();

        while (!input.equals("#exit")) {
            try {
                Object message = inputStream.readObject();
                if (message instanceof Message msg) {
                    if (msg.isFile()) {
                        if (msg.getContent().startsWith("#music")) {
                            String path = saveFileInDownloads(msg);
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
                            clip = AudioSystem.getClip();
                            clip.open(audioInputStream);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            volume.setValue(20f * (float) Math.log10(0.15f));
                            clip.start();
                        } else if (!msg.getAuthorName().equals(currUser.getUsername()))
                            System.out.println("file saved in: " + saveFileInDownloads(msg));
                    } else {
                        Object index = inputStream.readObject();
                        int ind = Integer.valueOf(index.toString());
                        System.out.println(ind + ". " + msg);
                    }
                } else {
                    if (message.toString().equals("#pause")) {
                        if (clip != null && clip.isRunning())
                            //clip.stop();
                            clip.close();
                    } else
                        System.out.println(message.toString());
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

        if (clip != null && clip.isRunning())
            clip.close();
            //clip.stop();

    }
}
