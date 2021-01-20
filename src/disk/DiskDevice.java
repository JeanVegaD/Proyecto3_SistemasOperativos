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
    private Block[] blocks;
    private int size;
    
    public DiskDevice (int size) {
        this.size = size;
    }
}
