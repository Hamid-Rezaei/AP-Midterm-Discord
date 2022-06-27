package model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private String content;
    private String authorName;
    private LocalDateTime date;
    private boolean isFile;
    private int fileSize;
    private byte[] file;

    public Message(String content, String authorName, LocalDateTime date) {
        this.content = content;
        this.authorName = authorName;
        this.date = date;
    }

    public Message(String content, String authorName, LocalDateTime date, boolean isFile) {
        this.content = content;
        this.authorName = authorName;
        this.date = date;
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

    public String getPath(){
        // file>path
        String[] parts = this.content.split(">");
        String path = parts[1];
        return path;
    }

    public String getFileName(){
        String[] parts = getPath().split("[/\\\\]");
        return parts[parts.length-1];
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


    @Override
    public String toString() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern(
                "MMM-dd, HH:mm");
        return String.format("[%s] %s: %s", date.format(formatter), authorName, content);
    }
}
