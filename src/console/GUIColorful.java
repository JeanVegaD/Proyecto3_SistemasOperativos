/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

/**
 *
 * @author Jean
 */

import console.FileSystemGUI;
import java.awt.AWTException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import p3_so.FS_File;


public class GUIColorful extends IGUI {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public GUIColorful(){
        introText();   
    }
    
    /**
     *
     */
    @Override
    public void initError(){
        
    }
    
    /*Prints in colors*/
    @Override
    public void printRed(String text){
        System.out.print(ANSI_RED + text + ANSI_RESET);
    }
    
    @Override
    public void printGreen(String text){
        System.out.print(ANSI_GREEN + text + ANSI_RESET);
    }
    
    @Override
    public void printYellow(String text){
        System.out.print(ANSI_YELLOW + text + ANSI_RESET);
    }
    
    @Override
    public void printPurple(String text){
        System.out.print(ANSI_PURPLE + text + ANSI_RESET);
    }
    
    @Override
    public void printBlue(String text){
        System.out.print(ANSI_BLUE + text + ANSI_RESET);
    }
    
}
