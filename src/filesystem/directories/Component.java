/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.directories;

import filesystem.users.Group;
import filesystem.users.User;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Luism
 */
public abstract class Component {
    public static int DIR = 0;
    public static int FILE = 1;
    protected String name;
    protected int size = 0;
    protected Date creationDate;
    protected Directory parent;
    protected String myPath;
    protected String location;
    protected int kind;
    protected User owner;
    protected Group group;
    protected PermissionDetails ownerPermissions;
    protected PermissionDetails groupPermissions;
    
    public Component(String name, int kind) {
        this.name = name;
        this.kind = kind;
        this.ownerPermissions = new PermissionDetails(7);
        this.groupPermissions = new PermissionDetails(0);
        this.setCreationDate();
    }
    
    private void setCreationDate() {
        Calendar calendario;
        calendario = Calendar.getInstance();
        this.creationDate = calendario.getTime();
    }

    public String getCreationDate(){		
        SimpleDateFormat mascara = new SimpleDateFormat( "dd/MM/yy" );
        return mascara.format(this.creationDate);
    }

    
    public String getName() {
        return this.name;
    }
    
    public void setParentDirectory(Directory parent) {
        this.parent = parent;
    }
    
    public Directory getParentDirectory() {
        return this.parent;
    }
    
    public abstract String getComponentInfo();
    
    //public abstract boolean save();

    public int getKind() {
        return this.kind;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    public boolean hasPermissionToRead(User user) {
        if (user.isRoot()) {
            return true;
        } else if (this.owner.equals(user)) {
            return this.ownerPermissions.hasReadPermission();
        } else if (this.group.isUserIn(user)) {
            return this.groupPermissions.hasReadPermission();
        } else {
            return false;
        }
    }
    
    public boolean hasPermissionToWrite(User user) {
        if (user.isRoot()) {
            return true;
        } else if (this.owner.equals(user)) {
            return this.ownerPermissions.hasWritePermission();
        } else if (this.group.isUserIn(user)) {
            return this.groupPermissions.hasWritePermission();
        } else {
            return false;
        }
    }
    
    public void setPermissions(String code) {
        this.ownerPermissions = new PermissionDetails(Integer.parseInt(Character.toString(code.charAt(0))));
        this.groupPermissions = new PermissionDetails(Integer.parseInt(Character.toString(code.charAt(1))));
    }

    public void move(Component dest) {
        this.parent.deleteComp(this);
        this.parent = (Directory) dest;
        this.setPaths();
    }
    
    public String getPath() {
        return this.myPath;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public abstract String getFullname();

    public abstract void setPaths();

    public User getOwner() {
        return this.owner;
    }

    public void changeOwner(User usr) {
        this.owner = usr;
    }

    public void changeGroup(Group group) {
        this.group = group;
    }

    public abstract void changeOwnerRecursive(User usr);
    
    public abstract void changeGroupRecursive(Group group);
}
