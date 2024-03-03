package com.example.javafx_project.controllers;

import com.example.javafx_project.App;
import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.GardeningDevice;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AppController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
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
    private ScrollPane scrollPane;
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
    @FXML
    private TableColumn<GardeningDevice, Button> editColumn;
    @FXML
    private TableColumn<GardeningDevice, Button> deleteColumn;

    @FXML
    private Label errorMessage_on_edit;

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
                loggerMain.info("Нажата кнопка добавления газонокосилки");
                controller.start(stage);
            } else if (temp_object instanceof AddAutoWatering) {
                AddAutoWatering controller = fxmlLoader.getController();
                loggerMain.info("Нажата кнопка добавления автополива");
                controller.start(stage);
            } else {
                AddThermalDrive controller = fxmlLoader.getController();
                loggerMain.info("Нажата кнопка добавления термопривода");
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
        loggerMain.info("Нажата кнопка выхода из программы");
        Platform.exit();
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainWindow.fxml"));
        Parent root = fxmlLoader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
//        scrollPane = new ScrollPane();
        updateListOfDevices(scrollPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setResizable(false);
        double centerX = (screenWidth - 1000) / 2;
        double centerY = (screenHeight - 600) / 2;
        stage.setX(centerX);
        stage.setY(centerY);
        stage.setTitle("Gardening Devices | Список устройств");
        InputStream iconStream = getClass().getResourceAsStream("/images/icon2.png");
        assert iconStream != null;
        Image icon = new Image(iconStream);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public void updateListOfDevices(ScrollPane scrollPane) {
        loggerMain.info("Обновлён список устройств");
        ArrayList<GardeningDevice> devicesFromDB = DatabaseManager.getAllDevices();

        if (devicesFromDB == null) return;
        ObservableList<GardeningDevice> devices = FXCollections.observableArrayList(devicesFromDB);

        table = new TableView<>(devices);

        table.setStyle("-fx-font-size: 14px");
        table.setPrefWidth(950);
        table.setPrefHeight(550);

        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(30.0);
        table.getColumns().add(idColumn);

        typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(100.0);
        table.getColumns().add(typeColumn);

        manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        manufacturerColumn.setPrefWidth(110.0);
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
        lifetimeColumn.setPrefWidth(80.0);
        table.getColumns().add(lifetimeColumn);

        isOnColumn = new TableColumn<>("Is switched on");
        isOnColumn.setCellValueFactory(new PropertyValueFactory<>("isOn"));
        isOnColumn.setPrefWidth(100.0);
        table.getColumns().add(isOnColumn);

        editColumn = new TableColumn<>("Edit");
        editColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    GardeningDevice device = getTableView().getItems().get(getIndex());
                    // ЛОГИКА ПРИ НАЖАТИИ КНОПКИ

                    Scene currentScene = editButton.getScene();
                    Stage stage = (Stage) currentScene.getWindow();

                    String menuItem;
                    String itemName = device.getType();
                    switch (itemName) {
                        case "Lawnmower" -> menuItem = "Газонокосилка";
                        case "AutoWatering" -> menuItem = "Автополив";
                        case "ThermalDrive" -> menuItem = "Термопривод";
                        default -> menuItem = "ERROR";
                    }

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("edit" + itemName + ".fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                        stage.setResizable(false);
                        stage.setTitle("Gardening Devices | Изменение устройства - " + menuItem);
                        stage.setScene(scene);
                        Object temp_object = fxmlLoader.getController();
                        if (temp_object instanceof EditLawnmower) {
                            EditLawnmower controller = fxmlLoader.getController();
                            loggerMain.info("Нажата кнопка изменения газонокосилки с ID = " + device.getId());
                            controller.start(stage, device);
                        } else if (temp_object instanceof EditAutoWatering) {
                            EditAutoWatering controller = fxmlLoader.getController();
                            loggerMain.info("Нажата кнопка изменения автополива с ID = " + device.getId());
                            controller.start(stage, device);
                        } else {
                            EditThermalDrive controller = fxmlLoader.getController();
                            loggerMain.info("Нажата кнопка изменения термопривода с ID = " + device.getId());
                            controller.start(stage, device);
                        }
//                        stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Edit button clicked! ID = " + device.getId());
                    updateListOfDevices(scrollPane);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
        editColumn.setPrefWidth(50.0);
        table.getColumns().add(editColumn);

        deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    GardeningDevice device = getTableView().getItems().get(getIndex());
                    int id = device.getId();
                    // ЛОГИКА ПРИ НАЖАТИИ КНОПКИ ДЛЯ ДЕЙСТВИЯ

                    Scene currentScene = deleteButton.getScene();
                    Stage stage = (Stage) currentScene.getWindow();

                    String menuItem;
                    String itemName = device.getType();
                    switch (itemName) {
                        case "Lawnmower" -> menuItem = "Газонокосилка";
                        case "AutoWatering" -> menuItem = "Автополив";
                        case "ThermalDrive" -> menuItem = "Термопривод";
                        default -> menuItem = "ERROR";
                    }

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("delete" + itemName + ".fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 600, 350);
                        stage.setResizable(false);
                        double centerX = (screenWidth - 600) / 2;
                        double centerY = (screenHeight - 350) / 2;
                        stage.setX(centerX);
                        stage.setY(centerY);
                        stage.setTitle("Gardening Devices | Удаление устройства - " + menuItem);
                        stage.setScene(scene);
                        Object temp_object = fxmlLoader.getController();
                        if (temp_object instanceof DeleteLawnmower) {
                            DeleteLawnmower controller = fxmlLoader.getController();
                            loggerMain.info("Нажата кнопка удаления газонокосилки с ID = " + id);
                            controller.start(stage, id);
                        } else if (temp_object instanceof DeleteAutoWatering) {
                            DeleteAutoWatering controller = fxmlLoader.getController();
                            loggerMain.info("Нажата кнопка удаления автополива с ID = " + id);
                            controller.start(stage, id);
                        } else {
                            DeleteThermalDrive controller = fxmlLoader.getController();
                            loggerMain.info("Нажата кнопка удаления термопривода с ID = " + id);
                            controller.start(stage, id);
                        }
//                        stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }



                    System.out.println("Delete button clicked! ID = " + device.getId());
                    updateListOfDevices(scrollPane);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        deleteColumn.setPrefWidth(70.0);
        table.getColumns().add(deleteColumn);
        scrollPane.setContent(table);
    }
}