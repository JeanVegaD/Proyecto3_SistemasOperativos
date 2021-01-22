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
        this.location = "";
    }

    public boolean addFile(String name, String type) {
        if (searchFile(name) == null) {
            File newF = new File(name, type);
            newF.setParentDirectory(this);
            newF.setPaths();
            this.contents.add(newF);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean addFile(String filename, String type, User owner, Group group) {
        if (searchFile(filename) == null) {
            File newF = new File(filename, type);
            newF.setOwner(owner);
            newF.setGroup(group);
            newF.setParentDirectory(this);
            newF.setPaths();
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
            dir.setPaths();
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
            dir.setPaths();
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
    
    public void deleteComponents(String regex) {
        this.contents.removeIf((comp) -> (comp.getFullname().matches(regex)));
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
    public void changeOwnerRecursive(User usr) {
        this.owner = usr;
        for(Component comp: this.contents) {
            comp.changeOwnerRecursive(usr);
        }
    }
    
    @Override
    public void changeGroupRecursive(Group group) {
        this.group = group;
        for(Component comp: this.contents) {
            comp.changeGroupRecursive(group);
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
