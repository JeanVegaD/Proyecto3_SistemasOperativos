/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import filesystem.directories.Directory;

/**
 *
 * @author Luism
 */
public class DirectoryManager {
    private Directory rootDir;
    private Directory currentDir;

    public DirectoryManager(Directory rootDir, Directory currentDir) {
        this.rootDir = rootDir;
        this.currentDir = currentDir;
    }
    
    public void setCurrentDir(Directory dir) {
        this.currentDir = dir;
    }

    public Directory getCurrentDir() {
        return this.currentDir;
    }
    
    public boolean fileExists(String fileName) {
        return this.currentDir.searchFile(fileName) != null;
    }
    
    public boolean directoryExists(String dirName) {
        return this.currentDir.searchDir(dirName) != null;
    }
}
