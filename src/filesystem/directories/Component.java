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
public abstract class Component {
    public static int DIR = 0;
    public static int FILE = 1;
    protected String name;
    protected Directory parent;
    protected String fullPath;
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
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getFullPath() {
        return this.fullPath;
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
    
    protected void setOwner(User owner) {
        this.owner = owner;
    }

    protected void setGroup(Group group) {
        this.group = group;
    }
}
