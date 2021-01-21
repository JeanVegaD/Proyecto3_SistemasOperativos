/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import disk.DiskDevice;
import filesystem.allocationmethod.IAllocationMethod;
import filesystem.directories.Component;
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

    public FileSystem() {
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
        this.rootDir.setPaths();
        configRootDir();
        this.rootDir.addDirectory("home", this.currentUser, this.getUserManager().searchGroup("root"));
        this.rootDir.searchDir("home").addDirectory("root", this.currentUser, this.getUserManager().searchGroup("root"));
        this.currentDir = this.rootDir;
        this.dirManager = new DirectoryManager(this.rootDir, this.currentDir);
    }

    private void configRootDir() {
        this.rootDir.setOwner(this.currentUser);
        this.rootDir.setGroup(this.userManager.searchGroup("root"));
        this.rootDir.setPermissions("77");
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
        return this.currentDir.getPath();
    }

    public String mkdir(String[] dirnames) throws NotEnoughPermissionException {
        if (this.currentDir.hasPermissionToWrite(currentUser)) {
            String result = "";
            for (String str : dirnames) {
                if (!this.currentDir.addDirectory(str, this.currentUser, this.userManager.searchGroup("root"))) {
                    result += String.format("%s already exists\n", str);
                }
            }
            result = result.endsWith("\n") ? result.substring(0, result.length() - 2) : "";
            return result;
        } else {
            throw new NotEnoughPermissionException(this.currentUser.getUsername(), "write", NotEnoughPermissionException.DIR);
        }
    }

    /**
     *
     * @param fileName_DirName
     * @param recursive
     * @return
     * @throws filesystem.NotEnoughPermissionException
     */
    public boolean rm(String fileName_DirName, boolean recursive) throws NotEnoughPermissionException, Exception {
        if (this.currentDir.hasPermissionToRead(currentUser)) {
            this.currentDir.deleteComponents(fileName_DirName);
            return true;
        } else {
            throw new NotEnoughPermissionException(this.currentUser.getUsername(), "write", NotEnoughPermissionException.DIR);
        }
    }

    public boolean mv(String firstParam, String secondParam) throws Exception {
        if (this.currentDir.hasPermissionToRead(currentUser)) {
            Component src;
            if (!this.isAFileString(firstParam)) { // first is a dir
                if (!this.isAFileString(secondParam)) { // second is a dir then move dir
                    src = this.currentDir.searchDir(firstParam);
                    if (src != null) { // Si existe
                        Component dest = this.currentDir.searchDir(secondParam);
                        src.move(dest);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    throw new Exception("if first param is a directory then second must be a directory");
                }
            } else { // first is file
                src = this.currentDir.searchFile(firstParam);
                if (src != null) { // first existe
                    if (this.isAFileString(secondParam)) { // Second is a file
                        // Renombrar
                        ((File) src).setName(secondParam);
                    } else { // Second is a dir
                        // Mover
                        Directory dest = this.currentDir.searchDir(secondParam);
                        if (dest != null) { // existe
                            src.move(dest);
                            return true;
                        } else { // no existe
                            return false;
                        }
                    }
                    return true;
                } else { // first no existe
                    return false;
                }
            }
        } else {
            throw new NotEnoughPermissionException(this.currentUser.getUsername(), "write", NotEnoughPermissionException.DIR);
        }
    }

    public boolean isAFileString(String str) {
        return str.matches("^[\\w,\\s-]+\\.[A-Za-z]{3}$");
    }

    public String cd(String dirpath) {
        String[] dirParts = dirpath.split("/");
        Directory actualDir = this.currentDir;
        for (String dir : dirParts) {
            if (dir.equals("..")) {
                actualDir = actualDir.getParentDirectory();
                if (actualDir == null) {
                    return "you are at the root directiry";
                }
            } else {
                if (!dir.equals(".") && !dir.equals("")) {
                    actualDir = actualDir.searchDir(dir);
                    if (actualDir == null) {
                        return "this directory does not exist";
                    }
                }
            }
        }
        this.currentDir = actualDir;
        this.dirManager.setCurrentDir(this.currentDir);
        return "";
    }
    
    public String whereis(String filename) {
        if (this.isAFileString(filename)) {
            String res = this.currentDir.searchFileRecursive(filename);
            if (res.equals("")) {
                return "file not found\n";
            } else {
                return res;
            }
        } else {
            return "incorrect filename\n";
        }
    }

    public boolean touch(String filename) throws NotEnoughPermissionException {
        if (this.dirManager.fileExists(filename)) {
            return false;
        } else {
            if (this.currentDir.hasPermissionToWrite(currentUser)) {
                String[] fileParts = this.getFileParts(filename);
                this.currentDir.addFile(fileParts[0], fileParts[1], this.currentUser, this.userManager.searchGroup("root"));
                return true;
            } else {
                throw new NotEnoughPermissionException(this.currentUser.getUsername(), "write", NotEnoughPermissionException.DIR);
            }
        }
    }

    public boolean chown(String ownername, String fileOrPathName, boolean recursive) throws Exception {
        Component comp;
        if (this.isAFileString(fileOrPathName)) { // Es un archivo
            comp = this.currentDir.searchFile(fileOrPathName);
        } else { // Es un directorio
            comp = this.currentDir.searchDir(fileOrPathName);
        }
        if (comp != null) {
            User usr = this.userManager.searchUser(ownername);
            if (usr != null) { // user exists
                if (comp.getOwner().getUsername().equals(currentUser.getUsername()) || this.currentUser.isRoot()) {
                    if (recursive) {
                        comp.changeOwnerRecursive(usr);
                    } else {
                        comp.changeOwner(usr);
                    }
                    return true;
                } else {
                    throw new Exception(String.format("%s cannot change the owner of this file", this.currentUser.getUsername()));
                }
            } else {
                return false;
            }
        } else { // File or dir don't exist
            return false;
        }
    }

    public boolean chgrp(String groupname, String fileOrPathName, boolean recursive) throws Exception {
        Component comp;
        if (this.isAFileString(fileOrPathName)) { // Es un archivo
            comp = this.currentDir.searchFile(fileOrPathName);
        } else { // Es un directorio
            comp = this.currentDir.searchDir(fileOrPathName);
        }
        if (comp != null) {
            Group group = this.userManager.searchGroup(groupname);
            if (group != null) { // user exists
                if (comp.getOwner().getUsername().equals(currentUser.getUsername()) || this.currentUser.isRoot()) {
                    if (recursive) {
                        comp.changeGroupRecursive(group);
                    } else {
                        comp.changeGroup(group);
                    }
                    return true;
                } else {
                    throw new Exception(String.format("%s cannot change the group of this file", this.currentUser.getUsername()));
                }
            } else {
                return false;
            }
        } else { // File or dir don't exist
            return false;
        }
    }

    public boolean chmod(String permissionsCode, String fileOrDirName) throws Exception {
        Component comp;
        if (this.isAFileString(fileOrDirName)) { // es un archivo
            comp = this.currentDir.searchFile(fileOrDirName);
        } else { // es un directorio
            comp = this.currentDir.searchDir(fileOrDirName);
        }
        if (comp != null) {
            if (comp.getOwner().getUsername().equals(currentUser.getUsername()) || this.currentUser.isRoot()) {
                comp.setPermissions(permissionsCode);
                return true;
            } else {
                throw new Exception(String.format("%s cannot change the permissions of this file", this.currentUser.getUsername()));
            }
        } else {
            return false;
        }
    }

    public boolean openFile(String parameter) throws NotEnoughPermissionException {
        File file = this.currentDir.searchFile(parameter);
        if (file != null) {
            if (file.hasPermissionToRead(currentUser)) {
                file.open();
            } else {
                throw new NotEnoughPermissionException(this.currentUser.getUsername(), "read", NotEnoughPermissionException.FILE);
            }
            return true;
        }
        return false;
    }

    public boolean closeFile(String parameter) throws NotEnoughPermissionException {
        File file = this.currentDir.searchFile(parameter);
        if (file != null) {
            if (file.hasPermissionToRead(currentUser)) {
                file.close();
            } else {
                throw new NotEnoughPermissionException(this.currentUser.getUsername(), "read", NotEnoughPermissionException.FILE);
            }
            return true;
        }
        return false;
    }

    public String getFilesOpen() {
        return String.format("Files open: %d", this.rootDir.getFilesOpen());
    }

    public String viewFCB(String filename) throws NotEnoughPermissionException {
        File file = this.currentDir.searchFile(filename);
        if (file != null) {
            if (file.hasPermissionToRead(currentUser)) {
                return file.getFileInfo();
            } else {
                throw new NotEnoughPermissionException(this.currentUser.getUsername(), "read", NotEnoughPermissionException.FILE);
            }
        } else {
            return "";
        }
    }

    public String infoFS() {
        String info = "";
        info += "Filesystem name: miFS\n";
        info += "Size: " + this.readSize(this.diskDevice.getSize()) + "\n";
        info += "Used space: " + this.readSize(this.diskDevice.getUsedSpace()) + "\n";
        info += "Free space: " + this.readSize(this.diskDevice.getSize() - this.diskDevice.getUsedSpace());
        return info;
    }

    public Directory getCurrentDir() {
        return this.dirManager.getCurrentDir();
    }

    public String getCurrentUsername() {
        return this.currentUser.getUsername();
    }

    public String getCurrentPath() {
        return this.currentDir.getPath();
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

    public User getCurrentUser() {
        return this.currentUser;
    }

    private String[] getFileParts(String parameter) {
        String[] result = new String[]{"", ""};
        if (parameter.contains(".")) {
            String[] parts = parameter.split("\\.");
            result[1] = parts[parts.length - 1];
            for (int i = 0; i < parts.length - 1; i++) {
                result[0] += parts[i];
            }
        } else {
            result[0] = parameter;
            result[1] = "FILE";
        }
        return result;
    }

    private String readSize(int size) {
        if (size < 1024) {
            return Integer.toString(size) + "KB";
        } else if (size >= 1024 && size < 1048576) {
            return Integer.toString((int) Math.round(size / 1024.0)) + "MB";
        } else {
            return Integer.toString((int) Math.round((size / 1024.0) / 1024)) + "GB";
        }
    }

}
