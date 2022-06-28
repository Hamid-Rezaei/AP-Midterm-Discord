package model.guild;

import java.io.Serializable;
import java.util.ArrayList;

public class Role implements Serializable {

    Permissions permissions;
    String roleName;

    public Role(String roleName) {
        this.permissions = new Permissions();
        this.roleName = roleName;
        if (roleName.equals("owner")){
            permissions.setCAN_STRICT_CHANNEL(true);
            permissions.setCAN_SEE_HISTORY(true);
            permissions.setCAN_REMOVE_USER(true);
            permissions.setCAN_BAN_USER(true);
            permissions.setCAN_CREATE_CHANNEL(true);
            permissions.setCAN_PIN_MESSAGE(true);
            permissions.setCAN_CHANGE_SERVERNAME(true);
            permissions.setCAN_DELETE_CHANNEL(true);
        }
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Permissions getPermissions() {
        return permissions;
    }
}
