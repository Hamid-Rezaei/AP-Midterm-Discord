package model;

import java.io.Serializable;

public class Message implements Serializable {
    String authorName;
    String content;

    public Message(String content, String authorName) {
        this.content = content;
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String toString() {
        return String.format("[%s]: %s", authorName, content);
    }
}
