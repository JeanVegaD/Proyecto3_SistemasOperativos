/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import disk.DiskDevice;
import filesystem.allocationmethod.IAllocationMethod;
import filesystem.directories.Directory;
import filesystem.directories.File;
import filesystem.users.Group;
import filesystem.users.User;
import java.util.ArrayList;

/**
 *
 * @author Luism
 */
public class FileSystem {
    private DiskDevice diskDevice;
    private IAllocationMethod allocationMethod;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();
    private Directory rootDir;
    
    private User currentUser;
    private Directory currentDir;
    private UserManager userManager;
    private DirectoryManager dirManager;
    
    public FileSystem () {
        userManager = new UserManager(users, groups);
    }
    
    public void format(int size, String rootPassword) {
        this.diskDevice = new DiskDevice(size);
        this.users.clear();
        this.groups.clear();
        this.userManager.createRootUser(rootPassword);
        this.userManager.createRootGroup();
        this.currentUser = this.userManager.getRootUser();
        this.rootDir = new Directory();
        this.rootDir.addDirectory("home", this.currentUser, this.getUserManager().searchGroup("root"));
        this.rootDir.searchDir("home").addDirectory("root", this.currentUser, this.getUserManager().searchGroup("root"));
        this.currentDir = this.rootDir;
        this.dirManager = new DirectoryManager(this.rootDir, this.currentDir);
    }
    
    public void usseradd(String name, String username, String password) {
        User newUser = new User(name, username, password);
        this.users.add(newUser);
        this.rootDir.searchDir("home").addDirectory(username, this.userManager.searchUser(username), this.getUserManager().searchGroup("root"));
        // TODO: llamar método para la representación en el archivo.
    }
    
    public void groupadd(String groupName) {
        Group newGroup = new Group(groupName);
        this.groups.add(newGroup);
    }
    
    public void passwd(String username, String newPassword) {
        User user = this.userManager.searchUser(username);
        user.changePassword(newPassword);
    }
    
    public void passwd(User user, String newPassword) {
        user.changePassword(newPassword);
    }
    
    public boolean su(String password) {
        return this.su("root", password);
    }
    
    public boolean su(String username, String password) {
        User user = this.userManager.validateUser(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        } else {
            return false;
        }
    }
    
    public String whoami() {
        return String.format("username: %s\nfullname: %s", this.currentUser.getUsername(), this.currentUser.getName());
    }
    
    public String pwd() {
        return this.currentDir.getFullPath();
    }
    
    public String mkdir(String[] dirnames) {
        String result = "";
        for (String str: dirnames) {
            if(!this.currentDir.addDirectory(str)) {
                result += String.format("%s already exists\n", str);
            }
        }
        result = result.endsWith("\n") ? result.substring(0, result.length() - 2) : "";
        return result;
    }
    
    /**
     * 
     * @param fileName_DirName
     */
    public void rm(String fileName_DirName) {
        
    }
    
    public void mv(String filename, String fileOrDirectory) {
        
    }
    
    public String cd(String dirpath) {
        String[] dirParts = dirpath.split("/");
        Directory actualDir = this.currentDir;
        for (String dir: dirParts) {
            if (dir.equals("..")) {
                actualDir = actualDir.getParentDirectory();
                if (actualDir == null) {
                    return "you are at the root directiry";
                }
            } else {
                actualDir = actualDir.searchDir(dir);
                if (actualDir == null) {
                    return "this directory does not exists or the path is incorrect";
                }
            }
        }
        this.currentDir = actualDir;
        this.dirManager.setCurrentDir(this.currentDir);
        return "";
    }
    
    public boolean touch(String filename) {
        if (this.dirManager.fileExists(filename)) {
            return false;
        } else {
            this.currentDir.addFile(filename, this.currentUser, this.userManager.searchGroup("root"));
            return true;
        }
    }
    
    public Directory getCurrentDir() {
        return this.dirManager.getCurrentDir();
    }

    public String getCurrentUsername() {
        return this.currentUser.getUsername();
    }

    public String getCurrentPath() {
        return this.currentDir.getFullPath();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public DirectoryManager getDirManager() {
        return dirManager;
    }
    
    public boolean hasPermissionOfGroupsCreation(String parameter) {
        return this.currentUser.getUsername().equals("root");
    }

    public boolean getCurrentUserIsRoot() {
        return this.currentUser.getUsername().equals("root");
    }
}
