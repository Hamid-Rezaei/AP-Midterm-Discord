package model.guild;

public enum Permission {
    CAN_CREATE_CHANNEL(false),

    CAN_DELETE_CHANNEL(false),

    CAN_REMOVE_USER(false),

    CAN_STRICT_CHANNEL(false),

    CAN_CHANGE_SERVERNAME(false),

    CAN_SEE_HISTORY(true),

    CAN_PIN_MESSAGE(false),

    CAN_BAN_USER(false);

    public boolean isAble;

    private Permission(boolean isAble) {
        this.isAble = isAble;
    }

    public boolean isAble() {
        return isAble;
    }
}
