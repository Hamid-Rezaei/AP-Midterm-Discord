package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    String content;
    String authorName;
    LocalDateTime date;
    boolean isFile;

    public Message(String content, String authorName, LocalDateTime date) {
        this.content = content;
        this.authorName = authorName;
    }

    public Message(String content, String authorName, LocalDateTime date, boolean isFile) {
        this.content = content;
        this.authorName = authorName;
        this.date = date;
        this.isFile = isFile;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern(
                "MMM-dd, HH:mm");
        return String.format("[%s] %s: %s", date.format(formatter), authorName, content);
    }
}
