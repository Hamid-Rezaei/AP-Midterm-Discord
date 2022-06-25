package model;

public class Message {
    String authorName;
    String content;

    public Message(String content, String authorName) {
        this.content = content;
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String toString() {
        return String.format("[%s]: %s", authorName, content);
    }
}
