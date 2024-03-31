package com.example.car_dealership_client.admin_controllers;

import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Admin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AdmMainController {
    private Stage stage;
    private static String name;
    @FXML
    Label adminNameHeader;
//    Server server;
//    DataBaseHandler db_handler = new DataBaseHandler();
    private static List<ApplicationData> waiting_applics = new ArrayList<>();
    private static List<ApplicationData> progress_applics = new ArrayList<>();
    private static List<ApplicationData> closed_applics = new ArrayList<>();
    private static Admin admin;
    private static List<String> workers = new ArrayList<>();



        public void connect(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            admin = new Admin(socket, ois, oos);
            Thread adminThread = new Thread(admin);
            adminThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void refresh(){
        try{
            admin.getApplicationsFromServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void clear_all_applics(){
        waiting_applics = new ArrayList<>();
        progress_applics = new ArrayList<>();
        closed_applics = new ArrayList<>();

    }
    public static void update_all_applics(List<ApplicationData> wait, List<ApplicationData> progress, List<ApplicationData> rejected, List<ApplicationData> finished, List<ApplicationData> cancelled){
        clear_all_applics();
        waiting_applics = wait;
        progress_applics = progress;
        closed_applics.addAll(rejected);
        closed_applics.addAll(finished);
        closed_applics.addAll(cancelled);
    }

    public static void update_workers(List<String> new_workers){
        workers = new_workers;


    }

   //TODO Pass three lists of applications here
    // Then make this prepare for other controllers
//    public void connect(){
//        try{
//            server = new Server(new ServerSocket(1234));
//            Thread serverThread = new Thread(server);
//            serverThread.start();
////            server.receiveApplicationsFromClient(waiting_applics);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void send(ApplicationData applic){
//        progress_applics.forEach(ApplicationData::print);
//            client.sendApplicationsToServer(applic);
//        System.out.println("Right before here");
//        client.sendApplicationsToServer(("Pipez"));
//        new ClientHandler().send_applications();
    }

//   public void prepare_main_menu(List<ApplicationData> waiting_applics, List<ApplicationData> rejected_applics, List<ApplicationData> finished_applics){
//   }

    public void prepare_main_menu(String name, Admin admin_inp){
        admin = admin_inp;
//        this.name = name;
        AdmMainController.name = name;
        adminNameHeader.setText("Admin");
    }
    public void switchToApplications(ActionEvent applications_clicked) throws IOException {
        stage = (Stage)((Node)applications_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/adm_views/applications.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AdmApplicController controller = loader.getController();
//        System.out.println(waiting_applics);
        controller.prepare_applications(waiting_applics, name, admin);


        stage.setScene(scene);
        stage.show();


    }

    public void switchToProgress(ActionEvent button_clicked) throws IOException {
        stage = (Stage)((Node)button_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/myOrders.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AdmProgressController controller = loader.getController();
        controller.prepare_applications(progress_applics, admin);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClosed(ActionEvent button_clicked) throws IOException {
        stage = (Stage)((Node)button_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/adm_views/forSale.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AdmClosedController controller = loader.getController();
        controller.prepare_applications(closed_applics, admin);
        stage.setScene(scene);
        stage.show();
    }
    public void quit(ActionEvent quit_clicked) throws IOException{
        Platform.exit();
    }

    public static void add_waiting_application(ApplicationData applic){
            waiting_applics.add(applic);
    }


    public static void add_progress_application(ApplicationData applic){
            progress_applics.add(applic);
//            System.out.println("This is progress - " + progress_applics);
    }

    public static void add_closed_application(ApplicationData applic){
            closed_applics.add(applic);
    }

    public static void update_closed_applications(List<ApplicationData> applics){
        closed_applics = applics;
    }
    public static List<ApplicationData> get_all_applications(){
        List<ApplicationData> all_applics = new ArrayList<>();
        all_applics.addAll(waiting_applics);
        all_applics.addAll(progress_applics);
        all_applics.addAll(closed_applics);

        return all_applics;
    }


    public void save() throws IOException {
//        db_handler.update_db(get_all_applications());
//        List<ApplicationData> all_applics = get_all_applications();
//        FileOutputStream fos = new FileOutputStream("devs.ser");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(all_applics);
//        oos.close();
//        file_logger.info("Devices were serialized");


    }

    public void sort_users_applic(List<ApplicationData> all_applics) {
        for (ApplicationData app : all_applics) {
            switch (app.get_status()) {
                case "On wait" -> waiting_applics.add(app);
                case "In Progress" -> progress_applics.add(app);
                case "Finished", "Rejected", "Cancelled" -> closed_applics.add(app);
                default -> throw new IllegalStateException("Unexpected value: " + app.get_status());
            }
        }
    }
    public void load(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("devs.ser"))){
            List<ApplicationData> applics = (List<ApplicationData>) ois.readObject();
        sort_users_applic(applics);
        } catch (ClassNotFoundException | ClassCastException e) {
            System.exit(228);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> get_workers_list(){
       return workers;
    }


}

