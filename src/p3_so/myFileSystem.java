/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_so;

import console.GUIColorful;

/**
 *
 * @author Jean
 */
public class myFileSystem {
    
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int argsCont = args.length;
        GUIColorful console = new GUIColorful();
        switch (argsCont) {
            case 0 -> console.loadNewFile();
            case 1 -> console.loadWithFile(args[0]);
            default -> console.initError();
        }
    }
    
}
