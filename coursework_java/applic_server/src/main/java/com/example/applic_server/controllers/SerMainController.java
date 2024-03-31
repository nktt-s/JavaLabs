package com.example.applic_server.controllers;

import com.example.applic_server.models.handlers.JsonHandler;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//TODO ApplicationData class
//TODO Applications controller render
//TODO Applications controller reject/accept(two another arrays)
import com.example.applic_server.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerMainController {
    private static final Logger file_logger = LoggerFactory.getLogger("file_data");
    private static final Logger applic_logger = LoggerFactory.getLogger("applications");
    private Stage stage;
    Server server;
    private static DataBaseHandler db_handler = new DataBaseHandler();
    private static List<ApplicationData> all_applics = new ArrayList<>();
    private static List<String> workers = new ArrayList<>();
    //TODO Read total_id from ser/des
    private static Integer total_id;
    JsonHandler js_handler = new JsonHandler();


    public void start_menu() {
        /*ConsoleMenu menu = new ConsoleMenu();
        Thread menu_thread = new Thread(menu);
        menu_thread.start();*/
    }

    public void get_workers() {
        for (Map.Entry<String, String> entry : js_handler.load_json_users().entrySet()) {
            if (entry.getValue().equals("Worker")) {
                workers.add(entry.getKey());
            }
        }
    }

    //TODO Pass three lists of applications here
    // Then make this prepare for other controllers
    public void connect() {
        try {
            deserialize_applics();
            all_applics.clear();
            deserialize_total();
            server = new Server(new ServerSocket(3308));
            Thread serverThread = new Thread(server);
            serverThread.start();
//            System.out.println("Server is running.");

//            server.receiveApplicationsFromClient(waiting_applics);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer get_total_id() {
        return total_id;
    }

    public static void increment_total_id() {
        total_id += 1;
    }

    public static void serialize_total() {
        file_logger.info("Serialized total");
        try {
            FileOutputStream fos = new FileOutputStream("total.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(total_id);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serialize_applics() {
        file_logger.info("Serialized applications");
        try {
            FileOutputStream fos = new FileOutputStream("applics.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(all_applics);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserialize_applics() throws IOException {
        file_logger.info("DeSerialized applications");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("applics.ser"))) {
            all_applics = (List<ApplicationData>) ois.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            System.exit(228);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserialize_total() {
        file_logger.info("DeSerialized total");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("total.ser"))) {
            total_id = (Integer) ois.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            System.exit(228);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void add_application(ApplicationData applic) {
        applic_logger.info("Application added");
        all_applics.add(applic);
//        serialize_applics();
        db_handler.update_db(all_applics);
    }

    public static void clear_all_applications() {
        all_applics.clear();
    }

    public static List<ApplicationData> get_all_applications() {
        return all_applics;
    }

    public static void updateApplication(ApplicationData application) {
        applic_logger.info("Application updated");
        //Update an application when admin sends it to us
        all_applics.forEach((app) -> {
            if (app.get_id().equals(application.get_id())) {
                app.set_status(application.get_status());
                app.set_worker(application.get_worker());
                //TODO make this function of worker
//                    app.set_worker(application.get_worker());
            }
        });
        db_handler.update_db(all_applics);
    }

    public static List<String> getWorkers() {
        return workers;
    }


//    public void save() throws IOException {
//        db_handler.update_db(get_all_applications());
//        List<ApplicationData> all_applics = get_all_applications();
//        FileOutputStream fos = new FileOutputStream("devs.ser");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(all_applics);
//        oos.close();
//        file_logger.info("Devices were serialized");
//
//
//    }

//    public static void sort_users_applic(List<ApplicationData> all_applics) {
//        //Sort a list of applications between lists of applications for each status
//        for (ApplicationData app : all_applics) {
//            switch (app.get_status()) {
//                case "On wait" -> waiting_applics.add(app);
//                case "In Progress" -> progress_applics.add(app);
//                case "Finished", "Rejected", "Cancelled" -> closed_applics.add(app);
//                default -> throw new IllegalStateException("Unexpected value: " + app.get_status());
//            }
//        }
//    }
//    public void load(){
//        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("devs.ser"))){
//            List<ApplicationData> applics = (List<ApplicationData>) ois.readObject();
//            file_logger.info("Devices were Deserialized");
//        sort_users_applic(applics);
//        } catch (ClassNotFoundException | ClassCastException e) {
//            System.exit(228);
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
