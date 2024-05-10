package com.example.car_dealership_client.seller_controllers;

import com.example.car_dealership_client.models.Car;
import com.example.car_dealership_client.models.DatabaseManager;
import com.example.car_dealership_client.models.Seller;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class EditCar {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @FXML
    private Label errorMessage_productionYear;
    @FXML
    private Label errorMessage_Nan;

    @FXML
    private TextField sellerField;
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
    private Seller seller;
    private String sellerName;
    private final String tableName = "AllStockCars";

    public void start(Stage stage, Car _car, Seller _seller, String _sellerName) {
        car = _car;
        seller = _seller;
        sellerName = _sellerName;

        Car carFromDB = DatabaseManager.getCar(_car.getId(), tableName);
        if (carFromDB == null) {
            loggerMain.error("Ошибка при получении данных автомобиля");
            return;
        }

        sellerField.setText(Objects.requireNonNull(carFromDB).getSeller());
        sellerField.setDisable(true);
        buyer.setText("Пока отсутствует");
        buyer.setDisable(true);
        manufacturer.setText(Objects.requireNonNull(carFromDB).getManufacturer());
        model.setText(Objects.requireNonNull(carFromDB).getModel());
        color.setText(Objects.requireNonNull(carFromDB).getColor());
        productionYear.setText(String.valueOf(Objects.requireNonNull(carFromDB).getProductionYear()));
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка отмены изменения автомобиля");
        SellerInStockController sellerInStockController = new SellerInStockController();
        Scene currentScene = cancelButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        sellerInStockController.start(stage, seller, sellerName);
    }

    @FXML
    private void onApplyButtonClicked() throws IOException {
        int id = car.getId();

        String _seller = sellerField.getText();
        String _buyer = null;
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
            SellerInStockController sellerInStockController = new SellerInStockController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            sellerInStockController.start(stage, seller, sellerName);
        }
    }
}
