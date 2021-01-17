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

import console.FileSystem;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;


public class GUI {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    private FileSystem FS;
    
    public GUI(){
        FS = new FileSystem();
        introText();   
    }
    
    
    /*Initializers*/
    public void loadWithFile(){
        
    }
    
    public void loadNewFile(){
        mainMenu();
    }
    
    public void initError(){
        
    }
    
    
    /*Prints in colors*/
    public void printRed(String text){
        System.out.print(ANSI_RED + text + ANSI_RESET);
    }
    
    public void printGreen(String text){
        System.out.print(ANSI_GREEN + text + ANSI_RESET);
    }
    
    public void printYellow(String text){
        System.out.print(ANSI_YELLOW + text + ANSI_RESET);
    }
    
    public void printPurple(String text){
        System.out.print(ANSI_PURPLE + text + ANSI_RESET);
    }
    
     public void printBlue(String text){
        System.out.print(ANSI_BLUE + text + ANSI_RESET);
    }
    
    public void print(String text){
        System.out.print(ANSI_RESET + text + ANSI_RESET);
    }
   
    private void printBreak(){
        System.out.println("");
    }
    
    public String getArgumentFromConsole(){
        Scanner input = new Scanner(System.in);
        String inputCommand = input.nextLine();

        return inputCommand;
    }
    
    
    private void introText(){
        printGreen("Welcome to the file system");
        printBreak();
    }
    
    
    private void mainMenu(){
        Scanner input = new Scanner(System.in);
        
        printPurple(FS.getCurrentUser());
        print("@");
        printBlue(FS.getCurrentPah());
        print(": ");
        String inputCommand = input.nextLine();
        parseCommand(inputCommand.split("\\s+"));
        
        mainMenu();
        
        
    }
    
    private void parseCommand(String[] commands){
        String command=commands[0];
        String[] parameters = Arrays.copyOfRange(commands, 1, commands.length);
        if(commands.length>0){
            switch(command){
                case "format" :
                    JSONObject res_format = FS.exctuteFormat(parameters);
                    boolean status_format = (boolean) (res_format.get("status"));
                    String msg_format  = (String) (res_format.get("msg"));
                    if(status_format){
                        printGreen(msg_format);
                        printBreak();
                    }else{
                        printRed(msg_format);
                        printBreak();
                    }
                    break; 
                case "exit" :
                    JSONObject res_exit = FS.exctuteExit(parameters);
                    boolean status_exit = (boolean) (res_exit.get("status"));
                    String msg_exit  = (String) (res_exit.get("msg"));
                    if(status_exit){
                        System.exit(0);
                    }else{
                        printRed(msg_exit);
                        printBreak();
                    }
                    break; 
                case "useradd" :
                    JSONObject res = FS.exctuteUserAdd(parameters, this);
                    boolean status = (boolean) (res.get("status"));
                    String msg  = (String) (res.get("msg"));
                    if(status){
                        printGreen(msg);
                        printBreak();
                    }else{
                        printRed(msg);
                        printBreak();
                    }
                    break; 
                case "groupadd" :
                    JSONObject res_groupadd = FS.exctuteGroupAdd(parameters);
                    boolean status__groupadd = (boolean) (res_groupadd.get("status"));
                    String msg__groupadd  = (String) (res_groupadd.get("msg"));
                    if(status__groupadd){
                        printGreen(msg__groupadd);
                        printBreak();
                    }else{
                        printRed(msg__groupadd);
                        printBreak();
                    }
                    break; 
                case "passwd" :
                    JSONObject res_passwd = FS.exctutePasswd(parameters, this);
                    boolean status__passwd = (boolean) (res_passwd.get("status"));
                    String msg__passwd  = (String) (res_passwd.get("msg"));
                    if(status__passwd){
                        printGreen(msg__passwd);
                        printBreak();
                    }else{
                        printRed(msg__passwd);
                        printBreak();
                    }
                    break; 
                case "su" :
                    JSONObject res_su = FS.exctuteSu(parameters, this);
                    boolean status_su = (boolean) (res_su.get("status"));
                    String msg_su  = (String) (res_su.get("msg"));
                    if(status_su){
                        printGreen(msg_su);
                        printBreak();
                    }else{
                        printRed(msg_su);
                        printBreak();
                    }
                    break; 
                case "whoami" :
                    JSONObject res_whoami = FS.exctuteWhoami(parameters);
                    boolean status_whoami = (boolean) (res_whoami.get("status"));
                    String msg_whoami  = (String) (res_whoami.get("msg"));
                    if(status_whoami){
                        print(msg_whoami);
                        printBreak();
                    }else{
                        printRed(msg_whoami);
                        printBreak();
                    }
                    break; 
                case "pwd" :
                    JSONObject res_pwd = FS.exctutePwd(parameters);
                    boolean status_pwd = (boolean) (res_pwd.get("status"));
                    String msg_pwd  = (String) (res_pwd.get("msg"));
                    if(status_pwd){
                        print(msg_pwd);
                        printBreak();
                    }else{
                        printRed(msg_pwd);
                        printBreak();
                    }
                    break; 
                case "mkdir" :
                    JSONObject res_mkdir = FS.exctuteMkdir(parameters);
                    boolean status_mkdir = (boolean) (res_mkdir.get("status"));
                    String msg_res_mkdir  = (String) (res_mkdir.get("msg"));
                    if(status_mkdir){
                        print(msg_res_mkdir);
                        printBreak();
                    }else{
                        printRed(msg_res_mkdir);
                        printBreak();
                    }
                    break; 
                case "rm" :
                    JSONObject res_rm = FS.exctuteRm(parameters);
                    boolean status_rm = (boolean) (res_rm.get("status"));
                    String msg_res_rm  = (String) (res_rm.get("msg"));
                    if(status_rm){
                        print(msg_res_rm);
                        printBreak();
                    }else{
                        printRed(msg_res_rm);
                        printBreak();
                    }
                    break;
                case "mv" :
                    JSONObject res_mv = FS.exctuteMv(parameters);
                    boolean status_mv = (boolean) (res_mv.get("status"));
                    String msg_res_mv = (String) (res_mv.get("msg"));
                    if(status_mv){
                        print(msg_res_mv);
                        printBreak();
                    }else{
                        printRed(msg_res_mv);
                        printBreak();
                    }
                    break; 
                case "ls" :
                    JSONObject res_ls = FS.exctuteLs(parameters);
                    boolean status_ls = (boolean) (res_ls.get("status"));
                    String msg_res_ls = (String) (res_ls.get("msg"));
                    if(status_ls){
                        print(msg_res_ls);
                        printBreak();
                    }else{
                        printRed(msg_res_ls);
                        printBreak();
                    }
                    break; 
                case "clear" :
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();     
                    break; 
 
                case "cd" :
                    JSONObject res_cd = FS.exctuteCd(parameters);
                    boolean status_cd = (boolean) (res_cd.get("status"));
                    String msg_res_cd = (String) (res_cd.get("msg"));
                    if(status_cd){
                        print(msg_res_cd);
                        printBreak();
                    }else{
                        printRed(msg_res_cd);
                        printBreak();
                    }
                    break; 
                case "whereis" :
                    JSONObject res_whereis = FS.exctuteWhereis(parameters);
                    boolean status_whereis = (boolean) (res_whereis.get("status"));
                    String msg_res_whereis = (String) (res_whereis.get("msg"));
                    if(status_whereis){
                        print(msg_res_whereis);
                        printBreak();
                    }else{
                        printRed(msg_res_whereis);
                        printBreak();
                    }
                    break; 
                case "ln" :
                    JSONObject res_ln = FS.exctuteLn(parameters);
                    boolean status_ln = (boolean) (res_ln.get("status"));
                    String msg_res_ln = (String) (res_ln.get("msg"));
                    if(status_ln){
                        print(msg_res_ln);
                        printBreak();
                    }else{
                        printRed(msg_res_ln);
                        printBreak();
                    }
                    break; 
                case "touch" :
                    JSONObject res_touch = FS.exctuteTouch(parameters);
                    boolean status_touch = (boolean) (res_touch.get("status"));
                    String msg_res_touch = (String) (res_touch.get("msg"));
                    if(status_touch){
                        print(msg_res_touch);
                        printBreak();
                    }else{
                        printRed(msg_res_touch);
                        printBreak();
                    }
                    break; 
                case "cat" :
                    JSONObject res_cat= FS.exctuteLn(parameters);
                    boolean status_cat = (boolean) (res_cat.get("status"));
                    String msg_res_cat = (String) (res_cat.get("msg"));
                    if(status_cat){
                        print(msg_res_cat);
                        printBreak();
                    }else{
                        printRed(msg_res_cat);
                        printBreak();
                    }
                    break; 
                case "chown" :
                    JSONObject res_chown= FS.exctuteChown(parameters);
                    boolean status_chown = (boolean) (res_chown.get("status"));
                    String msg_res_chown = (String) (res_chown.get("msg"));
                    if(status_chown){
                        print(msg_res_chown);
                        printBreak();
                    }else{
                        printRed(msg_res_chown);
                        printBreak();
                    }
                    break; 
                case "chgrp" :
                    JSONObject res_chgrp= FS.exctuteChgrp(parameters);
                    boolean status_chgrp = (boolean) (res_chgrp.get("status"));
                    String msg_res_chgrp = (String) (res_chgrp.get("msg"));
                    if(status_chgrp){
                        print(msg_res_chgrp);
                        printBreak();
                    }else{
                        printRed(msg_res_chgrp);
                        printBreak();
                    }
                    break; 
                case "chmod" :
                    JSONObject res_chmod= FS.exctuteChmod(parameters);
                    boolean status_chmod = (boolean) (res_chmod.get("status"));
                    String msg_res_chmod = (String) (res_chmod.get("msg"));
                    if(status_chmod){
                        print(msg_res_chmod);
                        printBreak();
                    }else{
                        printRed(msg_res_chmod);
                        printBreak();
                    }
                    break;
                case "openFile" :
                    JSONObject res_openFile= FS.exctuteOpenFile(parameters);
                    boolean status_openFile = (boolean) (res_openFile.get("status"));
                    String msg_res_openFile= (String) (res_openFile.get("msg"));
                    if(status_openFile){
                        print(msg_res_openFile);
                        printBreak();
                    }else{
                        printRed(msg_res_openFile);
                        printBreak();
                    }
                    break; 
                case "closeFile" :
                    JSONObject res_closeFile= FS.exctuteCloseFile(parameters);
                    boolean status_closeFile = (boolean) (res_closeFile.get("status"));
                    String msg_res_closeFile= (String) (res_closeFile.get("msg"));
                    if(status_closeFile){
                        print(msg_res_closeFile);
                        printBreak();
                    }else{
                        printRed(msg_res_closeFile);
                        printBreak();
                    }
                    break;
                case "viewFilesOpen" :
                    JSONObject res_viewFilesOpen= FS.exctuteViewFilesOpen(parameters);
                    boolean status_viewFilesOpen = (boolean) (res_viewFilesOpen.get("status"));
                    String msg_res_viewFilesOpen= (String) (res_viewFilesOpen.get("msg"));
                    if(status_viewFilesOpen){
                        print(msg_res_viewFilesOpen);
                        printBreak();
                    }else{
                        printRed(msg_res_viewFilesOpen);
                        printBreak();
                    }
                    break;
                case "viewFCB" :
                    JSONObject res_viewFCB= FS.exctuteViewFCB(parameters);
                    boolean status_viewFCB = (boolean) (res_viewFCB.get("status"));
                    String msg_res_viewFCB= (String) (res_viewFCB.get("msg"));
                    if(status_viewFCB){
                        print(msg_res_viewFCB);
                        printBreak();
                    }else{
                        printRed(msg_res_viewFCB);
                        printBreak();
                    }
                    break;
                case "infoFS" :
                    JSONObject res_infoFS= FS.exctuteInfoFs(parameters);
                    boolean status_infoFS = (boolean) (res_infoFS.get("status"));
                    String msg_res_infoFS= (String) (res_infoFS.get("msg"));
                    if(status_infoFS){
                        print(msg_res_infoFS);
                        printBreak();
                    }else{
                        printRed(msg_res_infoFS);
                        printBreak();
                    }
                    break;
                default:
                    printRed("Invalid command");
                    printBreak();
                    break;
                    
            }  
        }else{
            printRed("Invalid command");
            printBreak();
        }
    }
    
    
    
    
    
}
