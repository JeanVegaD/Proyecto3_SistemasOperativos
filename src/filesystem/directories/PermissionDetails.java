/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.directories;

/**
 *
 * @author Luism
 */
public class PermissionDetails {
    public static int READ = 4;
    public static int WRITE = 2;
    public static int EXECUTE = 1;
    public static int NONE = 0;
    
    public int read = 0;
    public int write = 0;
    public int execute = 0;
    
    public PermissionDetails() {   
    }
    
    public PermissionDetails(int permissions) {
        switch(permissions) {
            case 7:
                this.enableReading();
                this.enableWriting();
                this.enableExecuting();
                break;
            case 6:
                this.disableAll();
                this.enableReading();
                this.enableWriting();
                break;
            case 5:
                this.disableAll();
                this.enableReading();
                this.enableExecuting();
                break;
            case 4:
                this.disableAll();
                this.enableReading();
                break;
            case 3:
                this.disableAll();
                this.enableWriting();
                this.enableExecuting();
                break;
            case 2:
                this.disableAll();
                this.enableWriting();
                break;
            case 1:
                this.disableAll();
                this.enableExecuting();
                break;
            case 0:
                this.disableAll();
                break;
        }
    }
    
    private void enableReading() {
        this.read = READ;
    }
    
    private void enableWriting() {
        this.write = WRITE;
    }
    
    private void enableExecuting() {
        this.execute = EXECUTE;
    }
    
    public void disableReading() {
        this.read = 0;
    }
    
    public void disableWriting() {
        this.write = 0;
    }
    
    public void disableExecuting() {
        this.execute = 0;
    }
    
    private void disableAll() {
        this.read = 0;
        this.write = 0;
        this.execute = 0;
    }
    
    public int getPermissionsCode() {
        return this.write + this.read + this.execute;
    }
    
    public boolean hasReadPermission() {
        return this.read == READ;
    }
    
    public boolean hasWritePermission() {
        return this.write == WRITE;
    }
    
    public boolean hasExecutePermission() {
        return this.execute == EXECUTE;
    }
}
