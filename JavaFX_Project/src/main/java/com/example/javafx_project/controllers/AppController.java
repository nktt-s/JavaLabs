package com.example.javafx_project.controllers;

import com.example.javafx_project.App;
import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.Lawnmower;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AppController {
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button exitButton;
    @FXML
    private TableView<GardeningDevice> table;
    @FXML
    private TableColumn<GardeningDevice, Integer> idColumn;
    @FXML
    private TableColumn<GardeningDevice, String> typeColumn;
    @FXML
    private TableColumn<GardeningDevice, String> manufacturerColumn;
    @FXML
    private TableColumn<GardeningDevice, String> modelColumn;
    @FXML
    private TableColumn<GardeningDevice, String> powerSourceColumn;
    @FXML
    private TableColumn<GardeningDevice, Integer> productionYearColumn;
    @FXML
    private TableColumn<GardeningDevice, Integer> lifetimeColumn;
    @FXML
    private TableColumn<GardeningDevice, Boolean> isOnColumn;

    public Button addLawnmower;
    public Button addAutoWatering;
    public Button addThermalDrive;

    @FXML
    private void initialize() {
        ContextMenu contextMenu = createContextMenu();
        addButton.setContextMenu(contextMenu);
        addButton.setOnAction(event -> showContextMenu());
    }

    @FXML
    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        contextMenu.setStyle("-fx-font-size: 15");
        MenuItem item1 = new MenuItem("Газонокосилка");
        MenuItem item2 = new MenuItem("Автополив");
        MenuItem item3 = new MenuItem("Термопривод");

        contextMenu.getItems().addAll(item1, item2, item3);

        item1.setOnAction(this::handleMenuItemClick);
        item2.setOnAction(this::handleMenuItemClick);
        item3.setOnAction(this::handleMenuItemClick);

        return contextMenu;
    }

    private void showContextMenu() {
        // Получаем положение кнопки на экране
        Bounds bounds = addButton.localToScreen(addButton.getBoundsInLocal());

        // Отображаем контекстное меню по указанным координатам
        addButton.getContextMenu().show(addButton, bounds.getMinX(), bounds.getMaxY());
    }

    @FXML
    private void handleMenuItemClick(ActionEvent event) {
        String menuItem = ((MenuItem) event.getSource()).getText();
        String itemName;
        switch (menuItem) {
            case "Газонокосилка" -> itemName = "Lawnmower";
            case "Автополив" -> itemName = "AutoWatering";
            case "Термопривод" -> itemName = "ThermalDrive";
            default -> itemName = "ERROR";
        }
        Scene currentScene = addButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add" + itemName + ".fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setResizable(false);
            stage.setTitle("Gardening Devices | Добавление устройства - " + menuItem);
            stage.setScene(scene);
            Object temp_object = fxmlLoader.getController();
            if (temp_object instanceof AddLawnmower) {
                AddLawnmower controller = fxmlLoader.getController();
                controller.start(stage);
            } else if (temp_object instanceof AddAutoWatering) {
                AddAutoWatering controller = fxmlLoader.getController();
                controller.start(stage);
            } else {
                AddThermalDrive controller = fxmlLoader.getController();
                controller.start(stage);
            }
//            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onEditButtonClick() {

    }
    @FXML
    private void onDeleteButtonClick() {

    }
    @FXML
    private void onViewButtonClick() {

    }
    @FXML
    private void onExitButtonClick() {
        Platform.exit();
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainWindow.fxml"));
        Parent root = fxmlLoader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        updateListOfDevices(scrollPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setResizable(false);
        stage.setTitle("Gardening Devices | Список устройств");
        InputStream iconStream = getClass().getResourceAsStream("/images/icon2.png");
        assert iconStream != null;
        Image icon = new Image(iconStream);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public void updateListOfDevices(ScrollPane scrollPane) {
            ArrayList<GardeningDevice> devicesFromDB = DatabaseManager.getAllDevices();

            if (devicesFromDB == null) return;
            ObservableList<GardeningDevice> devices = FXCollections.observableArrayList(devicesFromDB);

            table = new TableView<>(devices);
            table.setStyle("-fx-font-size: 14px");
            table.setPrefWidth(950);
            table.setPrefHeight(550);

            idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setPrefWidth(50.0);
            table.getColumns().add(idColumn);

            typeColumn = new TableColumn<>("Type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            typeColumn.setPrefWidth(100.0);
            table.getColumns().add(typeColumn);

            manufacturerColumn = new TableColumn<>("Manufacturer");
            manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
            manufacturerColumn.setPrefWidth(130.0);
            table.getColumns().add(manufacturerColumn);

            modelColumn = new TableColumn<>("Model");
            modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
            modelColumn.setPrefWidth(85.0);
            table.getColumns().add(modelColumn);

            powerSourceColumn = new TableColumn<>("Power source");
            powerSourceColumn.setCellValueFactory(new PropertyValueFactory<>("powerSource"));
            powerSourceColumn.setPrefWidth(130.0);
            table.getColumns().add(powerSourceColumn);

            productionYearColumn = new TableColumn<>("Production year");
            productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
            productionYearColumn.setPrefWidth(125.0);
            table.getColumns().add(productionYearColumn);

            lifetimeColumn = new TableColumn<>("Lifetime");
            lifetimeColumn.setCellValueFactory(new PropertyValueFactory<>("lifetime"));
            lifetimeColumn.setPrefWidth(100.0);
            table.getColumns().add(lifetimeColumn);

            isOnColumn = new TableColumn<>("Is switched on");
            isOnColumn.setCellValueFactory(new PropertyValueFactory<>("isOn"));
            isOnColumn.setPrefWidth(135.0);
            table.getColumns().add(isOnColumn);

            scrollPane.setContent(table);
    }

    public GridPane createTableRow(int id, String type, String manufacturer, String model, String powerSource,
                                   int productionYear, int lifetime, boolean isOn) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("deviceItem.fxml"));
            GridPane gridPane = fxmlLoader.load();

            Button deleteDeviceButton = new Button("Удалить");

            deleteDeviceButton.setOnAction(event -> {
                DatabaseManager.deleteDevice(id);
                updateListOfDevices(new ScrollPane());
            });

            GridPane.setConstraints(deleteDeviceButton, 0, 0);
            gridPane.getChildren().add(deleteDeviceButton);

            Button configureDeviceButton = new Button("Изменить");
//            configureDeviceButton.setOnAction(event -> {
//                GardeningDevice device = DatabaseManager.getDevice(id);
//                switch (Objects.requireNonNull(device).getType()) {
//                    case "Lawnmower": {
//                        Scene currentScene = addLawnmower.getScene();
//                        Stage stage = (Stage) currentScene.getWindow();
//                        try {
//                            FXMLLoader loader = new FXMLLoader(App.class.getResource("editLawnmower.fxml"));
//                            Parent root = loader.load();
//                            stage.setScene(new Scene(root));
//                            EditLawnmower controller = loader.getController();
//                            controller.start(stage, device);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    }
//                    case "AutoWatering": {
//                        Scene currentScene = addAutoWatering.getScene();
//                        Stage stage = (Stage) currentScene.getWindow();
//                        try {
//                            FXMLLoader loader = new FXMLLoader(App.class.getResource("editAutoWatering.fxml"));
//                            Parent root = loader.load();
//                            stage.setScene(new Scene(root));
//                            EditAutoWatering controller = loader.getController();
//                            controller.start(stage, device);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    }
//                    case "ThermalDrive": {
//                        Scene currentScene = addThermalDrive.getScene();
//                        Stage stage = (Stage) currentScene.getWindow();
//                        try {
//                            FXMLLoader loader = new FXMLLoader(App.class.getResource("configureHeater.fxml"));
//                            Parent root = loader.load();
//                            stage.setScene(new Scene(root));
//                            EditThermalDrive controller = loader.getController();
//                            controller.start(stage, device);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
            GridPane.setConstraints(configureDeviceButton, 0, 1);
            gridPane.getChildren().add(configureDeviceButton);

            Label labelType = new Label(type);
            GridPane.setConstraints(labelType, 1, 0);
            gridPane.getChildren().add(labelType);

            Label labelManufacturer = new Label(manufacturer);
            GridPane.setConstraints(labelManufacturer, 2, 0);
            gridPane.getChildren().add(labelManufacturer);

            Label labelModel = new Label(model);
            GridPane.setConstraints(labelModel, 3, 0);
            gridPane.getChildren().add(labelModel);

            Label status;
            if (isOn) {
                status = new Label("Включено");
            } else {
                status = new Label("Отключено");
            }
            GridPane.setConstraints(status, 4, 0);
            gridPane.getChildren().add(status);

            return gridPane;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}