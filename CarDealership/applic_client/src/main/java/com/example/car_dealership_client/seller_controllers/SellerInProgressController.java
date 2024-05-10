package com.example.car_dealership_client.seller_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.models.Car;
import com.example.car_dealership_client.models.DatabaseManager;
import com.example.car_dealership_client.models.Seller;
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

public class SellerInProgressController {
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
    @FXML
    private TableColumn<Car, Button> rejectColumn;
    @FXML
    private TableColumn<Car, Button> confirmColumn;

    private static Seller seller;
    private static String sellerName;

    String tableName = "AllInProgressCars";

    public void start(Stage stage, Seller seller, String sellerName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/cars_in_progress.fxml"));
        Parent root = fxmlLoader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        SellerInProgressController.seller = seller;
        SellerInProgressController.sellerName = sellerName;
        updateCars(scrollPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setResizable(false);
        stage.setTitle("OCDS: Online Car Dealership System | My active orders page");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainMenu(ActionEvent backButton) throws IOException {
        loggerMain.info("Возвращение в главное меню клиента {}", sellerName);
        Stage stage = (Stage) ((Node) backButton.getSource()).getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/seller_main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        SellerMainController controller = fxmlLoader.getController();
        controller.prepareMainMenu(sellerName, seller);
        stage.setTitle("OCDS: Online Car Dealership System | Seller Main page");

        stage.setScene(scene);
        stage.show();
    }

    public void updateCars(ScrollPane scrollPane) {
        loggerMain.info("Запущен метод обновления таблицы автомобилей продавца {} в процессе продажи", sellerName);
        ArrayList<Car> carsFromDB = DatabaseManager.getAllCarsBySeller(tableName, sellerName);

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

        rejectColumn = new TableColumn<>("Reject");
        rejectColumn.setCellFactory(param -> new TableCell<>() {
            private final Button rejectButton = new Button("Reject");

            {
                rejectButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    int id = car.getId();
                    String manufacturer = car.getManufacturer();
                    String model = car.getModel();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("dealership.png"))));

                    alert.setTitle("Отклонение заказа");
                    alert.setHeaderText("Отклонение заказа на продажу автомобиля '" + manufacturer + " " + model + "'");
                    alert.setContentText("Вы действительно хотите отклонить заказ на продажу автомобиля с ID = " + id + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        loggerMain.info("Отклонение заказа на продажу автомобиля с ID = {}", id);
                        DatabaseManager.moveCarFromInProgressToStock(id);
                        updateCars(scrollPane);
                    } else {
                        loggerMain.info("Отмена отклонения заказа на продажу автомобиля с ID = {}", id);
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
                    setGraphic(rejectButton);
                }
            }
        });
        rejectColumn.setPrefWidth(90.0);
        table.getColumns().add(rejectColumn);

        confirmColumn = new TableColumn<>("Confirm");
        confirmColumn.setCellFactory(param -> new TableCell<>() {
            private final Button confirmButton = new Button("Confirm");

            {
                confirmButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    int id = car.getId();
                    String manufacturer = car.getManufacturer();
                    String model = car.getModel();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("dealership.png"))));

                    alert.setTitle("Подтверждение заказа");
                    alert.setHeaderText("Подтверждение заказа на продажу автомобиля '" + manufacturer + " " + model + "'");
                    alert.setContentText("Вы действительно хотите подтвердить заказ на продажу автомобиля с ID = " + id + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        loggerMain.info("Подтверждение заказа на продажу автомобиля с ID = {}", id);
                        DatabaseManager.moveCarFromInProgressToSold(id);
                        updateCars(scrollPane);
                    } else {
                        loggerMain.info("Отмена подтверждения заказа на продажу автомобиля с ID = {}", id);
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
                    setGraphic(confirmButton);
                }
            }
        });
        confirmColumn.setPrefWidth(90.0);
        table.getColumns().add(confirmColumn);

        scrollPane.setContent(table);
    }
}