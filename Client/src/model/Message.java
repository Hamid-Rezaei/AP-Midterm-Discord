package model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Message implements Serializable {
    private String content;
    private String authorName;
    private LocalDateTime date;
    private boolean isFile;
    private int fileSize;
    private byte[] file;
    private HashMap<Reaction, ArrayList<String>> reactions;

    public Message(String content, String authorName, LocalDateTime date) {
        this.content = content;
        this.authorName = authorName;
        this.date = date;
        this.reactions = new HashMap<>();
    }

    public Message(String content, String authorName, LocalDateTime date, boolean isFile) {
        this.content = content;
        this.authorName = authorName;
        this.date = date;
        this.reactions = new HashMap<>();
        this.isFile = isFile;
        loadFile(getPath());
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public byte[] getFile() {
        return file;
    }

    public boolean isFile() {
        return isFile;
    }

    public String getPath() {
        // file>path
        String[] parts = this.content.split(">");
        String path = parts[1];
        return path;
    }

    public String getFileName() {
        String[] parts = getPath().split("[/\\\\]");
        return parts[parts.length - 1];
    }

    public void loadFile(String path) {
        try {
            InputStream input = new FileInputStream(path);
            file = input.readAllBytes();
            fileSize = file.length;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setReaction(String type, String name){
        Reaction reaction = new Reaction();
        switch (type){
            case "like" -> reaction.setLikeReact();
            case "dislike" -> reaction.setDisLikeReact();
            case "smile" -> reaction.setSmileReact();
        }
        addReaction(reaction, name);
    }


    public void addReaction(Reaction reaction, String name) {
        ArrayList<String> names = reactions.get(reaction);
        if (names == null) {
            names = new ArrayList<>();
        }
        names.add(name);
        reactions.put(reaction, names);
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern(
                "MMM-dd, HH:mm");
        StringBuilder stb = new StringBuilder();
        stb.append(String.format("[%s] %s: %s\n", date.format(formatter), authorName, content));
        for (Reaction reaction : reactions.keySet()) {
            StringBuilder tmp = new StringBuilder(reaction.getEmoji() + " -> ");
            for(String name : reactions.get(reaction)) {
                tmp.append(name).append(" ");
            }
            tmp.append("\n");
            stb.append(tmp);
        }
        return stb.toString();
    }
}
