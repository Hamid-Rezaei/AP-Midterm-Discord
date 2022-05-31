package model.user;

public enum Status {
    ONLINE("\033[0;32m"), IDLE("\033[0;33m"), DO_NOT_DISTURB("\033[0;31m"), INVISIBLE("\033[38;5;244m ");


    private final String color;

    Status(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
