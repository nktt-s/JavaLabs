package com.example.car_dealership_client.admin_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.controllers.NameEnterController;
import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AdminMainController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    private Stage stage;
    private static String name;
    @FXML
    Label adminNameHeader;
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

    public static void clearAllApplics() {
        waiting_applics = new ArrayList<>();
        progress_applics = new ArrayList<>();
        closed_applics = new ArrayList<>();
    }

    public static void updateAllApplics(List<ApplicationData> wait, List<ApplicationData> progress, List<ApplicationData> rejected, List<ApplicationData> finished, List<ApplicationData> cancelled) {
        clearAllApplics();
        waiting_applics = wait;
        progress_applics = progress;
        closed_applics.addAll(rejected);
        closed_applics.addAll(finished);
        closed_applics.addAll(cancelled);
    }

    public static void updateWorkers(List<String> new_workers) {
        workers = new_workers;
    }

    public void prepareMainMenu(String name, Admin admin_inp) {
        admin = admin_inp;
//        this.name = name;
        AdminMainController.name = name;
        adminNameHeader.setText("Admin");
    }

    public void onInStockButtonClicked(ActionEvent inStockClicked) throws IOException {
        loggerMain.info("От имени Администратора нажата кнопка получения автомобилей в наличии");
        stage = (Stage) ((Node) inStockClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin/cars_in_stock.fxml"));
        fxmlLoader.load();
        AdminInStockController controller = fxmlLoader.getController();
        controller.start(stage);
    }

    public void onInProgressButtonClicked(ActionEvent inProgressClicked) throws IOException {
        loggerMain.info("От имени Администратора нажата кнопка получения автомобилей в процессе продажи");
        stage = (Stage) ((Node) inProgressClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin/cars_in_progress.fxml"));
        fxmlLoader.load();
        AdminInProgressController controller = fxmlLoader.getController();
        controller.start(stage);
    }

    public void onSoldButtonClicked(ActionEvent button_clicked) throws IOException {
        loggerMain.info("От имени Администратора нажата кнопка получения проданных автомобилей");
        stage = (Stage) ((Node) button_clicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin/cars_sold.fxml"));
        fxmlLoader.load();
        AdminSoldController controller = fxmlLoader.getController();
        controller.start(stage);
    }

    public void onLogOutButtonClicked(ActionEvent logOutClicked) throws IOException {
        stage = (Stage) ((Node) logOutClicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Login page");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        NameEnterController controller = loader.getController();
        controller.prepareEnterName();
        stage.setScene(scene);
        stage.show();
        admin.stop_connection();
    }

    public void sortUsersApplic(List<ApplicationData> all_applics) {
        for (ApplicationData app : all_applics) {
            switch (app.get_status()) {
                case "On wait" -> waiting_applics.add(app);
                case "In Progress" -> progress_applics.add(app);
                case "Finished", "Rejected", "Cancelled" -> closed_applics.add(app);
                default -> throw new IllegalStateException("Unexpected value: " + app.get_status());
            }
        }
    }

    public void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("devs.ser"))) {
            List<ApplicationData> applics = (List<ApplicationData>) ois.readObject();
            sortUsersApplic(applics);
        } catch (ClassNotFoundException | ClassCastException e) {
            System.exit(228);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

