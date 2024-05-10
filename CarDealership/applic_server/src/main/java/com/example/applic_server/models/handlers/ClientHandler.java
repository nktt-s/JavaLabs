package com.example.applic_server.models.handlers;

import com.example.applic_server.controllers.SerMainController;
//import com.example.car_dealership_server.models.Server.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.example.applic_server.models.*;

public class ClientHandler extends UserHandler {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public ClientHandler() {

    }

    public ClientHandler(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        super(socket, ois, oos);
    }

    @Override
    public void run() {
        clientHandlers.add(this);
        while (socket.isConnected()) {
            ApplicationData applicationFromClient;
            try {
                //TODO give an id to application
                if (name == null) {
                    name = (String) ois.readObject();
                    server_logger.info("New client's name was received");
                }
                send_applications();
                Object messgFromClient = ois.readObject();
                applicationFromClient = converter.list_to_application((List<String>) messgFromClient);
                applicationFromClient.set_id(SerMainController.get_total_id());
                SerMainController.increment_total_id();
                SerMainController.add_application(applicationFromClient);
                new AdminHandler().send_applications();
            } catch (IOException e) {
//                e.printStackTrace();
                closeEverything(socket, ois, oos);
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void send_applications() {
        for (ClientHandler clientHandler : clientHandlers) {
            List<ApplicationData> every_user_application = new ArrayList<>();
            SerMainController.get_all_applications().forEach(app -> {
                if (app.get_name().equals(clientHandler.name)) {
                    every_user_application.add(app);
                }
            });
            List<String> every_application_string = converter.app_to_list(every_user_application);
            try {
                clientHandler.oos.writeObject(every_application_string);
                clientHandler.oos.flush();
                server_logger.info("Applications were sent to client");
            } catch (IOException e) {
                closeEverything(socket, ois, oos);
            }
        }
    }
}
