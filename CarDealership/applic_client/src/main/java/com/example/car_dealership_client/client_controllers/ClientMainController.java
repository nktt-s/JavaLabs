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
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.Socket;

public class ClientMainController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @FXML
    Label clientNameHeader;
    private Stage stage;
    private static Client client;
    private static String clientName;

    public void setHeaderName(String clientName) {
        ClientMainController.clientName = clientName;
        clientNameHeader.setText("Welcome, " + clientName + "!");
    }

    public void prepareMainMenu(Client client) {
        setHeaderName(clientName);
        ClientMainController.client = client;
    }

    public void prepareMainMenu(String clientName, Client client) {
        setHeaderName(clientName);
        ClientMainController.client = client;
        ClientMainController.clientName = clientName;
    }

    public void connect(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            client = new Client(socket, ois, oos);
//            client.sendNameToServer(clientName);
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onInStockButtonClicked(ActionEvent inStockClicked) throws IOException {
        loggerMain.info("От имени Клиента {} нажата кнопка получения автомобилей в наличии", clientName);
        stage = (Stage) ((Node) inStockClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("client/cars_in_stock.fxml"));
        fxmlloader.load();
        ClientInStockController controller = fxmlloader.getController();
        controller.start(stage, client, clientName);
    }

    public void onMyActiveOrdersButtonClicked(ActionEvent buttonClicked) throws IOException {
        loggerMain.info("От имени Клиента {} нажата кнопка получения автомобилей в процессе покупки", clientName);
        stage = (Stage) ((Node) buttonClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client/cars_in_progress.fxml"));
        fxmlLoader.load();
        ClientInProgressController controller = fxmlLoader.getController();
        controller.start(stage, client, clientName);
    }

    public void onMyCompletedOrdersButtonClicked(ActionEvent buttonClicked) throws IOException {
        loggerMain.info("От имени Клиента {} нажата кнопка получения завершённых заказов автомобилей", clientName);
        stage = (Stage) ((Node) buttonClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client/cars_sold.fxml"));
        fxmlLoader.load();
        ClientSoldController controller = fxmlLoader.getController();
        controller.start(stage, client, clientName);
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
        client.stop_connection();
    }
}
