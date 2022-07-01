package model;

public enum Status {
    ONLINE("\033[0;32m"), IDLE("\033[0;33m"), DO_NOT_DISTURB("\033[0;31m"), INVISIBLE("\033[38;5;244m"), RESET("\033[0m");


    private final String color;

    Status(String color) {
        this.color = color;
    }

    public String toString(Status status) {
        if (status.equals(ONLINE)) {
            return "online";
        } else if (status.equals(IDLE)) {
            return "idle";
        } else if (status.equals(DO_NOT_DISTURB)) {
            return "do not disturb";
        } else {
            return "offline";
        }
    }

    @Override
    public String toString() {
        return color;
    }

    public static Status makeStatus(String status) {
        if (status.equals("online")) {
            return ONLINE;
        } else if (status.equals("idle")) {
            return IDLE;
        } else if (status.equals("do not disturb")) {
            return DO_NOT_DISTURB;
        } else {
            return INVISIBLE;
        }
    }
}
