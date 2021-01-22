/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

/**
 *
 * @author Luism
 */
public class NotEnoughPermissionException extends Exception {
    public static int DIR = 0;
    public static int FILE = 1;
    private String username;
    private String kind;
    private int dirOrFile;

    public NotEnoughPermissionException(String username, String kind, int dirOrFile) {
        this.username = username;
        this.kind = kind;
        this.dirOrFile = dirOrFile;
    }

    public NotEnoughPermissionException(String username, String kind) {
        this.username = username;
        this.kind = kind;
    }
    
    public String getUsername() {
        return username;
    }

    public String getDirOrFile() {
        return this.dirOrFile == DIR ? "directory" : "file";
    }
    
    @Override
    public String getMessage() {
        return String.format("%s has not %s permission at this %s", this.username, this.kind, this.getDirOrFile());
    }
}
