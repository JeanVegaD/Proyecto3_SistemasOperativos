/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_so;

import java.io.FileNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.Iterator;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Jean
 */
public class FS_File {
    String path; 
    JSONArray fsStructure  = new JSONArray();
    int size;
    
    public FS_File(String path){
        this.path=path;
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader(this.path))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            fsStructure= (JSONArray) obj;
            //System.out.println(fsStructure);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public FS_File(int sizeOfDisk){
        //format file
        this.size = sizeOfDisk;
        this.path="miDiscoDuro.fs";
        int sizeInKb = sizeOfDisk;

        for(int i=0; i<= sizeInKb ; i+=64){
            JSONObject tempJSON = new JSONObject();
            fsStructure.add(tempJSON);
        }
        writeInFile();
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void writeInFile(){
        //Write JSON file
        try (FileWriter file = new FileWriter(this.path)) {
            file.write(fsStructure.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addUser(String name, String username, String password){
        boolean flag = true;
        
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("users")){
                    String size = (String) tempJSON.get("size");
                    int sizeint = Integer.parseInt(size);
                    if(sizeint+10 <=64){
                        JSONArray arrayUsers  = (JSONArray)tempJSON.get("users");
                        
                        JSONObject tempuser = new JSONObject();
                        tempuser.put("name", name);
                        tempuser.put("username", username);
                        tempuser.put("password", password);
                        
                        arrayUsers.add(tempuser);
                                             
                        tempJSON.replace("size",Integer.toString(sizeint+10));
                        
                        flag =false;
                        break;
                    }
                }
            }
        }
        
        if(flag){
            for (Object obj : fsStructure) {
                JSONObject tempJSON = (JSONObject) obj;
                if(tempJSON.isEmpty()){

                    tempJSON.put("kind", "users");
                    tempJSON.put("size", "14");

                    JSONArray arrayUsers  = new JSONArray();

                    JSONObject tempuser = new JSONObject();
                    tempuser.put("name", name);
                    tempuser.put("username", username);
                    tempuser.put("password", password);

                    arrayUsers.add(tempuser);

                     tempJSON.put("users", arrayUsers);
                     break;

                }
            }
        }
        //System.out.println(fsStructure);
         writeInFile();
        
    }
    
    public void addGroup(String name){
        boolean flag = true;
        
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("groups")){
                    String size = (String) tempJSON.get("size");
                    int sizeint = Integer.parseInt(size);
                    if(sizeint+10 <=64){
                        JSONArray arrayUsers  = (JSONArray)tempJSON.get("groups");
                        
                        JSONObject tempuser = new JSONObject();
                        tempuser.put("name", name);

                        arrayUsers.add(tempuser);
                                             
                        tempJSON.replace("size", Integer.toString(sizeint+10));
                        
                        flag =false;
                        break;
                    }
                }
            }
        }
        
        if(flag){
            for (Object obj : fsStructure) {
                JSONObject tempJSON = (JSONObject) obj;
                if(tempJSON.isEmpty()){

                    tempJSON.put("kind", "groups");
                    tempJSON.put("size", "14");

                    JSONArray arrayUsers  = new JSONArray();

                    JSONObject tempuser = new JSONObject();
                    tempuser.put("name", name);

                    arrayUsers.add(tempuser);

                     tempJSON.put("groups", arrayUsers);
                     break;
                }
            }
        }
        //System.out.println(fsStructure);
         writeInFile();
    }
    
    public void addPath(String id, String path, String name, String owner, String group,String permissions,String date){
        for (Object obj : fsStructure) {
             JSONObject tempJSON = (JSONObject) obj;
            if(tempJSON.isEmpty()){
                tempJSON.put("kind", "dir");
                tempJSON.put("id", id);
                tempJSON.put("path", path);
                tempJSON.put("name", name);
                tempJSON.put("owner", owner);
                tempJSON.put("group", group);
                tempJSON.put("permissions", permissions);
                tempJSON.put("date", date);
                break;
            }
        }
        //System.out.println(fsStructure);
         writeInFile();
    }
    
    public int getInitIndexForFile(int sizeOfBloks){
        int sizeofJson = fsStructure.size();
        int res = -1;
        
        for(int i=0; i<sizeofJson;i++){
            boolean flag =true;
            JSONObject tempJSON = (JSONObject) fsStructure.get(i);
            if(tempJSON.isEmpty()){
                for(int j=0; j<sizeOfBloks;j++){
                    JSONObject tempJSON2 = (JSONObject) fsStructure.get(i+j);
                    if(!tempJSON2.isEmpty()){
                        flag = false;
                    }
                }
                if(flag){
                    res = i;
                    break;
                } 
            }
        }
        
        
        return res;
    }
    
    public void addFile(String id,String type, String path, String name, String size, String owner, String group,String permissions,String date, String content, boolean update){
        int sizeOFContent = content.length();
        int sizeOfBloks = Math.round(sizeOFContent/50);
        
        if(update){
            deleteFile(id);
        }
        
        //buscar campo
        int initBlock = getInitIndexForFile(sizeOfBloks);
       
        String tempString =content;
        for(int i =0; i<=sizeOfBloks; i++){
            String temp;
            if(tempString.length()>=51){
                temp = tempString.substring(0, 51);
                tempString =  tempString.substring(51);
            }else{
                temp = tempString.substring(0, tempString.length());
                tempString =  tempString.substring(tempString.length());
            }
            
            JSONObject tempJSON = (JSONObject) fsStructure.get(initBlock);
            
            tempJSON.put("kind", "file");
            tempJSON.put("id", id);
            tempJSON.put("type", type);
            tempJSON.put("path", path);
            tempJSON.put("name", name);
            tempJSON.put("size", Integer.toString(14+temp.length()));
            tempJSON.put("owner", owner);
            tempJSON.put("group", group);
            tempJSON.put("permissions", permissions);
            tempJSON.put("date", date);
            tempJSON.put("content", temp);

            initBlock++;
        }
        //System.out.println(fsStructure); 
         writeInFile();
    }
     
    public JSONArray getUsers(){
        JSONArray resArray =new JSONArray();
        
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("users")){
                    JSONArray arrayUsers  = (JSONArray)tempJSON.get("users");
                    resArray.addAll(arrayUsers);
                    
                }
            }
        }
        return resArray;    
    }
    
    public JSONArray getGroups(){
        JSONArray resArray =new JSONArray();
        
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("groups")){
                    JSONArray arrayUsers  = (JSONArray)tempJSON.get("groups");
                    resArray.addAll(arrayUsers);
                    
                }
            }
        }
        return resArray;   
    }
    
    public JSONArray getDir(){
        JSONArray resArray =new JSONArray();
        
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("dir")){
                    resArray.add(tempJSON);
                    
                }
            }
        }
        return resArray; 
    }
    
    public JSONArray getFiles(){
        JSONArray resArray =new JSONArray();
        
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("file")){
                    resArray.add(tempJSON);
                    
                }
            }
        }
        return resArray; 
    }
    
    public void deleteFile(String id){
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("file") ){ 
                    String id_obj = (String) tempJSON.get("id");
                    if(id_obj.equals(id)){
                        tempJSON.clear();
                    }
                }
            }
            
        } 
        //System.out.println(fsStructure);
         writeInFile();
    }
    
    public void deletePath(String id){
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("dir") ){ 
                    String id_obj = (String) tempJSON.get("id");
                    if(id_obj.equals(obj)){
                        tempJSON.clear();
                    }
                }
            }
            
        } 
        //System.out.println(fsStructure);
         writeInFile();
    }

    public void changeOwnerFile(String id, String owner){
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("file") ){ 
                   String id_obj = (String) tempJSON.get("id");
                    if(id_obj.equals(id)){
                        tempJSON.replace("owner", owner);
                    }
                }
            }
            
        } 
        //System.out.println(fsStructure);.
         writeInFile();
    }
    
     public void changeGroupFile(String id, String group){
        for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("file") ){ 
                    String id_obj = (String) tempJSON.get("id");
                    if(id_obj.equals(id)){
                        tempJSON.replace("group", group);
                    }
                }
            }
            
        } 
        //System.out.println(fsStructure);
         writeInFile();
    }
     
     public void changePermissionFile(String id, int permissions){
         for (Object obj : fsStructure) {
            JSONObject tempJSON = (JSONObject) obj;
            
            if(!tempJSON.isEmpty()){
                String kind = (String) tempJSON.get("kind");
                if(kind.equals("file") ){ 
                    String id_obj = (String) tempJSON.get("id");
                    if(id_obj.equals(id)){
                        tempJSON.replace("permissions", permissions);
                    }
                }
            }
            
        } 
        //System.out.println(fsStructure);
         writeInFile();
     }

}
