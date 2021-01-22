/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import filesystem.directories.Directory;
import filesystem.users.Group;
import filesystem.users.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import p3_so.FS_File;

/**
 *
 * @author Luism
 */
public class FileSystemReader {
    private FileSystem fs;
    private FS_File disk;
    
    public FileSystemReader(FileSystem fs, FS_File disk) {
        this.fs = fs;
        this.disk = disk;
        this.readUsers();
        this.readGroups();
    }

    private void readUsers() {
        JSONArray usersArray = disk.getUsers();
        for(Object userOBJ: usersArray) {
            JSONObject userJSON = (JSONObject) userOBJ;
            String name = (String) userJSON.get("name");
            String username = (String) userJSON.get("username");
            String password = (String) userJSON.get("password");
            User user = new User(name, username, password);
            fs.addUser(user);
        }
    }
    
    private void readGroups() {
        JSONArray groupsArray = disk.getGroups();
        for(Object userOBJ: groupsArray) {
            JSONObject groupJSON = (JSONObject) userOBJ;
            String name = (String) groupJSON.get("name");
            Group group = new Group(name);
            fs.addGroup(group);
        }
    }
    
    public void readDirectories() {
        Directory rootDir = fs.getCurrentDir();
        JSONArray dirsArray = disk.getDir();
        for(Object dirOBJ: dirsArray) {
            JSONObject dirJSON = (JSONObject) dirOBJ;
            String location = (String) dirJSON.get("path");
            System.out.println(location);
        }
    }
}
