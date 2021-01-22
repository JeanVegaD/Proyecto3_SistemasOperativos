/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.directories;

import filesystem.NotEnoughPermissionException;
import filesystem.users.Group;
import filesystem.users.User;
import p3_so.FS_File;

/**
 *
 * @author Luism
 */
public class File extends Component {
    private String type;
    private String contents = "";
    // TODO: manejar permisos (propietario, grupo)
    private boolean isOPen;
    
    public File(String name) {
        super(name, Component.FILE);
        this.isOPen = false;
    }

    File(String name, String type) {
        super(name, Component.FILE);
        this.isOPen = false;
        this.type = type;
    }

    @Override
    public String getComponentInfo() {
        return String.format("%s %s %s", 
                Integer.toString(this.ownerPermissions.getPermissionsCode()) + Integer.toString(this.groupPermissions.getPermissionsCode()),
                this.owner != null ? this.owner.getUsername(): "null",
                this.group != null ? this.group.getName() : "null");
    }
    
    public String getFileInfo() {
        String info = "";
        info += "name: " + this.name + (this.type.equals("FILE") ? "" : "." + this.type) + "\n";
        info += "owner: " + this.owner.getUsername() + "\n";
        info += "creation date: " + this.getCreationDate() + "\n";
        info += "status: " + (isOPen() ? "open" : "closed") + "\n";
        info += "size: " + this.size + "\n";
        info += "location: " + this.location;
        return info;
    }
    
    public String getContents() {
        return this.contents;
    }
    
    public String getContents(User user) throws NotEnoughPermissionException {
        if (this.hasPermissionToRead(user)) {
            return this.contents;
        } else {
            throw new NotEnoughPermissionException(user.getUsername(), "read", NotEnoughPermissionException.FILE);
        }
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    
    public void setContents(String contents, User user) throws Exception {
        if (this.hasPermissionToWrite(user)) {
            this.contents = contents;
        } else {
            throw new Exception(String.format("%s has no write permission", user.getUsername()));
        }
    }

    public String getType() {
        return this.type;
    }

    public boolean isOPen() {
        return isOPen;
    }
    
    public void open() {
        this.isOPen = true;
    }
    
    public void close() {
        this.isOPen = false;
    }
    
    @Override
    public void setPaths() {
        if (this.parent != null) {
            this.location = this.parent.myPath;
            this.myPath = this.parent.myPath + "/" + this.name + "." + this.type;
        } else {
            this.location = "";
            this.myPath = "";
        }
    }

    public void setName(String name) {
        String[] parts = name.split("\\.");
        if (parts.length == 2) {
            this.name = parts[0];
            this.type = parts[1];
        }
    }

    @Override
    public String getFullname() {
        return this.name + "." + this.type;
    }

    @Override
    public void changeOwnerRecursive(User usr, FS_File disk) {
        this.changeOwner(usr);
    }
    
    @Override
    public void changeGroupRecursive(Group group, FS_File disk) {
        this.changeGroup(group);
    }

    public String getSize() {
        return Integer.toString(this.size);
    }
}
