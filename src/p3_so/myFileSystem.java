/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_so;

import console.GUI;
import java.io.Console;

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
        GUI console = new GUI();
        if(argsCont==0){
            console.loadNewFile();
        }else if(argsCont==1){
            console.loadWithFile();
        }else{
            console.initError();
        }
    }
    
}
