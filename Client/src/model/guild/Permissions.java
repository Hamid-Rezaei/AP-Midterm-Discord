package model.guild;

public class Permissions {
    private boolean CAN_CREATE_CHANNEL = false;

    private boolean CAN_DELETE_CHANNEL = false;

    private boolean CAN_REMOVE_USER = false;

    private boolean CAN_STRICT_CHANNEL = false;

    private boolean CAN_CHANGE_SERVERNAME = false;

    private boolean CAN_SEE_HISTORY = true;

    private boolean CAN_PIN_MESSAGE = false;

    private boolean CAN_BAN_USER = false;

    private boolean CAN_ADD_USER = true;


    public void setCAN_CREATE_CHANNEL(boolean CAN_CREATE_CHANNEL) {
        this.CAN_CREATE_CHANNEL = CAN_CREATE_CHANNEL;
    }

    public void setCAN_DELETE_CHANNEL(boolean CAN_DELETE_CHANNEL) {
        this.CAN_DELETE_CHANNEL = CAN_DELETE_CHANNEL;
    }

    public void setCAN_REMOVE_USER(boolean CAN_REMOVE_USER) {
        this.CAN_REMOVE_USER = CAN_REMOVE_USER;
    }

    public void setCAN_STRICT_CHANNEL(boolean CAN_STRICT_CHANNEL) {
        this.CAN_STRICT_CHANNEL = CAN_STRICT_CHANNEL;
    }

    public void setCAN_CHANGE_SERVERNAME(boolean CAN_CHANGE_SERVERNAME) {
        this.CAN_CHANGE_SERVERNAME = CAN_CHANGE_SERVERNAME;
    }

    public void setCAN_SEE_HISTORY(boolean CAN_SEE_HISTORY) {
        this.CAN_SEE_HISTORY = CAN_SEE_HISTORY;
    }

    public void setCAN_PIN_MESSAGE(boolean CAN_PIN_MESSAGE) {
        this.CAN_PIN_MESSAGE = CAN_PIN_MESSAGE;
    }

    public void setCAN_BAN_USER(boolean CAN_BAN_USER) {
        this.CAN_BAN_USER = CAN_BAN_USER;
    }

    public void setCAN_ADD_USER(boolean CAN_ADD_USER) {
        this.CAN_ADD_USER = CAN_ADD_USER;
    }

    public boolean isCAN_ADD_USER() {
        return CAN_ADD_USER;
    }

    public boolean isCAN_CREATE_CHANNEL() {
        return CAN_CREATE_CHANNEL;
    }

    public boolean isCAN_DELETE_CHANNEL() {
        return CAN_DELETE_CHANNEL;
    }

    public boolean isCAN_REMOVE_USER() {
        return CAN_REMOVE_USER;
    }

    public boolean isCAN_STRICT_CHANNEL() {
        return CAN_STRICT_CHANNEL;
    }

    public boolean isCAN_CHANGE_SERVERNAME() {
        return CAN_CHANGE_SERVERNAME;
    }

    public boolean isCAN_SEE_HISTORY() {
        return CAN_SEE_HISTORY;
    }

    public boolean isCAN_PIN_MESSAGE() {
        return CAN_PIN_MESSAGE;
    }

    public boolean isCAN_BAN_USER() {
        return CAN_BAN_USER;
    }
}
