package model;

import java.io.Serializable;

public class Reaction implements Serializable {
    String emoji;

    public void setLikeReact() {
        emoji = "like";
    }

    public void setDisLikeReact() {
        emoji = "dislike";
    }

    public void setSmileReact() {
        emoji = "smile";
    }


    public String getEmoji() {
        return emoji;
    }
}
