/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package disk;

/**
 *
 * @author Luism
 */
public class DiskDevice {

    public static int MIN_SIZE = 64;
    private Block[] blocks;
    private int size;
    private int usedSpace;
    
    public DiskDevice (int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getUsedSpace() {
        return usedSpace;
    }
    
    
}
