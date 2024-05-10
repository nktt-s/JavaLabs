package com.example.car_dealership_client.seller_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.client_controllers.ClientMainController;
import com.example.car_dealership_client.models.Car;
import com.example.car_dealership_client.models.DatabaseManager;
import com.example.car_dealership_client.models.Seller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class SellerSoldController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car, Integer> idColumn;
    @FXML
    private TableColumn<Car, String> buyerColumn;
    @FXML
    private TableColumn<Car, String> manufacturerColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, String> colorColumn;
    @FXML
    private TableColumn<Car, Integer> productionYearColumn;

    private static Seller seller;
    private static String sellerName;

    String tableName = "AllSoldCars";

    public void start(Stage stage, Seller seller, String sellerName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/cars_sold.fxml"));
        Parent root = fxmlLoader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        SellerSoldController.seller = seller;
        SellerSoldController.sellerName = sellerName;
        updateCars(scrollPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setResizable(false);
        stage.setTitle("OCDS: Online Car Dealership System | My completed orders page");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainMenu(ActionEvent backButton) throws IOException {
        loggerMain.info("Возвращение в главное меню продавца {}", sellerName);
        Stage stage = (Stage) ((Node) backButton.getSource()).getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/seller_main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        SellerMainController controller = fxmlLoader.getController();
        controller.prepareMainMenu(seller);
        stage.setTitle("OCDS: Online Car Dealership System | Seller Main page");

        stage.setScene(scene);
        stage.show();
    }

    public void updateCars(ScrollPane scrollPane) {
        loggerMain.info("Запущен метод обновления таблицы завершённых заказов продавца {}", sellerName);
        ArrayList<Car> carsFromDB = DatabaseManager.getAllCarsBySeller(tableName, sellerName);

        // TODO Добавить в эту таблицу дату завершённого заказа

        if (carsFromDB == null) return;
        ObservableList<Car> cars = FXCollections.observableArrayList(carsFromDB);

        table = new TableView<>(cars);

        table.setStyle("-fx-font-size: 16px;");
        table.setPrefWidth(950);
        table.setPrefHeight(550);

        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(30.0);
        table.getColumns().add(idColumn);

        buyerColumn = new TableColumn<>("Buyer");
        buyerColumn.setCellValueFactory(new PropertyValueFactory<>("buyer"));
        buyerColumn.setPrefWidth(100.0);
        table.getColumns().add(buyerColumn);

        manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        manufacturerColumn.setPrefWidth(140.0);
        table.getColumns().add(manufacturerColumn);

        modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelColumn.setPrefWidth(85.0);
        table.getColumns().add(modelColumn);

        colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setPrefWidth(85.0);
        table.getColumns().add(colorColumn);

        productionYearColumn = new TableColumn<>("Production year");
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        productionYearColumn.setPrefWidth(150.0);
        table.getColumns().add(productionYearColumn);

        scrollPane.setContent(table);
    }
}
