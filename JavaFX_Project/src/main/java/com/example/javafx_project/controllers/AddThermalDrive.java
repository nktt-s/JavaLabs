package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.ThermalDrive;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AddThermalDrive {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @FXML
    private Label errorMessage_productionYear;
    @FXML
    private Label errorMessage_lifetime;
    @FXML
    private Label errorMessage_temperature;
    @FXML
    private Label errorMessage_Nan;

    @FXML
    private TextField manufacturer;
    @FXML
    private TextField model;
    @FXML
    private TextField powerSource;
    @FXML
    private TextField productionYear;
    @FXML
    private TextField lifetime;
    @FXML
    private TextField temperature;

    @FXML
    private CheckBox protectiveFunction;
    @FXML
    private CheckBox isOn;

    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;

    private boolean hasErrors = true;

    public void start(Stage stage) {
        manufacturer.setText("manufacturer");
        model.setText("model");
        powerSource.setText("mains power");
        productionYear.setText("2020");
        lifetime.setText("5");
        temperature.setText("20");
        protectiveFunction.setSelected(true);
        isOn.setSelected(false);
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка отмены добавления термопривода");
        AppController appController = new AppController();
        Scene currentScene = cancelButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        appController.start(stage);
    }

    @FXML
    private void onApplyButtonClicked() throws IOException {
        String _manufacturer = manufacturer.getText();
        String _model = model.getText();
        String _powerSource = powerSource.getText();

        int _productionYear;
        try {
            _productionYear = Integer.parseInt(productionYear.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            loggerMain.warn("Попытка ввести нечисловые символы в поле 'Год производства'");
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        int _lifetime;
        try {
            _lifetime = Integer.parseInt(lifetime.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            loggerMain.warn("Попытка ввести нечисловые символы в поле 'Срок службы'");
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        int _temperature;
        try {
            _temperature = Integer.parseInt(temperature.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            loggerMain.warn("Попытка ввести нечисловые символы в поле 'Температура'");
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        boolean _protectiveFunction = protectiveFunction.isSelected();
        boolean _isOn = isOn.isSelected();

        ThermalDrive thermalDrive = new ThermalDrive(_manufacturer, _model, _powerSource, _productionYear, _lifetime, _temperature, _protectiveFunction, _isOn);

        if (!thermalDrive.isValidYear(_productionYear)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Год производства'");
            errorMessage_productionYear.setText("Установлено недопустимое значение " + "в поле 'Год производства' (" + GardeningDevice.MIN_YEAR + "-" + thermalDrive.getCurrentYear() + ")");
            hasErrors = true;
        } else {
            errorMessage_productionYear.setText("");
        }
        if (!thermalDrive.isValidLifetime(_lifetime)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Срок службы'");
            errorMessage_lifetime.setText("Установлено недопустимое значение " + "в поле 'Срок службы' (3-20 лет)");
            hasErrors = true;
        } else {
            errorMessage_lifetime.setText("");
        }
        if (!thermalDrive.isValidTemperature(_temperature)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Температура'");
            errorMessage_temperature.setText("Установлено недопустимое значение " + "в поле 'Установленная температура' (5-30°C)");
            hasErrors = true;
        } else {
            errorMessage_temperature.setText("");
        }

        if (thermalDrive.isValidYear(_productionYear) && thermalDrive.isValidLifetime(_lifetime) && thermalDrive.isValidTemperature(_temperature))
            hasErrors = false;
        if (!hasErrors) {
            loggerMain.info("Добавлен новый термопривод с ID = " + thermalDrive.getId());
            DatabaseManager.addDevice(thermalDrive);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }
}
