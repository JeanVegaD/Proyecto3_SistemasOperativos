/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.awt.AWTException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luism
 */
public class GUI extends IGUI {
    
    public GUI(){
        introText();   
    }

    @Override
    public void initError() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void printRed(String text){
        this.print(text);
    }
    
    @Override
    public void printGreen(String text){
        this.print(text);
    }
    
    @Override
    public void printYellow(String text){
        this.print(text);
    }
    
    @Override
    public void printPurple(String text){
        this.print(text);
    }
    
    @Override
    public void printBlue(String text){
        this.print(text);
    }
}
