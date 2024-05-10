package com.example.applic_server.models.handlers;

import com.example.applic_server.models.*;
import com.example.applic_server.controllers.SerMainController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WorkerHandler extends UserHandler {
    public static ArrayList<WorkerHandler> workerHandlers = new ArrayList<>();

    public WorkerHandler() {

    }

    public WorkerHandler(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        super(socket, ois, oos);
    }

    @Override
    public void run() {
        workerHandlers.add(this);
        while (socket.isConnected()) {
            ApplicationData applicationFromWorker;
            try {
                //TODO give an id to application
                if (name == null) {
                    name = (String) ois.readObject();
                    server_logger.info("New worker's name was received");
                }
                send_applications();
                Object messgFromWorker = ois.readObject();
                applicationFromWorker = converter.list_to_application((List<String>) messgFromWorker);
                SerMainController.updateApplication(applicationFromWorker);
                new AdminHandler().send_applications();
                new ClientHandler().send_applications();
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
//        System.out.println("--------Worker Handlers: " + workerHandlers);
        for (WorkerHandler workerHandler : workerHandlers) {
            List<ApplicationData> every_user_application = new ArrayList<>();
            SerMainController.get_all_applications().forEach(app -> {
//                System.out.println(app.get_worker() + " + " + workerHandler.name);
                if (app.get_status().equals("In Progress")) if (app.get_worker().equals(workerHandler.name)) {
                    every_user_application.add(app);
                }
            });
            List<String> every_application_string = converter.app_to_list(every_user_application);
//            System.out.println("--------Applications for worker list" + every_application_string);
            try {
                workerHandler.oos.writeObject(every_application_string);
                workerHandler.oos.flush();
//                System.out.println("--------Applications were sent to client");
                server_logger.info("Applications were sent to client");
            } catch (IOException e) {
                closeEverything(socket, ois, oos);
            }
        }
    }
}
