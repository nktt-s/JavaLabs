package com.example.car_dealership_client.seller_controllers;

import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Worker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class WorkerMainController {
    @FXML
    Label workerNameHeader;
    private Stage stage;
    private static String name;
    private Worker worker;
    private static List<ApplicationData> progress_applics;

    public void switchToJobs(ActionEvent applications_clicked) throws IOException {
        stage = (Stage)((Node)applications_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/seller_views/work-jobs.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SellerJobsController controller = loader.getController();
        controller.prepare_jobs(progress_applics, name, worker);


        stage.setScene(scene);
        stage.show();


    }
    public static List<ApplicationData> get_all_applications(){
       return progress_applics;
    }
    public void set_header_name(String name){
        WorkerMainController.name = name;
        workerNameHeader.setText("Worker " + name + ".");
    }
    public void prepare_main_menu(Worker worker_inp){
        set_header_name(name);
        worker = worker_inp;
    }
    public void prepare_main_menu(Worker worker_inp, List<ApplicationData> applics){
        set_header_name(name);
        worker = worker_inp;
        progress_applics = applics;
    }
    public void prepare_main_menu(String inp_name, Worker worker_inp){
        set_header_name(inp_name);
        worker = worker_inp;

    }
        public void connect(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            worker = new Worker(socket, ois, oos);
            worker.sendNameToServer(name);
            Thread workerThread = new Thread(worker);
            workerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void quit(ActionEvent quit_clicked) throws IOException{
        worker.stop_connection();
        Platform.exit();
        System.exit(0);
    }

    public static void update_all_applications(List<ApplicationData> applics){
        progress_applics = applics;
    }
    public static void delete_application(int applic){
        progress_applics.remove(applic);
    }


}
