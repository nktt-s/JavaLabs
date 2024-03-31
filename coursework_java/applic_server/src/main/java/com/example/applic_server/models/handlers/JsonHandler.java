package com.example.applic_server.models.handlers;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.HashMap;

public class JsonHandler {
    Gson gson = new Gson();




    public void store_json_passswords(HashMap<String, String> passwords, HashMap<String,String> users){
//        System.out.println("I was storing!");
        try (FileWriter writer = new FileWriter("passwords.json")){
            String json_pass = gson.toJson(passwords);
            writer.write(json_pass);
        }
        catch(IOException e){
//                file_logger.error("Error saving heaphones" + e.toString());
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter("users.json")){
            String json_users = gson.toJson(users);
            writer.write(json_users);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public HashMap<String, String> load_json_passwords(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("passwords.json"));
            return (HashMap<String,String>) gson.fromJson(br, HashMap.class);
        }
        catch (EmptyStackException e){
//            System.out.println("Yep");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public HashMap<String, String> load_json_users(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("users.json"));
            return (HashMap<String,String>) gson.fromJson(br, HashMap.class);
        }
        catch (EmptyStackException e){
//            System.out.println("Yep");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
