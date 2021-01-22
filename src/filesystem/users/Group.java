/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.users;

import java.util.ArrayList;

/**
 *
 * @author Luism
 */
public class Group {

    private String groupName;
    private ArrayList<User> users = new ArrayList<>();

    public Group(String groupName) {
        this.groupName = groupName;
    }
    
    public static String getRootGroupname() {
        return "sudo";
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public static Group createRootGroup() {
        return new Group("sudo");
    }

    public String getName() {
        return this.groupName;
    }

    public boolean isUserIn(User user) {
        for (User usr : this.users) {
            if (usr.equals(user)) {
                return true;
            }
        }
        return false;
    }
}
