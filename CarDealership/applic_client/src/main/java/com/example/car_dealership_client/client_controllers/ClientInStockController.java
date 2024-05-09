package com.example.car_dealership_client.client_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.models.Car;
import com.example.car_dealership_client.models.Client;
import com.example.car_dealership_client.models.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ClientInStockController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car, Integer> idColumn;
    @FXML
    private TableColumn<Car, String> sellerColumn;
    @FXML
    private TableColumn<Car, String> manufacturerColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, String> colorColumn;
    @FXML
    private TableColumn<Car, Integer> productionYearColumn;
    @FXML
    private TableColumn<Car, Button> requestColumn;

    private static Client client;
    private static String clientName;

    String tableName = "AllStockCars";

    public void start(Stage stage, Client client, String clientName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client/cars_in_stock.fxml"));
        Parent root = fxmlLoader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        ClientInStockController.client = client;
        ClientInStockController.clientName = clientName;
        updateCars(scrollPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setResizable(false);
        stage.setTitle("OCDS: Online Car Dealership System | Cars in stock page");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainMenu(ActionEvent onBackClicked) throws IOException {
        Stage stage = (Stage) ((Node) onBackClicked.getSource()).getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client/client_main.fxml"));
        Parent menuRoot = fxmlLoader.load();
        Scene menuScene = new Scene(menuRoot);
        ClientMainController controller = fxmlLoader.getController();
        controller.prepareMainMenu(clientName, client);
        stage.setTitle("OCDS: Online Car Dealership System | Client Main page");

        stage.setScene(menuScene);
        stage.show();
    }

    public void updateCars(ScrollPane scrollPane) {
        loggerMain.info("Запущен метод обновления таблицы автомобилей в наличии");
        ArrayList<Car> carsFromDB = DatabaseManager.getAllCars(tableName);

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

        sellerColumn = new TableColumn<>("Seller");
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("seller"));
        sellerColumn.setPrefWidth(100.0);
        table.getColumns().add(sellerColumn);

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

        requestColumn = new TableColumn<>("Request");
        requestColumn.setCellFactory(param -> new TableCell<>() {
            private final Button requestButton = new Button("Request");

            {
                requestButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    int id = car.getId();
                    String manufacturer = car.getManufacturer();
                    String model = car.getModel();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("dealership.png"))));

                    alert.setTitle("Подтверждение запроса на покупку автомобиля");
                    alert.setHeaderText("Покупка автомобиля '" + manufacturer + " " + model + "'");
                    alert.setContentText("Вы действительно хотите создать запрос на покупку автомобиля с ID = " + id + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        loggerMain.info("Создание запроса на покупку автомобиля с ID = {}", id);
                        DatabaseManager.moveCarFromStockToInProgress(id, clientName);
                        updateCars(scrollPane);
                    } else {
                        loggerMain.info("Отмена создания запроса на покупку автомобиля с ID = {}", id);
                        alert.close();
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(requestButton);
                }
            }
        });
        requestColumn.setPrefWidth(90.0);
        table.getColumns().add(requestColumn);
        scrollPane.setContent(table);
    }
}