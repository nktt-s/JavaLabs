package com.example.car_dealership_client.admin_controllers;

import com.example.car_dealership_client.models.Car;
import com.example.car_dealership_client.models.DatabaseManager;
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
    private Label fileMessage;

    @FXML
    private TextField seller;
    @FXML
    private TextField manufacturer;
    @FXML
    private TextField model;
    @FXML
    private TextField color;
    @FXML
    private TextField productionYear;
    @FXML
    private TextField filename;

    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;
    @FXML
    private Button exportButton;

    private Car car;

    public void start(Stage stage, Car _car) {
        car = _car;

        Car carFromDB = DatabaseManager.getCar(_car.getId());

        seller.setText(Objects.requireNonNull(carFromDB).getSeller());
        manufacturer.setText(Objects.requireNonNull(carFromDB).getManufacturer());
        model.setText(Objects.requireNonNull(carFromDB).getModel());
        color.setText(Objects.requireNonNull(carFromDB).getColor());
        productionYear.setText(String.valueOf(Objects.requireNonNull(carFromDB).getProductionYear()));
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка отмены изменения автополива");
        AdminInStockController adminInStockController = new AdminInStockController();
        Scene currentScene = cancelButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        adminInStockController.start(stage);
    }

    @FXML
    private void onApplyButtonClicked() throws IOException {
        int id = car.getId();

        String _seller = seller.getText();
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

        Car car = new Car(id, _seller, _manufacturer, _model, _color, _productionYear);

        if (car.isValidYear(_productionYear)) {
            errorMessage_productionYear.setText("");
            hasErrors = false;
        } else {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Год производства'");
            errorMessage_productionYear.setText("Установлено недопустимое значение " + "в поле 'Год производства' (" + Car.MIN_YEAR + "-" + car.getCurrentYear() + ")");
        }

        if (!hasErrors) {
            loggerMain.info("Изменён автомобиль с ID = {}", car.getId());
            DatabaseManager.updateCar(car);

            AdminInStockController adminInStockController = new AdminInStockController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            adminInStockController.start(stage);
        }
    }

    @FXML
    public void onExportButtonClicked() {
//        loggerMain.info("Нажата кнопка экспорта автополива");
//        fileMessage.setText("");
//        String filenameValue = filename.getText();
//        switch (FileManager.getTypeOfFile(filenameValue)) {
//            case 1:
//                if (BinaryFileManager.writeToBinaryFile(car, filenameValue)) {
//                    fileMessage.setText("Файл успешно записан!");
//                    loggerMain.info("Успешный экспорт автополива");
//                } else {
//                    fileMessage.setText("Произошла ошибка!");
//                    loggerMain.error("Ошибка при экспорте автополива");
//                }
//                break;
//
//            case 2:
//                if (JsonFileManager.writeToJSON(car, filenameValue)) {
//                    fileMessage.setText("Файл успешно записан");
//                    loggerMain.info("Успешный экспорт автополива с ID = " + car.getId());
//                } else {
//                    fileMessage.setText("Произошла ошибка!");
//                    loggerMain.error("Ошибка при экспорте автополива");
//                }
//                break;
//
//            default:
//                fileMessage.setText("Расширение файла не соответствует .dat или .json");
//                break;
//        }
    }
}
