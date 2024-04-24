package com.example.car_dealership_client.admin_controllers;

import com.example.car_dealership_client.models.Car;
import com.example.car_dealership_client.models.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class EditCar {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @FXML
    private Label errorMessage_productionYear;
    @FXML
    private Label errorMessage_Nan;

    @FXML
    private TextField seller;
    @FXML
    private TextField buyer;
    @FXML
    private TextField manufacturer;
    @FXML
    private TextField model;
    @FXML
    private TextField color;
    @FXML
    private TextField productionYear;

    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;

    private Car car;

    private String tableName;
    private String controllerName;

    public void start(Stage stage, Car _car, String _tableName) {
        car = _car;
        tableName = _tableName;

        Car carFromDB = DatabaseManager.getCar(_car.getId(), tableName);
        if (carFromDB == null) {
            loggerMain.error("Ошибка при получении данных автомобиля");
            return;
        }

        seller.setText(Objects.requireNonNull(carFromDB).getSeller());
        if (tableName.equals("AllStockCars")) {
            buyer.setText("Пока отсутствует");
            buyer.setDisable(true);
        } else {
            buyer.setText(Objects.requireNonNull(carFromDB).getBuyer());
        }
        manufacturer.setText(Objects.requireNonNull(carFromDB).getManufacturer());
        model.setText(Objects.requireNonNull(carFromDB).getModel());
        color.setText(Objects.requireNonNull(carFromDB).getColor());
        productionYear.setText(String.valueOf(Objects.requireNonNull(carFromDB).getProductionYear()));

        switch (tableName) {
            case "AllStockCars" -> controllerName = "AdminInStockController";
            case "AllInProgressCars" -> controllerName = "AdminInProgressController";
            default -> controllerName = null;
        }
    }

    @FXML
    private void onCancelButtonClicked() {
        loggerMain.info("Нажата кнопка отмены изменения автомобиля");
        try {
            Class<?> controllerClass = Class.forName("com.example.car_dealership_client.admin_controllers." + controllerName);
            Object temp_object = controllerClass.getDeclaredConstructor().newInstance();

            if (temp_object instanceof Initializable) {
                ((Initializable) temp_object).initialize(null, null);
            }

            if (temp_object instanceof AdminInStockController adminInStockController) {
                Scene currentScene = cancelButton.getScene();
                Stage stage = (Stage) currentScene.getWindow();
                adminInStockController.start(stage);
            } else if (temp_object instanceof AdminInProgressController adminInProgressController) {
                Scene currentScene = cancelButton.getScene();
                Stage stage = (Stage) currentScene.getWindow();
                adminInProgressController.start(stage);
            }

        } catch (Exception e) {
            loggerMain.error("Ошибка в EditCar при нажатии кнопки CancelButton");
        }
    }

    @FXML
    private void onApplyButtonClicked() {
        int id = car.getId();

        String _seller = seller.getText();
        String _buyer;
        if (tableName.equals("AllStockCars")) {
            _buyer = null;
        } else {
            _buyer = buyer.getText();
        }
        String _manufacturer = manufacturer.getText();
        String _model = model.getText();
        String _color = color.getText();

        int _productionYear;
        boolean hasErrors = true;
        try {
            _productionYear = Integer.parseInt(productionYear.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            loggerMain.warn("Попытка ввести нечисловые символы в поле 'Год производства'");
            errorMessage_Nan.setText("Проверьте числовые значения!");
            return;
        }

        Car car = new Car(id, _seller, _buyer, _manufacturer, _model, _color, _productionYear);

        if (car.isValidYear(_productionYear)) {
            errorMessage_productionYear.setText("");
            hasErrors = false;
        } else {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Год производства'");
            errorMessage_productionYear.setText("Установлено недопустимое значение " + "в поле 'Год производства' (" + Car.MIN_YEAR + "-" + car.getCurrentYear() + ")");
        }

        if (!hasErrors) {
            loggerMain.info("Изменён автомобиль с ID = {}", car.getId());
            DatabaseManager.updateCar(car, tableName);

            try {
                Class<?> controllerClass = Class.forName("com.example.car_dealership_client.admin_controllers." + controllerName);
                Object temp_object = controllerClass.getDeclaredConstructor().newInstance();

                if (temp_object instanceof Initializable) {
                    ((Initializable) temp_object).initialize(null, null);
                }

                if (temp_object instanceof AdminInStockController adminInStockController) {
                    Scene currentScene = cancelButton.getScene();
                    Stage stage = (Stage) currentScene.getWindow();
                    adminInStockController.start(stage);
                } else if (temp_object instanceof AdminInProgressController adminInProgressController) {
                    Scene currentScene = cancelButton.getScene();
                    Stage stage = (Stage) currentScene.getWindow();
                    adminInProgressController.start(stage);
                }

            } catch (Exception e) {
                loggerMain.error("Ошибка в EditCar при нажатии кнопки ApplyButton");
            }
        }
    }
}
