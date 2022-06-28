package model;

public class Reaction {
    String emoji;

    public void setLikeReact(){
        emoji = "like";
    }

    public void setDisLikeReact(){
        emoji = "dislike";
    }
    public void setSmileReact(){
        emoji = "smile";
    }


    public String getEmoji() {
        return emoji;
    }
}
