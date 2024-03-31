package com.example.car_dealership_client.client_controllers;

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

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientMainController {
    @FXML
    TextField nameTextField;
    @FXML
    Label enterNameLabel;
    @FXML
    Label clientNameHeader;
    private Stage stage;
    private static String name;
    private static List<ApplicationData> waiting_applics = new ArrayList<>();
    private static List<ApplicationData> progress_applics = new ArrayList<>();
    private static List<ApplicationData> rejected_applics = new ArrayList<>();
    private static List<ApplicationData> finished_applics = new ArrayList<>();
    private static List<ApplicationData> cancelled_applics = new ArrayList<>();
    private Client client;
    private static List<ApplicationData> shadow_data = new ArrayList<>();
    //All-time number of applications for unique applications id;
    private static Integer total_id;


    public void set_header_name(String name) {
        ClientMainController.name = name;
        clientNameHeader.setText("Welcome, client, " + name + "!");
    }

    public void prepare_main_menu(Client client_inp) {
        set_header_name(name);
        client = client_inp;
    }

    public void prepare_main_menu(String inp_name, Client client_inp) {
        set_header_name(inp_name);
        client = client_inp;

    }

    public void connect(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {

        try {
            client = new Client(socket, ois, oos);
            client.sendNameToServer(name);
            Thread clientThread = new Thread(client);
            clientThread.start();
//            client.receiveApplicationsFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void refresh() {
        try {
            client.getApplicationsFromServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void get() {
        for (ApplicationData app : waiting_applics) {
//            System.out.println(app.get_name());
        }
//       set_header_name();
    }

    public void onLogOutButtonClicked1(ActionEvent applications_clicked) throws IOException {
        stage = (Stage) ((Node) applications_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/apply.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ClientApplyController controller = loader.getController();
        controller.prepare_applications(client, name);
        stage.setScene(scene);
        stage.show();
    }

    public void onShowMyOrdersButtonClicked(ActionEvent button_clicked) throws IOException {
        stage = (Stage) ((Node) button_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/myOrders.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ClientProgressController controller = loader.getController();
        controller.prepare_applications(client, progress_applics);
        stage.setScene(scene);
        stage.show();
    }

    public void onForSaleButtonClicked(ActionEvent button_clicked) throws IOException {
        stage = (Stage) ((Node) button_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/forSale.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/name-enter.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        NameEnterController controller = loader.getController();
//        System.out.println("Here are closed: " + finished_applics + rejected_applics + cancelled_applics);
        controller.prepare_enter_name();
        stage.setScene(scene);
        stage.show();

//        client.stop_connection();
//        Platform.exit();
//        System.exit(0);
    }

    public static void remove_applications(List<ApplicationData> waiting_applics_in, List<ApplicationData> applics) {
        waiting_applics.addAll(applics);
//        System.out.println(waiting_applics);
    }

    public static void clear_all_applics() {
        waiting_applics = new ArrayList<>();
        progress_applics = new ArrayList<>();
        rejected_applics = new ArrayList<>();
        finished_applics = new ArrayList<>();
        cancelled_applics = new ArrayList<>();
    }

    public static void update_all_applics(List<ApplicationData> wait, List<ApplicationData> progress, List<ApplicationData> rejected, List<ApplicationData> finished, List<ApplicationData> cancelled) {
        clear_all_applics();
        waiting_applics = wait;
//        System.out.println("Progress applics in update_all_applics in CMC - " + progress);
        progress_applics = progress;
        rejected_applics = rejected;
        finished_applics = finished;
        cancelled_applics = cancelled;
    }

    public List<ApplicationData> get_all_applications() {
        List<ApplicationData> all_applics = new ArrayList<>();
        all_applics.addAll(waiting_applics);
        all_applics.addAll(progress_applics);
        all_applics.addAll(rejected_applics);
        all_applics.addAll(finished_applics);
        all_applics.addAll(cancelled_applics);
        return all_applics;
    }

    public void save() throws IOException {
        if (shadow_data.isEmpty()) {
            load_shadow_data();
        }
        List<ApplicationData> all_applics = get_all_applications();
        all_applics.addAll(shadow_data);
        FileOutputStream fos = new FileOutputStream("applics.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(all_applics);
        oos.close();

    }

    public void load_shadow_data() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("applics.ser"))) {
            List<ApplicationData> applics = (List<ApplicationData>) ois.readObject();
            for (ApplicationData app : applics) {
                if (!app.get_name().equals(name)) {
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

    public void sort_users_applic(List<ApplicationData> all_applics) {
        for (ApplicationData app : all_applics) {
            if (app.get_name().equals(name)) {
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
        clear_all_applics();
//        System.out.println("Loaded");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("applics.ser"))) {
            List<ApplicationData> applics = (List<ApplicationData>) ois.readObject();
//            file_logger.info("Devices were Deserialized");
            sort_users_applic(applics);
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
