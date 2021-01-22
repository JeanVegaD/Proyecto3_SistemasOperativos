/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.users;

/**
 *
 * @author Luism
 */
public class User {
    private String name;
    private String username;
    private String password;
    
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }
    
    public boolean validateUser(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    
    public static User createRootUser(String password) {
        return new User("root", "root", password);
    }

    public boolean isRoot() {
        return this.username.equals("root");
    }

}
