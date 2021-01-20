/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.directories;

import filesystem.users.Group;
import filesystem.users.User;

/**
 *
 * @author Luism
 */
public class File extends Component {
    private String extension;
    
    // TODO: manejar permisos (propietario, grupo)
    
    public File(String name) {
        super(name, Component.FILE);
    }

    @Override
    public String getComponentInfo() {
        return String.format("%s %s %s", 
                Integer.toString(this.ownerPermissions.getPermissionsCode()) + Integer.toString(this.groupPermissions.getPermissionsCode()),
                this.owner != null ? this.owner.getUsername(): "null",
                this.group != null ? this.group.getName() : "null");
    }
}
