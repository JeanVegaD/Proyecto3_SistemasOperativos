/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import filesystem.users.Group;
import filesystem.users.User;
import java.util.ArrayList;

/**
 *
 * @author Luism
 */
public class UserManager {
    private ArrayList<User> users;
    private ArrayList<Group> groups;
    
    public UserManager(ArrayList<User> users, ArrayList<Group> groups) {
        this.users = users;
        this.groups = groups;
    }
    
    public User validateUser(String username, String password) {
        for(User user: users) {
            if (user.validateUser(username, password)) {
                return user;
            }
        }
        return null;
    }
    
    public User searchUser(String username) {
        for(User user: this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     *
     * @param groupname
     * @return
     */
    public Group searchGroup(String groupname) {
        for(Group group: this.groups) {
            if (group.getName().equals(groupname)) {
                return group;
            }
        }
        return null;
    }
    
    public void createRootUser(String password) {
        User rootUser = User.createRootUser(password);
        this.users.add(rootUser);
    }

    public void createRootGroup() {
        Group rootGroup = Group.createRootGroup();
        rootGroup.addUser(this.getRootUser());
        this.groups.add(rootGroup);
    }
    
    public User getRootUser() {
        return this.searchUser("root");
    }

    public boolean userExists(String username) {
        User usr = this.searchUser(username);
        return usr != null;
    }

    public boolean groupExists(String groupname) {
        return this.searchGroup(groupname) != null;
    }

    
}
