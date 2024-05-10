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

public class SellerInStockController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @FXML
    private Button addButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car, Integer> idColumn;
    @FXML
    private TableColumn<Car, String> manufacturerColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, String> colorColumn;
    @FXML
    private TableColumn<Car, Integer> productionYearColumn;
    @FXML
    private TableColumn<Car, Button> editColumn;
    @FXML
    private TableColumn<Car, Button> deleteColumn;

    private static Seller seller;
    private static String sellerName;

    String tableName = "AllStockCars";

    public void start(Stage stage, Seller seller, String sellerName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/cars_in_stock.fxml"));
        Parent root = fxmlLoader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        SellerInStockController.seller = seller;
        SellerInStockController.sellerName = sellerName;
        updateCars(scrollPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setResizable(false);
        stage.setTitle("OCDS: Online Car Dealership System | Cars in stock page");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainMenu(ActionEvent onBackClicked) throws IOException {
        loggerMain.info("Возвращение в главное меню продавца {}", sellerName);
        Stage stage = (Stage) ((Node) onBackClicked.getSource()).getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/seller_main.fxml"));
        Parent menuRoot = fxmlLoader.load();
        Scene menuScene = new Scene(menuRoot);
        SellerMainController controller = fxmlLoader.getController();
        controller.prepareMainMenu(sellerName, seller);
        stage.setTitle("OCDS: Online Car Dealership System | Seller Main page");

        stage.setScene(menuScene);
        stage.show();
    }

    @FXML
    private void addButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/add_car.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        Scene currentScene = addButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();

        stage.setResizable(false);
        stage.setTitle("OCDS: Online Car Dealership System | Add car page");
        stage.setScene(scene);
        AddCar controller = fxmlLoader.getController();
        loggerMain.info("Нажата кнопка добавления автомобиля");
        controller.start(stage, seller, sellerName);
    }

    public void updateCars(ScrollPane scrollPane) {
        loggerMain.info("Запущен метод обновления таблицы автомобилей в наличии");
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

        editColumn = new TableColumn<>("Edit");
        editColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    Scene currentScene = editButton.getScene();
                    Stage stage = (Stage) currentScene.getWindow();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/edit_car.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                        stage.setResizable(false);
                        stage.setTitle("OCDS: Online Car Dealership System | Edit car page");
                        stage.setScene(scene);
                        EditCar controller = fxmlLoader.getController();
                        loggerMain.info("Нажата кнопка изменения автомобиля с ID = {}", car.getId());
                        controller.start(stage, car, seller, sellerName);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    updateCars(scrollPane);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
        editColumn.setPrefWidth(70.0);
        table.getColumns().add(editColumn);

        deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    int id = car.getId();
                    String manufacturer = car.getManufacturer();
                    String model = car.getModel();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("dealership.png"))));

                    alert.setTitle("Подтверждение удаления автомобиля");
                    alert.setHeaderText("Удаление автомобиля '" + manufacturer + " " + model + "'");
                    alert.setContentText("Вы действительно хотите безвозвратно удалить автомобиль с ID = " + id + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        loggerMain.info("Удаление автомобиля с ID = {}", id);
                        DatabaseManager.deleteCar(id, tableName);
                        updateCars(scrollPane);
                    } else {
                        loggerMain.info("Отмена удаления автомобиля с ID = {}", id);
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
                    setGraphic(deleteButton);
                }
            }
        });
        deleteColumn.setPrefWidth(90.0);
        table.getColumns().add(deleteColumn);
        scrollPane.setContent(table);
    }
}