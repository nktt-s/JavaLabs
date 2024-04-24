package com.example.car_dealership_client.seller_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.controllers.NameEnterController;
import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Seller;
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

public class SellerMainController {
    @FXML
    Label workerNameHeader;
    private Stage stage;
    private static String name;
    private Seller seller;
    private static List<ApplicationData> progress_applics;

    public static List<ApplicationData> get_all_applications() {
        return progress_applics;
    }

    public void set_header_name(String name) {
        SellerMainController.name = name;
        workerNameHeader.setText("Welcome, seller, " + name + "!");
    }

    public void prepare_main_menu(Seller seller_inp) {
        set_header_name(name);
        seller = seller_inp;
    }

    public void prepare_main_menu(Seller seller_inp, List<ApplicationData> applics) {
        set_header_name(name);
        seller = seller_inp;
        progress_applics = applics;
    }

    public void prepare_main_menu(String inp_name, Seller seller_inp) {
        set_header_name(inp_name);
        seller = seller_inp;

    }

    public void connect(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            seller = new Seller(socket, ois, oos);
            seller.sendNameToServer(name);
            Thread sellerThread = new Thread(seller);
            sellerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToJobs(ActionEvent applications_clicked) throws IOException {
        stage = (Stage) ((Node) applications_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("work_jobs.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SellerJobsController controller = loader.getController();
        controller.prepare_jobs(progress_applics, name, seller);
        stage.setScene(scene);
        stage.show();
    }

    public void quit(ActionEvent quit_clicked) throws IOException {
        stage = (Stage) ((Node) quit_clicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Login page");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        NameEnterController controller = loader.getController();
        controller.prepareEnterName();
        stage.setScene(scene);
        stage.show();
        seller.stop_connection();
    }

    public static void update_all_applications(List<ApplicationData> applics) {
        progress_applics = applics;
    }

    public static void delete_application(int applic) {
        progress_applics.remove(applic);
    }


}
