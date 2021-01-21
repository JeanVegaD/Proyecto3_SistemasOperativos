/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Scanner;
import org.json.simple.JSONObject;

/**
 *
 * @author Luism
 */
public abstract class IGUI {
    protected FileSystemGUI FS;
    
    /*Initializers*/
    public abstract void loadWithFile();
    
    public abstract void loadNewFile();
    
    public abstract void initError();
    
    public abstract void printRed(String text);
    
    public abstract void printGreen(String text);
    
    public abstract void printYellow(String text);
    
    public abstract void printPurple(String text);
    
    public abstract void printBlue(String text);
    
    public void printBreak(){
        System.out.println("");
    }
    
    public void introText(){
        print("Welcome to the file system");
        printBreak();
    }
    
    public void mainMenu() throws AWTException {
        Scanner input = new Scanner(System.in);
        printPurple(FS.getCurrentUsername());
        print("@");
        printBlue(FS.getFsName());
        print(":");
        printBlue("~" + FS.getCurrentPath());
        print("$ ");
        String inputCommand = input.nextLine();
        parseCommand(inputCommand.split("\\s+"));
        mainMenu();
    }
    
    public String getArgumentFromConsole() {
        Scanner input = new Scanner(System.in);
        String inputCommand = input.nextLine();
        return inputCommand;
    }
    
    public void print(String text) {
        System.out.print(text);
    }
    
    private void parseCommand(String[] commands) throws AWTException{
        String command=commands[0];
        String[] parameters = Arrays.copyOfRange(commands, 1, commands.length);
        if(commands.length>0){
            switch(command){
                case "format" :
                    JSONObject res_format = FS.executeFormat(parameters, this);
                    boolean status_format = (boolean) (res_format.get("status"));
                    String msg_format  = (String) (res_format.get("msg"));
                    if(status_format){
                        print(msg_format);
                        printBreak();
                    }else{
                        print(msg_format);
                        printBreak();
                    }
                    break; 
                case "exit" :
                    JSONObject res_exit = FS.executeExit(parameters);
                    boolean status_exit = (boolean) (res_exit.get("status"));
                    String msg_exit  = (String) (res_exit.get("msg"));
                    if(status_exit){
                        System.exit(0);
                    }else{
                        print(msg_exit);
                        printBreak();
                    }
                    break; 
                case "useradd" :
                    JSONObject res = FS.executeUserAdd(parameters, this);
                    boolean status = (boolean) (res.get("status"));
                    String msg  = (String) (res.get("msg"));
                    if(status){
                        print(msg);
                        printBreak();
                    }else{
                        print(msg);
                        printBreak();
                    }
                    break; 
                case "groupadd" :
                    JSONObject res_groupadd = FS.executeGroupAdd(parameters);
                    boolean status__groupadd = (boolean) (res_groupadd.get("status"));
                    String msg__groupadd  = (String) (res_groupadd.get("msg"));
                    if(status__groupadd){
                        print(msg__groupadd);
                        printBreak();
                    }else{
                        print(msg__groupadd);
                        printBreak();
                    }
                    break; 
                case "passwd" :
                    JSONObject res_passwd = FS.executePasswd(parameters, this);
                    boolean status__passwd = (boolean) (res_passwd.get("status"));
                    String msg__passwd  = (String) (res_passwd.get("msg"));
                    if(status__passwd){
                        print(msg__passwd);
                        printBreak();
                    }else{
                        print(msg__passwd);
                        printBreak();
                    }
                    break; 
                case "su" :
                    JSONObject res_su = FS.executeSu(parameters, this);
                    boolean status_su = (boolean) (res_su.get("status"));
                    String msg_su  = (String) (res_su.get("msg"));
                    if(status_su){
                        print(msg_su);
                        printBreak();
                    }else{
                        print(msg_su);
                        printBreak();
                    }
                    break; 
                case "whoami" :
                    JSONObject res_whoami = FS.executeWhoami(parameters);
                    boolean status_whoami = (boolean) (res_whoami.get("status"));
                    String msg_whoami  = (String) (res_whoami.get("msg"));
                    if(status_whoami){
                        print(msg_whoami);
                        printBreak();
                    }else{
                        print(msg_whoami);
                        printBreak();
                    }
                    break; 
                case "pwd" :
                    JSONObject res_pwd = FS.executePwd(parameters);
                    boolean status_pwd = (boolean) (res_pwd.get("status"));
                    String msg_pwd  = (String) (res_pwd.get("msg"));
                    if(status_pwd){
                        print(msg_pwd);
                        printBreak();
                    }else{
                        print(msg_pwd);
                        printBreak();
                    }
                    break; 
                case "mkdir" :
                    JSONObject res_mkdir = FS.executeMkdir(parameters);
                    boolean status_mkdir = (boolean) (res_mkdir.get("status"));
                    String msg_res_mkdir  = (String) (res_mkdir.get("msg"));
                    if(status_mkdir){
                        print(msg_res_mkdir);
                        printBreak();
                    }else{
                        print(msg_res_mkdir);
                        printBreak();
                    }
                    break; 
                case "rm" :
                    JSONObject res_rm = FS.executeRm(parameters);
                    boolean status_rm = (boolean) (res_rm.get("status"));
                    String msg_res_rm  = (String) (res_rm.get("msg"));
                    if(status_rm){
                        if(!msg_res_rm.equals("ok") && !msg_res_rm.equals("")) {
                            print(msg_res_rm);
                            printBreak();
                        }
                    }else{
                        print(msg_res_rm);
                        printBreak();
                    }
                    break;
                case "mv" :
                    JSONObject res_mv = FS.executeMv(parameters);
                    boolean status_mv = (boolean) (res_mv.get("status"));
                    String msg_res_mv = (String) (res_mv.get("msg"));
                    if(status_mv){
                        print(msg_res_mv);
                        printBreak();
                    }else{
                        print(msg_res_mv);
                        printBreak();
                    }
                    break; 
                case "ls" :
                    JSONObject res_ls = FS.executeLs(parameters, this);
                    boolean status_ls = (boolean) (res_ls.get("status"));
                    String msg_res_ls = (String) (res_ls.get("msg"));
                    if(status_ls){
                        //print(msg_res_ls);
                        //printBreak();
                    }else{
                        print(msg_res_ls);
                        printBreak();
                    }
                    break; 
                case "clear" :
                    clearConsole();     
                    break; 
 
                case "cd" :
                    JSONObject res_cd = FS.executeCd(parameters);
                    boolean status_cd = (boolean) (res_cd.get("status"));
                    String msg_res_cd = (String) (res_cd.get("msg"));
                    if(status_cd){
                        if(!msg_res_cd.equals("")) {
                            print(msg_res_cd);
                            printBreak();
                        }
                    }else{
                        print(msg_res_cd);
                        printBreak();
                    }
                    break; 
                case "whereis" :
                    JSONObject res_whereis = FS.executeWhereis(parameters);
                    boolean status_whereis = (boolean) (res_whereis.get("status"));
                    String msg_res_whereis = (String) (res_whereis.get("msg"));
                    if(status_whereis){
                        print(msg_res_whereis);
                    }else{
                        print(msg_res_whereis);
                    }
                    break; 
                case "ln" :
                    JSONObject res_ln = FS.executeLn(parameters);
                    boolean status_ln = (boolean) (res_ln.get("status"));
                    String msg_res_ln = (String) (res_ln.get("msg"));
                    if(status_ln){
                        print(msg_res_ln);
                        printBreak();
                    }else{
                        print(msg_res_ln);
                        printBreak();
                    }
                    break; 
                case "touch" :
                    JSONObject res_touch = FS.executeTouch(parameters);
                    boolean status_touch = (boolean) (res_touch.get("status"));
                    String msg_res_touch = (String) (res_touch.get("msg"));
                    if(status_touch){
                        print(msg_res_touch);
                        printBreak();
                    }else{
                        print(msg_res_touch);
                        printBreak();
                    }
                    break; 
                case "cat" :
                    JSONObject res_cat= FS.executeCat(parameters);
                    boolean status_cat = (boolean) (res_cat.get("status"));
                    String msg_res_cat = (String) (res_cat.get("msg"));
                    if(status_cat){
                        print(msg_res_cat);
                        printBreak();
                    }else{
                        print(msg_res_cat);
                        printBreak();
                    }
                    break; 
                case "chown" :
                    JSONObject res_chown= FS.executeChown(parameters);
                    boolean status_chown = (boolean) (res_chown.get("status"));
                    String msg_res_chown = (String) (res_chown.get("msg"));
                    if(status_chown){
                        print(msg_res_chown);
                        printBreak();
                    }else{
                        print(msg_res_chown);
                        printBreak();
                    }
                    break; 
                case "chgrp" :
                    JSONObject res_chgrp= FS.executeChgrp(parameters);
                    boolean status_chgrp = (boolean) (res_chgrp.get("status"));
                    String msg_res_chgrp = (String) (res_chgrp.get("msg"));
                    if(status_chgrp){
                        print(msg_res_chgrp);
                        printBreak();
                    }else{
                        print(msg_res_chgrp);
                        printBreak();
                    }
                    break; 
                case "chmod" :
                    JSONObject res_chmod= FS.executeChmod(parameters);
                    boolean status_chmod = (boolean) (res_chmod.get("status"));
                    String msg_res_chmod = (String) (res_chmod.get("msg"));
                    if(status_chmod){
                        print(msg_res_chmod);
                        printBreak();
                    }else{
                        print(msg_res_chmod);
                        printBreak();
                    }
                    break;
                case "openFile" :
                    JSONObject res_openFile= FS.executeOpenFile(parameters);
                    boolean status_openFile = (boolean) (res_openFile.get("status"));
                    String msg_res_openFile= (String) (res_openFile.get("msg"));
                    if(status_openFile){
                        print(msg_res_openFile);
                        printBreak();
                    }else{
                        print(msg_res_openFile);
                        printBreak();
                    }
                    break; 
                case "closeFile" :
                    JSONObject res_closeFile= FS.executeCloseFile(parameters);
                    boolean status_closeFile = (boolean) (res_closeFile.get("status"));
                    String msg_res_closeFile= (String) (res_closeFile.get("msg"));
                    if(status_closeFile){
                        print(msg_res_closeFile);
                        printBreak();
                    }else{
                        print(msg_res_closeFile);
                        printBreak();
                    }
                    break;
                case "viewFilesOpen" :
                    JSONObject res_viewFilesOpen= FS.executeViewFilesOpen(parameters);
                    boolean status_viewFilesOpen = (boolean) (res_viewFilesOpen.get("status"));
                    String msg_res_viewFilesOpen= (String) (res_viewFilesOpen.get("msg"));
                    if(status_viewFilesOpen){
                        print(msg_res_viewFilesOpen);
                        printBreak();
                    }else{
                        print(msg_res_viewFilesOpen);
                        printBreak();
                    }
                    break;
                case "viewFCB" :
                    JSONObject res_viewFCB= FS.executeViewFCB(parameters);
                    boolean status_viewFCB = (boolean) (res_viewFCB.get("status"));
                    String msg_res_viewFCB= (String) (res_viewFCB.get("msg"));
                    if(status_viewFCB){
                        print(msg_res_viewFCB);
                        printBreak();
                    }else{
                        print(msg_res_viewFCB);
                        printBreak();
                    }
                    break;
                case "infoFS" :
                    JSONObject res_infoFS= FS.executeInfoFs(parameters);
                    boolean status_infoFS = (boolean) (res_infoFS.get("status"));
                    String msg_res_infoFS= (String) (res_infoFS.get("msg"));
                    if(status_infoFS){
                        print(msg_res_infoFS);
                        printBreak();
                    }else{
                        print(msg_res_infoFS);
                        printBreak();
                    }
                    break;
                case "note":
                    JSONObject res_note= FS.executeNote(parameters);
                    boolean status_note = (boolean) (res_note.get("status"));
                    String msg_res_note= (String) (res_note.get("msg"));
                    if(status_note){
                    }else{
                        print(msg_res_note);
                        printBreak();
                    }
                    break;
                default:
                    print("Invalid command");
                    printBreak();
                    break;
                    
            }  
        }else{
            print("Invalid command");
            printBreak();
        }
    }
    
    public void clearConsole() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);robot.keyPress(KeyEvent.VK_L);
        robot.keyRelease(KeyEvent.VK_CONTROL);robot.keyRelease(KeyEvent.VK_L);
    }
}
