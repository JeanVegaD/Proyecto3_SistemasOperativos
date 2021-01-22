/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.directories;

import filesystem.users.Group;
import filesystem.users.User;
import java.util.ArrayList;
import p3_so.FS_File;

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
        this.location = "";
    }

    public boolean addFile(String name, String type) {
        if (searchFile(name) == null) {
            File newF = new File(name, type);
            newF.setParentDirectory(this);
            newF.setPaths();
            newF.generateID();
            this.contents.add(newF);
            return true;
        } else {
            return false;
        }
    }
    
    public File addFile(String filename, String type, User owner, Group group) {
        if (searchFile(filename) == null) {
            File newF = new File(filename, type);
            newF.setOwner(owner);
            newF.setGroup(group);
            newF.setParentDirectory(this);
            newF.setPaths();
            newF.generateID();
            this.contents.add(newF);
            return newF;
        } else {
            return null;
        }
    }

    public boolean addDirectory(String name) {
        if (searchDir(name) == null) {
            Directory dir = new Directory(name);
            dir.setParentDirectory(this);
            dir.setPaths();
            dir.generateID();
            this.contents.add(dir);
            return true;
        } else {
            return false;
        }
    }
    
    
    public Directory addDirectory(String name, User owner, Group group) {
        if (searchDir(name) == null) {
            Directory dir = new Directory(name);
            dir.setOwner(owner);
            dir.setGroup(group);
            dir.setParentDirectory(this);
            dir.setPaths();
            dir.generateID();
            this.contents.add(dir);
            return dir;
        } else {
            return null;
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
    
    public Directory searchDirPattern(String dirName) {
        for (Component comp : this.contents) {
            if (comp.kind == Directory.DIR) {
                if (comp.getFullname().matches(dirName)) {
                    return (Directory) comp;
                }
            }
        }
        return null;
    }

    public File searchFile(String fileName) {
        for (Component comp : this.contents) {
            if (comp.kind == Directory.FILE) {
                if (comp.name.equals(fileName) || fileName.equals(comp.name + "." + ((File) comp).getType())) {
                    return (File) comp;
                }
            }
        }
        return null;
    }
    
    public File searchFilePattern(String fileName) {
        for (Component comp : this.contents) {
            if (comp.kind == Directory.FILE) {
                if (comp.getFullname().matches(fileName)) {
                    return (File) comp;
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

    public int getFilesOpen() {
        int total = 0;
        for(Component comp: this.contents) {
            if (comp.kind == DIR) {
                total += ((Directory) comp).getFilesOpen();
            } else {
                if (((File) comp).isOPen()) {
                    total += 1;
                }
            }
        }
        return total;
    }

    public void deleteComp(Component comp) {
        this.contents.remove(comp);
    }
    
    public ArrayList<Component> deleteComponents(String regex) {
        ArrayList<Component> toRemove = new ArrayList<>();
        for(Component comp: this.contents) {
            if (comp.getFullname().matches(regex)) {
                toRemove.add(comp);
            }
        }
        this.contents.removeAll(toRemove);
        return toRemove;
    }

    public ArrayList<Component> deleteDirRecursive(Directory dir) {
        ArrayList<Component> toRemove = new ArrayList<>();
        toRemove.addAll(dir.contents);
        for(Component comp: this.contents) {
            if (comp.getKind() == DIR) {
                toRemove.addAll(this.deleteDirRecursive((Directory) comp));
            }
        }
        return toRemove;
    }
    
    @Override
    public void setPaths() {
        if (this.parent != null) {
            this.location = this.parent.myPath;
            this.myPath = this.parent.myPath + "/" + this.name;
            this.contents.forEach(comp -> {
                comp.setPaths();
            });
        } else {
            this.location = "";
            this.myPath = "";
        }
        
    }

    @Override
    public String getFullname() {
        return this.name;
    }

    @Override
    public void changeOwnerRecursive(User usr, FS_File disk) {
        this.owner = usr;
        for(Component comp: this.contents) {
            comp.changeOwnerRecursive(usr, disk);
            disk.changeComponentOwner(Integer.toString(this.getID()), usr.getUsername());
        }
    }
    
    @Override
    public void changeGroupRecursive(Group group, FS_File disk) {
        this.group = group;
        for(Component comp: this.contents) {
            comp.changeGroupRecursive(group, disk);
            disk.changeComponentGroup(Integer.toString(this.getID()), group.getName());
        }
    }

    public String searchFileRecursive(String filename) {
        String res = "";
        File file = this.searchFile(filename);
        if (file != null) {
            res = file.location + "\n";
        }
        for(Component comp: this.contents) {
            if (comp.getKind() == DIR) {
                String res2 = ((Directory) comp).searchFileRecursive(filename);
                if (!res2.equals("")) {
                    res += res2;
                }
            }
        }
        return res;
    }

}
