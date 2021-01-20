/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.directories;

import filesystem.users.Group;
import filesystem.users.User;
import java.util.ArrayList;

/**
 *
 * @author Luism
 */
public class Directory extends Component {
    
    private ArrayList<Component> contents = new ArrayList<>();

    public Directory(String name) {
        super(name, Component.DIR);
    }

    public Directory() {
        super("", Component.DIR);
        this.fullPath = "";
    }

    public boolean addFile(String name) {
        if (searchFile(name) == null) {
            File newF = new File(name);
            newF.setParentDirectory(this);
            newF.fullPath = this.fullPath + "/" + newF.name;
            this.contents.add(newF);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean addFile(String filename, User currentUser, Group searchGroup) {
        if (searchFile(name) == null) {
            File newF = new File(name);
            newF.setOwner(owner);
            newF.setGroup(group);
            newF.setParentDirectory(this);
            newF.fullPath = this.fullPath + "/" + newF.name;
            this.contents.add(newF);
            return true;
        } else {
            return false;
        }
    }

    public boolean addDirectory(String name) {
        if (searchDir(name) == null) {
            Directory dir = new Directory(name);
            dir.setParentDirectory(this);
            dir.fullPath = this.fullPath + "/" + dir.name;
            this.contents.add(dir);
            return true;
        } else {
            return false;
        }
    }
    
    
    public boolean addDirectory(String name, User owner, Group group) {
        if (searchDir(name) == null) {
            Directory dir = new Directory(name);
            dir.setOwner(owner);
            dir.setGroup(group);
            dir.setParentDirectory(this);
            dir.fullPath = this.fullPath + "/" + dir.name;
            this.contents.add(dir);
            return true;
        } else {
            return false;
        }
    }

    public Directory searchDir(String dirName) {
        for (Component comp : this.contents) {
            if (comp.kind == Directory.DIR) {
                if (comp.name.equals(dirName)) {
                    return (Directory) comp;
                }
            }
        }
        return null;
    }

    public Directory searchFile(String fileName) {
        for (Component comp : this.contents) {
            if (comp.kind == Directory.FILE) {
                if (comp.name.equals(fileName)) {
                    return (Directory) comp;
                }
            }
        }
        return null;
    }

    @Override
    public String getComponentInfo() {
        return String.format("%s %s %s", 
                Integer.toString(this.ownerPermissions.getPermissionsCode()) + Integer.toString(this.groupPermissions.getPermissionsCode()),
                this.owner != null ? this.owner.getUsername() : "null",
                this.group != null ? this.group.getName() : "null");
    }

    public ArrayList<Component> getContents() {
        return this.contents;
    }



}
