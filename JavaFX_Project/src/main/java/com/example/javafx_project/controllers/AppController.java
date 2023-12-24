package com.example.javafx_project.controllers;

import com.example.javafx_project.App;
import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.GardeningDevice;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
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
    private TableView<GardeningDevice> listOfDevices;
    @FXML
    private ScrollPane list_scrollPane;
    @FXML
    private TableColumn<GardeningDevice, Integer> idColumn;
    @FXML
    private TableColumn<GardeningDevice, String> typeColumn;
    @FXML
    private TableColumn<GardeningDevice, Boolean> isOnColumn;
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
    private void onAddButtonClick() {

        Scene currentScene = addButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("addDevice.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setResizable(false);
            stage.setTitle("Gardening Devices | Добавление устройства");
            stage.setScene(scene);
            stage.show();


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

//        AppController controller = fxmlLoader.getController();
//        System.out.println("DEBUG INFO:\n\n" + controller);

        ArrayList<GardeningDevice> devices = DatabaseManager.getAllDevices();
        System.out.println(devices);
//
//        assert devices != null;
//        ObservableList<GardeningDevice> res_devices = FXCollections.observableArrayList(devices);
//
//        listOfDevices = new TableView<>();
//
//        TableColumn<GardeningDevice, Integer> idColumn = new TableColumn<>("ID");
//        TableColumn<GardeningDevice, String> manufacturerColumn = new TableColumn<>("Manufacturer");
//        TableColumn<GardeningDevice, String> modelColumn = new TableColumn<>("Model");
//        TableColumn<GardeningDevice, String> powerSourceColumn = new TableColumn<>("Power Source");
//        TableColumn<GardeningDevice, Integer> productionYearColumn = new TableColumn<>("Production Year");
//        TableColumn<GardeningDevice, Integer> lifetimeColumn = new TableColumn<>("Lifetime");
//        TableColumn<GardeningDevice, Boolean> isOnColumn = new TableColumn<>("Is On");

//        Назначьте соответствующие значения для каждого столбца
//        manufacturerColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getManufacturer()));
//        modelColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getModel()));
//        powerSourceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPowerSource()));
//        productionYearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProductionYear()));
//        lifetimeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLifetime()));
//        isOnColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().isOn));

//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
//        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
//        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
//        powerSourceColumn.setCellValueFactory(new PropertyValueFactory<>("powerSource"));
//        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
//        lifetimeColumn.setCellValueFactory(new PropertyValueFactory<>("lifetime"));
//        isOnColumn.setCellValueFactory(new PropertyValueFactory<>("isOn"));

//        listOfDevices.getColumns().addAll(idColumn, manufacturerColumn, modelColumn,
//                powerSourceColumn, productionYearColumn, lifetimeColumn, isOnColumn);

//        listOfDevices.setItems(res_devices);


//        listOfDevices.getColumns().add(idColumn);
//        listOfDevices.getColumns().add(manufacturerColumn);
//        listOfDevices.getColumns().add(modelColumn);
//        listOfDevices.getColumns().add(powerSourceColumn);
//        listOfDevices.getColumns().add(productionYearColumn);
//        listOfDevices.getColumns().add(lifetimeColumn);
//        listOfDevices.getColumns().add(isOnColumn);

//        list_scrollPane = new ScrollPane(listOfDevices);

//        updateListOfDevices();

        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setResizable(false);
        stage.setTitle("Gardening Devices | Список устройств");
        InputStream iconStream = getClass().getResourceAsStream("/images/icon2.png");
        assert iconStream != null;
        Image icon = new Image(iconStream);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public void updateListOfDevices() {
        try {
            ArrayList<GardeningDevice> devices = DatabaseManager.getAllDevices();

            if (devices == null) return;
            listOfDevices.getItems().clear();
//            listOfDevices = new TableView<>(res_devices);
//            listOfDevices.getItems().addAll(devices);
            for (GardeningDevice device : devices) {
//                device.update
//                GridPane gridPane = createGridPane(device.getId(), device.getType(), device.getManufacturer(),
//                    device.getModel(), device.getPowerSource(), device.getProductionYear(), device.getLifetime(), device.checkStatus());
//                listOfDevices.getChildrenUnmodifiable().add(gridPane);

                TableColumn<GardeningDevice, Integer> idColumn = new TableColumn<>("ID");
                idColumn.setCellValueFactory(new PropertyValueFactory<>(String.valueOf(device.getId())));
                listOfDevices.getColumns().add(idColumn);


                TableColumn<GardeningDevice, String> manufacturerColumn = new TableColumn<>("Manufacturer");
                TableColumn<GardeningDevice, String> modelColumn = new TableColumn<>("Model");
                TableColumn<GardeningDevice, String> powerSourceColumn = new TableColumn<>("Power Source");
                TableColumn<GardeningDevice, Integer> productionYearColumn = new TableColumn<>("Production Year");
                TableColumn<GardeningDevice, Integer> lifetimeColumn = new TableColumn<>("Lifetime");
                TableColumn<GardeningDevice, Boolean> isOnColumn = new TableColumn<>("Is On");

//                 Назначьте соответствующие значения для каждого столбца
//                manufacturerColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getManufacturer()));
//                modelColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getModel()));
//                powerSourceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPowerSource()));
//                productionYearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProductionYear()));
//                lifetimeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLifetime()));
//                isOnColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().isOn));

                // Добавьте столбцы в таблицу
//                listOfDevices.getColumns().addAll(idColumn, manufacturerColumn, modelColumn, powerSourceColumn, productionYearColumn, lifetimeColumn, isOnColumn);



                // Отобразите данные устройств в таблице
//                listOfDevices.setItems(FXCollections.observableArrayList(devices));



//                try {
//                    Parent item = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("DeviceItems.fxml")));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
//            list_scrollPane.setFitToHeight(true);
//            list_scrollPane.setFitToWidth(true);
        } catch (Exception ex) {
//            System.out.println("EXCEPTION");
            ex.printStackTrace();
        }
    }

    public GridPane createTableRow(int id, String type, String manufacturer, String model, String powerSource,
                                   int productionYear, int lifetime, boolean isOn) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("deviceItem.fxml"));
            GridPane gridPane = fxmlLoader.load();

            Button deleteDeviceButton = new Button("Удалить");

            deleteDeviceButton.setOnAction(event -> {
                DatabaseManager.deleteDevice(id);
                updateListOfDevices();
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