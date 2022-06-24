package controller;

public enum ServerErrorType {
    NO_ERROR(1), USER_ALREADY_EXISTS(2), SERVER_CONNECTION_FAILED(3), DATABASE_ERROR(4), Duplicate_ERROR(5), UNKNOWN_ERROR(404);


    private final int code;

    public int getCode() {
        return code;
    }

    ServerErrorType(int code) {
        this.code = code;
    }
}
