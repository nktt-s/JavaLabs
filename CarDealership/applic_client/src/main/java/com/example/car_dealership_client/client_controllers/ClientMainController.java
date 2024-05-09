package com.example.car_dealership_client.client_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.controllers.NameEnterController;
import com.example.car_dealership_client.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientMainController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @FXML
    TextField nameTextField;
    @FXML
    Label enterNameLabel;
    @FXML
    Label clientNameHeader;
    private Stage stage;
    private static String clientName;
    private static List<ApplicationData> waiting_applics = new ArrayList<>();
    private static List<ApplicationData> progress_applics = new ArrayList<>();
    private static List<ApplicationData> rejected_applics = new ArrayList<>();
    private static List<ApplicationData> finished_applics = new ArrayList<>();
    private static List<ApplicationData> cancelled_applics = new ArrayList<>();
    private static Client client;
    private static List<ApplicationData> shadow_data = new ArrayList<>();
    //All-time number of applications for unique applications id;
    private static Integer total_id;


    public void setHeaderName(String clientName) {
        ClientMainController.clientName = clientName;
        clientNameHeader.setText("Welcome, " + clientName + "!");
    }

    public void prepareMainMenu(Client client) {
        setHeaderName(clientName);
        this.client = client;
    }

    public void prepareMainMenu(String clientName, Client client) {
        setHeaderName(clientName);
        this.client = client;
    }

    public void connect(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            client = new Client(socket, ois, oos);
            client.sendNameToServer(clientName);
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO
    public void onInStockButtonClicked(ActionEvent applications_clicked) throws IOException {
        loggerMain.info("От имени Клиента {} нажата кнопка получения автомобилей в наличии", clientName);
        stage = (Stage) ((Node) applications_clicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Cars in stock page");
        FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("client/cars_in_stock.fxml"));
        fxmlloader.load();
        ClientInStockController controller = fxmlloader.getController();
        controller.start(stage, client, clientName);
    }

    // TODO
    public void onShowMyOrdersButtonClicked(ActionEvent button_clicked) throws IOException {
        stage = (Stage) ((Node) button_clicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | My active orders page");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("my_orders.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ClientProgressController controller = loader.getController();
        controller.prepare_applications(client, progress_applics);
        stage.setScene(scene);
        stage.show();
    }

    // TODO
    public void switchToMainMenu(ActionEvent go_back_clicked) throws IOException {
        Stage stage = (Stage) ((Node) go_back_clicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Client page");
        FXMLLoader menuLoader = new FXMLLoader(Main.class.getResource("client_main.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        ClientMainController menuController = menuLoader.getController();
        stage.setScene(menuScene);
        stage.show();
    }

    // TODO
    public void onCompletedOrdersButtonClicked(ActionEvent button_clicked) throws IOException {
        stage = (Stage) ((Node) button_clicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Cars sold page");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("cars_sold.fxml")); // TODO Создать отдельный FXML
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ClientClosedController controller = loader.getController();
        List<ApplicationData> closed = new ArrayList<>();
//        System.out.println("Here are closed: " + finished_applics + rejected_applics + cancelled_applics);
        closed.addAll(finished_applics);
        closed.addAll(rejected_applics);
        closed.addAll(cancelled_applics);
        controller.prepare_applications(client, closed);
        stage.setScene(scene);
        stage.show();
    }


    public void onLogOutButtonClicked(ActionEvent quit_clicked) throws IOException {
        stage = (Stage) ((Node) quit_clicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Login page");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        NameEnterController controller = loader.getController();
        controller.prepareEnterName();
        stage.setScene(scene);
        stage.show();
        client.stop_connection();
    }

    public static void remove_applications(List<ApplicationData> waiting_applics_in, List<ApplicationData> applics) {
        waiting_applics.addAll(applics);
    }

    public static void clearAllApplics() {
        waiting_applics = new ArrayList<>();
        progress_applics = new ArrayList<>();
        rejected_applics = new ArrayList<>();
        finished_applics = new ArrayList<>();
        cancelled_applics = new ArrayList<>();
    }

    public static void updateAllApplics(List<ApplicationData> wait, List<ApplicationData> progress, List<ApplicationData> rejected, List<ApplicationData> finished, List<ApplicationData> cancelled) {
        clearAllApplics();
        waiting_applics = wait;
        progress_applics = progress;
        rejected_applics = rejected;
        finished_applics = finished;
        cancelled_applics = cancelled;
    }

    public List<ApplicationData> getAllApplications() {
        List<ApplicationData> all_applics = new ArrayList<>();
        all_applics.addAll(waiting_applics);
        all_applics.addAll(progress_applics);
        all_applics.addAll(rejected_applics);
        all_applics.addAll(finished_applics);
        all_applics.addAll(cancelled_applics);
        return all_applics;
    }

    public void save() throws IOException {
        if (shadow_data.isEmpty()) loadShadowData();
        List<ApplicationData> all_applics = getAllApplications();
        all_applics.addAll(shadow_data);
        FileOutputStream fos = new FileOutputStream("applics.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(all_applics);
        oos.close();
    }

    public void loadShadowData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("applics.ser"))) {
            List<ApplicationData> applics = (List<ApplicationData>) ois.readObject();
            for (ApplicationData app : applics) {
                if (!app.get_name().equals(clientName)) {
                    shadow_data.add(app);
                }
            }
//            file_logger.info("Devices were Deserialized");
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sortUsersApplic(List<ApplicationData> all_applics) {
        for (ApplicationData app : all_applics) {
            if (app.get_name().equals(clientName)) {
                switch (app.get_status()) {
                    case "On wait" -> waiting_applics.add(app);
                    case "In Progress" -> progress_applics.add(app);
                    case "Finished" -> finished_applics.add(app);
                    case "Rejected" -> rejected_applics.add(app);
                    case "Cancelled" -> cancelled_applics.add(app);
                    default -> throw new IllegalStateException("Unexpected value: " + app.get_status());
                }
            } else {
                shadow_data.add(app);
            }
        }
    }

    public void load() {
        shadow_data.clear();
        clearAllApplics();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("applics.ser"))) {
            List<ApplicationData> applics = (List<ApplicationData>) ois.readObject();
//            file_logger.info("Devices were Deserialized");
            sortUsersApplic(applics);
        } catch (ClassNotFoundException | ClassCastException e) {
            System.exit(228);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer get_total_id() {
        return total_id;
    }

    public static void set_total_id(Integer new_total_id) {
        total_id = new_total_id;
    }
}
