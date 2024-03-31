package com.example.applic_server.models.handlers;

import com.example.applic_server.controllers.SerMainController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.example.applic_server.models.*;

public class AdminHandler extends UserHandler {


        public static ArrayList<AdminHandler> adminHandlers = new ArrayList<>();
    public AdminHandler(){

    }

    public AdminHandler(Socket socket, ObjectInputStream ois, ObjectOutputStream oos){
        super(socket, ois, oos);
    }

        @Override
        public void run(){
            adminHandlers.add(this);
            send_applications();
            while(socket.isConnected()){
                ApplicationData application;
                try {
                    Object incomingMessage = ois.readObject();
                    application = converter.list_to_application((List<String>) incomingMessage);
                    SerMainController.updateApplication(application);
                    new ClientHandler().send_applications();
                    new WorkerHandler().send_applications();
                }

                catch (IOException e) {
//                e.printStackTrace();
                    closeEverything(socket,ois,oos);
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        public void send_applications(){
            for (AdminHandler adminHandler: adminHandlers){
                try {
                    adminHandler.oos.writeObject(SerMainController.getWorkers());
                    adminHandler.oos.writeObject(converter.app_to_list(SerMainController.get_all_applications()));
                    adminHandler.oos.flush();
                    server_logger.info("Applications were sent to admin");
                }
                catch (IOException e){
                    closeEverything(socket,ois,oos);
                }
            }
        }

    }