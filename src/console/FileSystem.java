/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;
import org.json.simple.JSONObject;

/**
 *
 * @author Jean
 */
public class FileSystem {
    
    private String currentPah = "miFS";
    private String currentUser = "root";
    
    public FileSystem(){
        
    }
    
    public String getCurrentPah() {
        return currentPah;
    }

    public String getCurrentUser() {
        return currentUser;
    }
    
    
    
    /*Commmands*/
    public JSONObject exctuteFormat(String[] parameters){
        JSONObject res = new JSONObject();
       if(parameters.length==0){

           res.put("status", true);
           res.put("msg", "ok");
           return res;
       }else {
          res.put("status", false);
            res.put("msg", "format expected 0 arguments but got " + parameters.length);
            return res;
       }
    }
    
    
    
    public JSONObject exctuteExit(String[] parameters){
       JSONObject res = new JSONObject();
       if(parameters.length==0){

           res.put("status", true);
           res.put("msg", "ok");
           return res;
       }else {
          res.put("status", false);
            res.put("msg", "exit expected 0 arguments but got " + parameters.length);
            return res;
       }
    }
    
    public JSONObject exctuteUserAdd(String[] parameters,GUI self){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ //validar que el usuario no exista
                
                self.print("name: ");
                String name = self.getArgumentFromConsole();
                if(name== ""){
                    res.put("status", false);
                    res.put("msg", "invalid name");
                    return res;
                }
                
                self.print("password: ");
                String pass = self.getArgumentFromConsole();
                String[] passArguments  = pass.split("\\s+");
                if(passArguments.length!=1 &&  passArguments[0]==""){
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                    
                }
                
                self.print("confirm password: ");
                String pass2 = self.getArgumentFromConsole();
                String[] passArguments2  = pass2.split("\\s+");
                if(passArguments2.length!=1 &&  passArguments2[0]==""){
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                    
                }
                
                if(passArguments[0].equals(passArguments2[0])){
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                }else{
                    res.put("status", false);
                    res.put("msg", "passwords do not match");
                    return res;
                }
            }else{
                res.put("status", false);
                res.put("msg", "This user already exists");
                return res;
            }
        }else{
            res.put("status", false);
            res.put("msg", "usseradd expected 1 arguments but got " + parameters.length);
            return res;
        }  
    }
    
    public JSONObject exctuteGroupAdd(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
             if(true){ //validar que el usuario tenga permisos para crear grupos
                 if(true){
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;  
                 }else{
                     res.put("status", false);
                    res.put("msg", "This user already exists");
                    return res;
                 }
             }else{
                    res.put("status", false);
                    res.put("msg", "this user cannot create groups");
                    return res;
             }
        }else{
            res.put("status", false);
            res.put("msg", "groupadd expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctutePasswd(String[] parameters,GUI self){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ //validar que el ususarioe exista
                

                self.print("password: ");
                String pass = self.getArgumentFromConsole();
                String[] passArguments  = pass.split("\\s+");
                if(passArguments.length!=1 &&  passArguments[0]==""){
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                    
                }
                
                self.print("confirm password: ");
                String pass2 = self.getArgumentFromConsole();
                String[] passArguments2  = pass2.split("\\s+");
                if(passArguments2.length!=1 &&  passArguments2[0]==""){
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                }
                
                if(passArguments[0].equals(passArguments2[0])){
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                }else{
                    res.put("status", false);
                    res.put("msg", "passwords do not match");
                    return res;
                }
            }else{
                res.put("status", false);
                res.put("msg", "this user does not exist");
                return res;
            }
        }else{
            res.put("status", false);
            res.put("msg", "passwd expected 1 arguments but got " + parameters.length);
            return res;
        }  
    }
    
    public JSONObject exctuteSu(String[] parameters,GUI self){
       JSONObject res = new JSONObject();
       
       if(parameters.length==0){
           //cambio de usuario a root
           res.put("status", true);
           res.put("msg", "ok");
           return res;
       }else if(parameters.length==1){
            if(true){ //validar que el ususarioe exista
                self.print("password: ");
                String pass = self.getArgumentFromConsole();
                String[] passArguments  = pass.split("\\s+");
                if(passArguments.length!=1 &&  passArguments[0]==""){
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;   
                }
                
                //validar password del usuario
                if(true ){
                    //cambio al usuario indicado
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                }else{
                    res.put("status", false);
                    res.put("msg", "passwords do not match");
                    return res;
                }
            }else{
                res.put("status", false);
                res.put("msg", "this user does not exist");
                return res;
            }
        }else{
            res.put("status", false);
            res.put("msg", "su expected 0 or 1 arguments but got " + parameters.length);
            return res;
        }   
    }
    
    public JSONObject exctuteWhoami(String[] parameters){
       JSONObject res = new JSONObject();
       if(parameters.length==0){
           //obtener el nombre y el usuario 
           String name= "name: "  + "\n";
           String username= "username: " ;
           
           res.put("status", true);
           res.put("msg", name+ username);
           return res;
       }else {
          res.put("status", false);
            res.put("msg", "whoami expected 0 arguments but got " + parameters.length);
            return res;
       }
    }
    
    public JSONObject exctutePwd(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==0){
           //obtener la ruta actual
           String path= "/root/";
           
           res.put("status", true);
           res.put("msg",path);
           return res;
        }else {
          res.put("status", false);
            res.put("msg", "pwd expected 0 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteMkdir(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
           //se valida el directorio
           if(true){
                res.put("status", true);
                res.put("msg","ok");
                return res;
           }else{
               res.put("status", false);
                res.put("msg", "invalid dirname");
                return res;
           } 
        }else {
            res.put("status", false);
            res.put("msg", "mkdir expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteRm(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
           //se valida que exista y se pueda borrar
           if(true){
                res.put("status", true);
                res.put("msg","ok");
                return res;
           }else{
               res.put("status", false);
                res.put("msg", "invalid dirname or file ");
                return res;
           }
        }else if(parameters.length==2){
            if(parameters[0].equals("-R")){
                //se valida que exista y se pueda borrar
                if(true){
                     res.put("status", true);
                     res.put("msg","ok");
                     return res;
                }else{
                    res.put("status", false);
                     res.put("msg", "invalid dirname or file ");
                     return res;
                }
            }else{
                res.put("status", false);
                res.put("msg", "rm expected a flag in the first argument");
                return res;
            }     
        }else {
            res.put("status", false);
            res.put("msg", "rm expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteMv(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==2){
           //se valida el primer directorio
           if(true){
               if(true){ //se valida si el segundo directorio existe
                    res.put("status", true);
                    res.put("msg","ok");
                    return res;
               }else{// se cambia de nombre
                    res.put("status", true);
                    res.put("msg","ok");
                    return res;
               }
           }else{
                res.put("status", false);
                res.put("msg", "invalid dirname");
                return res;
           } 
        }else {
            res.put("status", false);
            res.put("msg", "mv expected 2 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteLs(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==0){
           //se lista el directorio
           
            res.put("status", true);
            res.put("msg","ok");
            return res;
           
        }else if(parameters.length==1){
            if(parameters[0].equals("-R")){
                //se lista el directorio y sus carpetas
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
                res.put("status", false);
                res.put("msg", "ls expected a flag in the first argument");
                return res;
            }   
        }
        else {
            res.put("status", false);
            res.put("msg", "ls expected 0 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public void exctuteClear(String[] parameters){
        
    }
    
    public JSONObject exctuteCd(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(parameters[0].equals("..")){
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
               if(true){ //se valida el directorio
                    res.put("status", true);
                    res.put("msg","ok");
                    return res;
               }else{
                    res.put("status", false);
                    res.put("msg", "invalid dirname");
                    return res;
               }
            }           
        }else {
            res.put("status", false);
            res.put("msg", "cd expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteWhereis(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ // se valida archivo
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
                res.put("status", false);
                res.put("msg", "invalid filename");
                return res;
            }           
        }else {
            res.put("status", false);
            res.put("msg", "whereis expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteLn(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==2){
           if(true){ //se valida el primer directorio y los 
               if(true){// se validan los permisos sobre el archivo
                   if(true){ //se valida si el segundo directorio existe
                        res.put("status", true);
                        res.put("msg","ok");
                        return res;
                   }else{// se cambia de nombre
                        res.put("status", true);
                        res.put("msg","ok");
                        return res;
                   }
               }else{
                   res.put("status", false);
                    res.put("msg", "user can't do this action");
                    return res;
               }
           }else{
                res.put("status", false);
                res.put("msg", "invalid dirname");
                return res;
           } 
        }else {
            res.put("status", false);
            res.put("msg", "ln expected 2 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteTouch(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ //se valida que no exista  el archivo
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
                res.put("status", false);
                res.put("msg", "the file already exists");
                return res;
            }           
        }else {
            res.put("status", false);
            res.put("msg", "touch expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteCat(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ //se valida que el archivo exista
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
                res.put("status", false);
                res.put("msg", "invalid filename");
                return res;
            }           
        }else {
            res.put("status", false);
            res.put("msg", "touch expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteChown(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==2){
           if(true){ //se valida el username
               if(true){ //se valida el driname or filename
                   res.put("status", true);
                    res.put("msg","ok");
                    return res;
               }else{
                    res.put("status", false);
                res.put("msg", "invalid  driname or filename");
                return res;
               }
           }else{
                res.put("status", false);
                res.put("msg", "invalid  username");
                return res;
           }
        }else if(parameters.length==3){
            if(parameters[0].equals("-R")){
                if(true){ //se valida el ussename
                     if(true){ //se valida el driname or filename
                        res.put("status", true);
                         res.put("msg","ok");
                         return res;
                    }else{
                         res.put("status", false);
                     res.put("msg", "invalid  driname or filename");
                     return res;
                    }
                }else{
                    res.put("status", false);
                    res.put("msg", "invalid  username");
                    return res;
                }
            }else{
                res.put("status", false);
                res.put("msg", "chown expected a flag in the first argument");
                return res;
            }     
        }else {
            res.put("status", false);
            res.put("msg", "chown expected 2 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteChgrp(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==2){
           if(true){ //se valida el gruopname
               if(true){ //se valida el driname or filename
                   res.put("status", true);
                    res.put("msg","ok");
                    return res;
               }else{
                    res.put("status", false);
                res.put("msg", "invalid  driname or filename");
                return res;
               }
           }else{
                res.put("status", false);
                res.put("msg", "invalid  username");
                return res;
           }
        }else if(parameters.length==3){
            if(parameters[0].equals("-R")){
                if(true){ //se valida el gruopname
                     if(true){ //se valida el driname or filename
                        res.put("status", true);
                         res.put("msg","ok");
                         return res;
                    }else{
                         res.put("status", false);
                     res.put("msg", "invalid  driname or filename");
                     return res;
                    }
                }else{
                    res.put("status", false);
                    res.put("msg", "invalid  username");
                    return res;
                }
            }else{
                res.put("status", false);
                res.put("msg", "chgrp expected a flag in the first argument");
                return res;
            }     
        }else {
            res.put("status", false);
            res.put("msg", "chgrp expected 2 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteChmod(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==2){
            if(true){ //se valida el numero
                if(true){ //se valida el filename
                    res.put("status", true);
                    res.put("msg","ok");
                    return res;
               }else{
                    res.put("status", false);
                    res.put("msg", "invalid filename");
                    return res;
               }
            }else{
                res.put("status", false);
                res.put("msg", "invalid permission");
                return res;
            }           
        }else {
            res.put("status", false);
            res.put("msg", "chmod expected 2 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteOpenFile(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ // se valida archivo
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
                res.put("status", false);
                res.put("msg", "invalid filename");
                return res;
            }           
        }else {
            res.put("status", false);
            res.put("msg", "openFile expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteCloseFile(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ // se valida archivo
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
                res.put("status", false);
                res.put("msg", "invalid filename");
                return res;
            }           
        }else {
            res.put("status", false);
            res.put("msg", "closeFile expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteViewFilesOpen(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==0){
            res.put("status", true);
            res.put("msg","ok");
            return res;         
        }else {
            res.put("status", false);
            res.put("msg", "viewFilesOpen expected 0 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteViewFCB(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==1){
            if(true){ // se valida archivo
                res.put("status", true);
                res.put("msg","ok");
                return res;
            }else{
                res.put("status", false);
                res.put("msg", "invalid filename");
                return res;
            }           
        }else {
            res.put("status", false);
            res.put("msg", "viewFCB expected 1 arguments but got " + parameters.length);
            return res;
        }
    }
    
    public JSONObject exctuteInfoFs(String[] parameters){
        JSONObject res = new JSONObject();
        if(parameters.length==0){
            res.put("status", true);
            res.put("msg","ok");
            return res;         
        }else {
            res.put("status", false);
            res.put("msg", "infoFS expected 0 arguments but got " + parameters.length);
            return res;
        }
    }
    
    
    
}
