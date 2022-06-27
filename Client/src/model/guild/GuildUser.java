package model.guild;




import model.User;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class GuildUser extends User implements Serializable {

    private Role role;

    public GuildUser(User user, Role role) {
        super(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getToken(), user.getAvatar());

    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
