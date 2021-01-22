/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import disk.DiskDevice;
import filesystem.FileSystem;
import filesystem.NotEnoughPermissionException;
import filesystem.directories.Component;
import filesystem.directories.Directory;
import filesystem.directories.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import p3_so.FS_File;

/**
 *
 * @author Jean
 */
public class FileSystemGUI {

    private String fsName = "miFS";
    private String currentUser = "root";
    private FileSystem fs;

    public FileSystemGUI(int size, String rootPassword, FS_File diskFS) {
        this.fs = new FileSystem(diskFS);
        this.fs.format(size, rootPassword);
        //this.initTestValues(); // TEST
    }
    
    public FileSystemGUI(FS_File diskFS) {
        this.fs = new FileSystem(diskFS);
    }

    /*public void initTestValues() {
        this.fs.usseradd("Luis M", "luis", "luis1");
        this.fs.usseradd("Michelle A", "mich", "mich1");
        this.fs.usseradd("Jean V", "jean", "jean1");

        this.fs.groupadd("admin");
        this.fs.groupadd("test");

        this.fs.getCurrentDir().searchDir("home").searchDir("luis").addFile("myFile", "txt", this.fs.getUserManager().searchUser("luis"), this.fs.getUserManager().searchGroup("admin"));
        this.fs.getCurrentDir().searchDir("home").searchDir("luis").addFile("file1", "asm", this.fs.getUserManager().searchUser("luis"), this.fs.getUserManager().searchGroup("admin"));
        this.fs.getCurrentDir().searchDir("home").searchDir("luis").addFile("file2", "asm", this.fs.getUserManager().searchUser("luis"), this.fs.getUserManager().searchGroup("admin"));
        this.fs.getCurrentDir().searchDir("home").searchDir("luis").addFile("file3", "asm", this.fs.getUserManager().searchUser("luis"), this.fs.getUserManager().searchGroup("admin"));
        this.fs.getCurrentDir().searchDir("home").searchDir("luis").searchFile("myFile.txt").setContents("Hola, mi nombre es Luis");
    }
    */
    public String getFsName() {
        return fsName;
    }

    public String getCurrentUsername() {
        return this.fs.getCurrentUsername();
    }

    /*Commmands*/
    public JSONObject executeFormat(String[] parameters, IGUI self) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) { // Se debe indicar el tamaño del disco
            int size = Integer.parseInt(parameters[0]);
            if (size < DiskDevice.MIN_SIZE) {
                res.put("status", false);
                res.put("msg", "the minimum size is " + Integer.toString(DiskDevice.MIN_SIZE));
            } else {
                // Ask for root password
                self.print("password: ");
                String pass = self.getArgumentFromConsole();
                String[] passArguments = pass.split("\\s+");
                if (passArguments.length != 1 && !passArguments[0].equals("")) {
                    res.put("status", false);
                    res.put("msg", "invalid password");
                } else {
                    this.fs.format(size, passArguments[0]);
                    res.put("status", true);
                    res.put("msg", "ok");
                }
            }
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "format expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeExit(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 0) {
            res.put("status", true);
            res.put("msg", "ok");
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "exit expected 0 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeUserAdd(String[] parameters, IGUI self) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            if (!this.fs.getUserManager().userExists(parameters[0])) { //valida que el usuario no exista
                self.print("full name: ");
                String name = self.getArgumentFromConsole();
                if (name.isEmpty()) {
                    res.put("status", false);
                    res.put("msg", "invalid name");
                    return res;
                }

                self.print("password: ");
                String pass = self.getArgumentFromConsole();
                String[] passArguments = pass.split("\\s+");
                if (passArguments.length != 1 && !passArguments[0].equals("")) {
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                }

                self.print("confirm password: ");
                String pass2 = self.getArgumentFromConsole();
                String[] passArguments2 = pass2.split("\\s+");
                if (passArguments2.length != 1 && !passArguments[0].equals("")) {
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                }

                if (passArguments[0].equals(passArguments2[0])) {
                    this.fs.usseradd(name, parameters[0], passArguments[0]);
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", "passwords do not match");
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "this user already exists");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "usseradd expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeGroupAdd(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            if (this.fs.hasPermissionOfGroupsCreation(parameters[0])) { // valida que el usuario tenga permisos para crear grupos
                if (this.fs.getUserManager().groupExists(parameters[0])) { // valida que el grupo existe
                    res.put("status", false);
                    res.put("msg", "this group already exists");
                    return res;
                } else {
                    this.fs.groupadd(parameters[0]);
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "this user cannot create groups");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "groupadd expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executePasswd(String[] parameters, IGUI self) {
        JSONObject res = new JSONObject();
        switch (parameters.length) {
            case 1: // El usuario actual es el root y puede cambiar la contraseña de otro usuario
                if (!(this.fs.getCurrentUserIsRoot() && this.fs.getUserManager().userExists(parameters[0]))) {
                    res.put("status", false);
                    res.put("msg", "this user doesn't have permissions to change other users password");
                    return res;
                }
            case 0: // El usuario actual no es el root o lo es, pero va a cambiar su contraseña
                self.print("password: ");
                String pass = self.getArgumentFromConsole();
                String[] passArguments = pass.split("\\s+");
                if (passArguments.length != 1 && !passArguments[0].equals("")) {
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                }
                self.print("confirm password: ");
                String pass2 = self.getArgumentFromConsole();
                String[] passArguments2 = pass2.split("\\s+");
                if (passArguments2.length != 1 && !passArguments2[0].equals("")) {
                    res.put("status", false);
                    res.put("msg", "invalid password");
                    return res;
                }
                if (passArguments[0].equals(passArguments2[0])) {
                    this.fs.passwd(parameters.length == 0 ? this.fs.getCurrentUsername() : parameters[0], passArguments[0]);
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", "passwords do not match");
                    return res;
                }
            default:
                res.put("status", false);
                res.put("msg", "passwd expected 0 or 1 arguments but got " + parameters.length);
                return res;
        }
    }

    public JSONObject executeSu(String[] parameters, IGUI self) {
        JSONObject res = new JSONObject();
        boolean isRoot = false;
        if (parameters.length == 0 || parameters.length == 1) {
            if (parameters.length == 0) {
                isRoot = true;
            } else {
                if (!this.fs.getUserManager().userExists(parameters[0])) { // validar que el ususarioe exista
                    res.put("status", false);
                    res.put("msg", "this user does not exist");
                    return res;
                }
            }
            self.print("password: ");
            String pass = self.getArgumentFromConsole();
            String[] passArguments = pass.split("\\s+");
            if (passArguments.length != 1 && passArguments[0].isEmpty()) {
                res.put("status", false);
                res.put("msg", "invalid password");
                return res;
            }
            // validar password del usuario
            boolean validUser;
            if (isRoot) {
                validUser = this.fs.su(passArguments[0]);
            } else {
                validUser = this.fs.su(parameters[0], passArguments[0]);
            }
            if (validUser) {
                res.put("status", true);
                res.put("msg", "ok");
                return res;
            } else {
                res.put("status", false);
                res.put("msg", "passwords do not match");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "su expected 0 or 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeWhoami(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 0) {
            String info = this.fs.whoami();
            res.put("status", true);
            res.put("msg", info);
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "whoami expected 0 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executePwd(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 0) {
            // obtener la ruta actual
            String path = this.fs.getCurrentPath();
            if (path.isEmpty()) {
                path = "*root*";
            }
            res.put("status", true);
            res.put("msg", path);
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "pwd expected 0 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeMkdir(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 0) {
            res.put("status", false);
            res.put("msg", "mkdir expected 1 arguments but got " + parameters.length);
            return res;
        } else {
            try {
                String result = this.fs.mkdir(parameters);
                res.put("status", true);
                res.put("msg", result.equals("") ? "ok" : result);
                return res;
            } catch (NotEnoughPermissionException ex) {
                res.put("status", false);
                res.put("msg", ex.getMessage());
                return res;
            }
        }
    }

    public JSONObject executeRm(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            try {
                //se valida que exista y se pueda borrar
                if (this.fs.rm(parameters[0], false)) {
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", "invalid dirname or file ");
                    return res;
                }
            } catch (NotEnoughPermissionException ex) {
                res.put("status", false);
                res.put("msg", ex.getMessage());
                return res;
            } catch (Exception ex) {
                res.put("status", false);
                res.put("msg", ex.getMessage());
                return res;
            }
        } else if (parameters.length == 2) {
            if (parameters[0].equals("-R")) {
                try {
                    //se valida que exista y se pueda borrar
                    if (this.fs.rm(parameters[1], true)) {
                        res.put("status", true);
                        res.put("msg", "ok");
                        return res;
                    } else {
                        res.put("status", false);
                        res.put("msg", "invalid dirname or file ");
                        return res;
                    }
                } catch (NotEnoughPermissionException ex) {
                    res.put("status", false);
                    res.put("msg", ex.getMessage());
                    return res;
                } catch (Exception ex) {
                    res.put("status", false);
                    res.put("msg", ex.getMessage());
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "rm expected a flag in the first argument");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "rm expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeMv(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 2) {
            try {
                //se valida el primer directorio
                if (this.fs.mv(parameters[0], parameters[1])) {
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", "invalid dirname or filename");
                    return res;
                }
            } catch (Exception ex) {
                res.put("status", false);
                res.put("msg", ex.getMessage());
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "mv expected 2 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeLs(String[] parameters, IGUI self) {
        JSONObject res = new JSONObject();
        if (parameters.length == 0) {
            // se lista el directorio
            if (this.fs.getDirManager().getCurrentDir().hasPermissionToRead(this.fs.getCurrentUser())) {
                for (Component comp : this.fs.getDirManager().getCurrentDir().getContents()) {
                    self.print(comp.getComponentInfo() + " ");
                    if (comp.getKind() == Component.DIR) {
                        self.printBlue("-d " + comp.getName());
                    } else {
                        self.print("-f " + comp.getName()
                                + (((File) comp).getType().equals("FILE") ? "" : "." + ((File) comp).getType()));
                    }
                    self.print("\n");
                }
                res.put("status", true);
                res.put("msg", "ok");
                return res;
            } else {
                res.put("status", false);
                res.put("msg", String.format("%s has not permission to read at this directory", this.fs.getCurrentUser().getUsername()));
                return res;
            }
        } else if (parameters.length == 1) {
            if (parameters[0].equals("-R")) {
                if (this.fs.getDirManager().getCurrentDir().hasPermissionToRead(this.fs.getCurrentUser())) {
                    this.printDirectory(this.fs.getDirManager().getCurrentDir(), self);
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", String.format("%s has not permission to read at this directory", this.fs.getCurrentUser().getUsername()));
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "ls expected a flag in the first argument");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "ls expected 0 arguments but got " + parameters.length);
            return res;
        }
    }

    private void printDirectory(Directory dir, IGUI self) {
        ArrayList<Directory> pendDirs = new ArrayList<>();
        for (Component comp : dir.getContents()) {
            self.print(comp.getComponentInfo() + " ");
            if (comp.getKind() == Component.DIR) {
                self.printBlue("-d " + comp.getName());
                pendDirs.add((Directory) comp);
            } else {
                self.print("-f " + comp.getName()
                        + (((File) comp).getType().equals("FILE") ? "" : "." + ((File) comp).getType()));
            }
            self.print("\n");
        }
        if (pendDirs.size() > 0) {
            self.print("\n");
        }
        for (Directory dirP: pendDirs) {
            if (dirP.getContents().size() > 0) {
                self.print("." + dirP.getPath() + "\n");
                this.printDirectory(dirP, self);
            }
        }
    }

    public void executeClear(String[] parameters) {

    }

    public JSONObject executeCd(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            String result = this.fs.cd(parameters[0]);
            res.put("status", true);
            res.put("msg", result);
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "cd expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeWhereis(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            res.put("status", true);
            res.put("msg", this.fs.whereis(parameters[0]));
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "whereis expected 1 arguments but got " + parameters.length + "\n");
            return res;
        }
    }

    public JSONObject executeLn(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 2) {
            if (true) { //se valida el primer directorio y los 
                if (true) {// se validan los permisos sobre el archivo
                    if (true) { //se valida si el segundo directorio existe
                        res.put("status", true);
                        res.put("msg", "ok");
                        return res;
                    } else {// se cambia de nombre
                        res.put("status", true);
                        res.put("msg", "ok");
                        return res;
                    }
                } else {
                    res.put("status", false);
                    res.put("msg", "user can't do this action");
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "invalid dirname");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "ln expected 2 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeTouch(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            if (this.fs.isAFileString(parameters[0])) {
                try {
                    boolean result = this.fs.touch(parameters[0]);
                    if (result) { //se valida que no exista  el archivo
                        res.put("status", true);
                        res.put("msg", "file created");
                        return res;
                    } else {
                        res.put("status", false);
                        res.put("msg", "the file already exists");
                        return res;
                    }
                } catch (NotEnoughPermissionException ex) {
                    res.put("status", false);
                    res.put("msg", ex.getMessage());
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "invalid filename");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "touch expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeCat(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            File file = this.fs.getCurrentDir().searchFile(parameters[0]);
            if (file != null) { //se valida que el archivo exista
                try {
                    String contents = file.getContents(this.fs.getCurrentUser());
                    res.put("status", true);
                    res.put("msg", contents);
                } catch (NotEnoughPermissionException ex) {
                    res.put("status", false);
                    res.put("msg", ex.getMessage());
                }
                return res;
            } else {
                res.put("status", false);
                res.put("msg", "file does not exist");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "cat expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeChown(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 2) {
            if (this.fs.getUserManager().userExists(parameters[0])) {
                try {
                    //se valida el username
                    if (this.fs.chown(parameters[0], parameters[1], false)) { //se valida el driname or filename
                        res.put("status", true);
                        res.put("msg", "ok");
                        return res;
                    } else {
                        res.put("status", false);
                        res.put("msg", "invalid  driname or filename");
                        return res;
                    }
                } catch (Exception ex) {
                    res.put("status", false);
                    res.put("msg", ex.getMessage());
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "invalid  username");
                return res;
            }
        } else if (parameters.length == 3) {
            if (parameters[0].equals("-R")) {
                if (this.fs.getUserManager().userExists(parameters[0])) {
                    try {
                        //se valida el username
                        if (this.fs.chown(parameters[0], parameters[1], true)) { //se valida el driname or filename
                            res.put("status", true);
                            res.put("msg", "ok");
                            return res;
                        } else {
                            res.put("status", false);
                            res.put("msg", "invalid  driname or filename");
                            return res;
                        }
                    } catch (Exception ex) {
                        res.put("status", false);
                        res.put("msg", ex.getMessage());
                        return res;
                    }
                } else {
                    res.put("status", false);
                    res.put("msg", "invalid  username");
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "chown expected a flag in the first argument");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "chown expected 2 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeChgrp(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 2) {
            if (this.fs.getUserManager().groupExists(parameters[0])) {
                try {
                    //se valida el username
                    if (this.fs.chgrp(parameters[0], parameters[1], false)) { //se valida el driname or filename
                        res.put("status", true);
                        res.put("msg", "ok");
                        return res;
                    } else {
                        res.put("status", false);
                        res.put("msg", "invalid  driname or filename");
                        return res;
                    }
                } catch (Exception ex) {
                    res.put("status", false);
                    res.put("msg", ex.getMessage());
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "invalid  groupname");
                return res;
            }
        } else if (parameters.length == 3) {
            if (parameters[0].equals("-R")) {
                if (this.fs.getUserManager().groupExists(parameters[0])) {
                    try {
                        //se valida el username
                        if (this.fs.chgrp(parameters[0], parameters[1], true)) { //se valida el driname or filename
                            res.put("status", true);
                            res.put("msg", "ok");
                            return res;
                        } else {
                            res.put("status", false);
                            res.put("msg", "invalid  driname or filename");
                            return res;
                        }
                    } catch (Exception ex) {
                        res.put("status", false);
                        res.put("msg", ex.getMessage());
                        return res;
                    }
                } else {
                    res.put("status", false);
                    res.put("msg", "invalid  groupname");
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "chgrp expected a flag in the first argument");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "chgrp expected 2 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeChmod(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 2) {
            if (this.validatePermissionString(parameters[0])) { //se valida el numero
                if (this.fs.isAFileString(parameters[1])) {
                    try {
                        //se valida el filename
                        if (this.fs.chmod(parameters[0], parameters[1])) {
                            res.put("status", true);
                            res.put("msg", "ok");
                            return res;
                        } else {
                            res.put("status", false);
                            res.put("msg", "file does not exist");
                            return res;
                        }
                    } catch (Exception ex) {
                        res.put("status", false);
                        res.put("msg", ex.getMessage());
                        return res;
                    }
                } else {
                    res.put("status", false);
                    res.put("msg", "invalid filename");
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "invalid permission");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "chmod expected 2 arguments but got " + parameters.length);
            return res;
        }
    }

    private boolean validatePermissionString(String code) {
        return code.matches("^[0-7][0-7]$");
    }

    public JSONObject executeOpenFile(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            try {
                if (this.fs.openFile(parameters[0])) { // se valida archivo
                    res.put("status", true);
                    res.put("msg", "file opened");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", "file does not exists");
                    return res;
                }
            } catch (NotEnoughPermissionException ex) {
                res.put("status", false);
                res.put("msg", ex.getMessage());
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "openFile expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeCloseFile(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            try {
                if (this.fs.closeFile(parameters[0])) { // se valida archivo
                    res.put("status", true);
                    res.put("msg", "file closed");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", "invalid filename");
                    return res;
                }
            } catch (NotEnoughPermissionException ex) {
                res.put("status", false);
                res.put("msg", ex.getMessage());
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "closeFile expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeViewFilesOpen(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 0) {
            res.put("status", true);
            res.put("msg", this.fs.getFilesOpen());
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "viewFilesOpen expected 0 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeViewFCB(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            if (this.fs.getDirManager().fileExists(parameters[0])) { // se valida archivo
                try {
                    res.put("status", true);
                    res.put("msg", this.fs.viewFCB(parameters[0]));
                } catch (NotEnoughPermissionException ex) {
                    res.put("status", true);
                    res.put("msg", ex.getMessage());
                }
                return res;
            } else {
                res.put("status", false);
                res.put("msg", "file does not exists");
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "viewFCB expected 1 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeInfoFs(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 0) {
            res.put("status", true);
            res.put("msg", this.fs.infoFS());
            return res;
        } else {
            res.put("status", false);
            res.put("msg", "infoFS expected 0 arguments but got " + parameters.length);
            return res;
        }
    }

    public JSONObject executeNote(String[] parameters) {
        JSONObject res = new JSONObject();
        if (parameters.length == 1) {
            // Execute note
            if (this.fs.isAFileString(parameters[0])) {
                File file = this.fs.getCurrentDir().searchFile(fsName);
                if (file != null) {
                    // Start node
                    res.put("status", true);
                    res.put("msg", "ok");
                    return res;
                } else {
                    res.put("status", false);
                    res.put("msg", "file does not exists");
                    return res;
                }
            } else {
                res.put("status", false);
                res.put("msg", "note expected 1 argument but got " + parameters.length);
                return res;
            }
        } else {
            res.put("status", false);
            res.put("msg", "note expected 1 argument but got " + parameters.length);
            return res;
        }
    }

    String getCurrentPath() {
        return this.fs.getCurrentPath();
    }

}
