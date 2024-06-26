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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

public class AppController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @FXML
    private Button addButton;
    @FXML
    private Button viewButton;
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
    private void initialize() {
        ContextMenu contextMenu_add = createContextMenu_add();
        addButton.setContextMenu(contextMenu_add);
        addButton.setOnAction(event -> showContextMenu_add());

        ContextMenu contextMenu_view = createContextMenu_view();
        viewButton.setContextMenu(contextMenu_view);
        viewButton.setOnAction(event -> showContextMenu_view());
    }

    @FXML
    private ContextMenu createContextMenu_add() {
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

    private void showContextMenu_add() {
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
    private ContextMenu createContextMenu_view() {
        loggerMain.info("Нажата кнопка просмотра устройства");
        ContextMenu contextMenu = new ContextMenu();

        contextMenu.setStyle("-fx-font-size: 15");
        MenuItem item = new MenuItem("Ввести ID");
        item.setOnAction(event -> showIDInputDialog());
        contextMenu.getItems().add(item);

        return contextMenu;
    }

    private void showIDInputDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Введите ID");
        dialog.setHeaderText(null);
        dialog.setContentText("Пожалуйста, введите ID:");

        // Создаем текстовое поле для ввода ID
        TextField idField = new TextField();

        // Создаем лейбл для отображения ошибки
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red");

        // Устанавливаем фильтр для текстового поля, чтобы разрешить только ввод цифр
        idField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                }));

        // Добавляем текстовое поле и лейбл в сетку
        GridPane grid = new GridPane();
        grid.add(idField, 0, 0);
        grid.add(errorLabel, 0, 1);

        // Устанавливаем сетку в качестве контента диалогового окна
        dialog.getDialogPane().setContent(grid);

        // Устанавливаем результат как ButtonType.OK, чтобы при нажатии Enter окно закрывалось
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return idField.getText();
            }
            return null;
        });

        // Показываем диалоговое окно и обрабатываем результат
        dialog.showAndWait().ifPresent(id -> {
            // Здесь можно обработать введенный ID
            GardeningDevice device;
            if (id.isEmpty()) {
                loggerMain.error("Введена пустая строка в поле ID устройства");
            }
            else if ((device = DatabaseManager.getDevice(Integer.parseInt(id))) != null) {
                loggerMain.info("Введён ID существующего устройства");

                String itemName = device.getType();
                String menuItem;
                switch (itemName) {
                    case "Lawnmower" -> menuItem = "Газонокосилка";
                    case "AutoWatering" -> menuItem = "Автополив";
                    case "ThermalDrive" -> menuItem = "Термопривод";
                    default -> menuItem = "ERROR";
                }

                Scene currentScene = viewButton.getScene();
                Stage stage = (Stage) currentScene.getWindow();

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view" + itemName + ".fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                    stage.setResizable(false);

                    stage.setTitle("Gardening Devices | Просмотр устройства - " + menuItem);
                    stage.setScene(scene);
                    Object temp_object = fxmlLoader.getController();
                    if (temp_object instanceof ViewLawnmower) {
                        ViewLawnmower controller = fxmlLoader.getController();
                        loggerMain.info("Нажата кнопка просмотра газонокосилки");
                        controller.start(stage, device);
                    } else if (temp_object instanceof ViewAutoWatering) {
                        ViewAutoWatering controller = fxmlLoader.getController();
                        loggerMain.info("Нажата кнопка просмотра автополива");
                        controller.start(stage, device);
                    } else {
                        ViewThermalDrive controller = fxmlLoader.getController();
                        loggerMain.info("Нажата кнопка просмотра термопривода");
                        controller.start(stage, device);
                    }
        //            stage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }



            } else {
                loggerMain.error("Введён ID несуществующего устройства");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Возникла ошибка!");
                alert.setContentText("Устройство с ID = " + id + " не найдено!");
                alert.showAndWait();
            }
        });
    }

    private void showContextMenu_view() {
        // Получаем положение кнопки на экране
        Bounds bounds = viewButton.localToScreen(viewButton.getBoundsInLocal());

        // Отображаем контекстное меню по указанным координатам
        viewButton.getContextMenu().show(viewButton, bounds.getMinX(), bounds.getMaxY());
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
                    String menuItem;
                    String itemName = device.getType();
                    switch (itemName) {
                        case "Lawnmower" -> menuItem = "Газонокосилка";
                        case "AutoWatering" -> menuItem = "Автополив";
                        case "ThermalDrive" -> menuItem = "Термопривод";
                        default -> menuItem = "ERROR";
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/images/icon2.png"));
                    alert.setTitle("Подтверждение удаления устройства");
                    alert.setHeaderText("Удаление устройства '" + menuItem + "'");
                    alert.setContentText("Вы действительно хотите безвозвратно удалить устройство с ID = " + id + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        loggerMain.info("Удаление устройства с ID = " + id);
                        DatabaseManager.deleteDevice(id);
                        updateListOfDevices(scrollPane);
                    } else {
                        loggerMain.info("Отмена удаления устройства с ID = " + id);
                        alert.close();
                    }
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