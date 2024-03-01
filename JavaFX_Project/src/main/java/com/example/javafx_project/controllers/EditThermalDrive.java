package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.AutoWatering;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.Lawnmower;
import com.example.javafx_project.devices.ThermalDrive;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EditThermalDrive {
    @FXML
    private Label errorMessage_productionYear;
    @FXML
    private Label errorMessage_lifetime;
    @FXML
    private Label errorMessage_waterPressure;
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
    private CheckBox isProtectiveFunctionOn;
    @FXML
    private CheckBox isOn;

    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;

    private boolean hasErrors = true;

    private ThermalDrive device;

    public void start(Stage stage, GardeningDevice _device) {
        device = (ThermalDrive) _device;

        ThermalDrive deviceFromDB = (ThermalDrive) DatabaseManager.getDevice(_device.getId());

        manufacturer.setText(Objects.requireNonNull(deviceFromDB).getManufacturer());
        model.setText(Objects.requireNonNull(deviceFromDB).getModel());
        powerSource.setText(Objects.requireNonNull(deviceFromDB).getPowerSource());
        productionYear.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getProductionYear()));
        lifetime.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getLifetime()));
        temperature.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getTemperature()));
        isProtectiveFunctionOn.setSelected(deviceFromDB.isIsProtectiveFunctionOn());
        isOn.setSelected(deviceFromDB.isIsOn());
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        AppController appController = new AppController();
        Scene currentScene = cancelButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        appController.start(stage);
    }

    @FXML
    private void onApplyButtonClicked() throws IOException {
        System.out.println("Apply button clicked!");
        int id = device.getId();

        String _manufacturer = manufacturer.getText();
        String _model = model.getText();
        String _powerSource = powerSource.getText();

        int _productionYear;
        try {
            _productionYear = Integer.parseInt(productionYear.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        int _lifetime;
        try {
            _lifetime = Integer.parseInt(lifetime.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        int _temperature;
        try {
            _temperature = Integer.parseInt(temperature.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        boolean _isProtectiveFunctionOn = isProtectiveFunctionOn.isSelected();
        boolean _isOn = isOn.isSelected();

        ThermalDrive thermalDrive = new ThermalDrive(id, _manufacturer, _model, _powerSource, _productionYear, _lifetime, _temperature, _isProtectiveFunctionOn, _isOn);
//        AutoWatering autoWatering = new AutoWatering(_manufacturer, _model, _powerSource, _productionYear, _lifetime, _waterPressure, _isSprinklerAttached, _isWinterMode, _isOn);

        if (!thermalDrive.isValidYear(_productionYear)) {
            errorMessage_productionYear.setText("Установлено недопустимое значение " +
                "в поле 'Год производства' (" + GardeningDevice.MIN_YEAR + "-" +
                thermalDrive.getCurrentYear() + ")");
            hasErrors = true;
        } else {
            errorMessage_productionYear.setText("");
        }
        if (!thermalDrive.isValidLifetime(_lifetime)) {
            errorMessage_lifetime.setText("Установлено недопустимое значение " +
                "в поле 'Срок службы' (3-20 лет)");
            hasErrors = true;
        } else {
            errorMessage_lifetime.setText("");
        }
        if (!thermalDrive.isValidTemperature(_temperature)) {
            errorMessage_waterPressure.setText("Установлено недопустимое значение " +
                "в поле 'Давление воды' (20-80 psi)");
            hasErrors = true;
        } else {
            errorMessage_waterPressure.setText("");
        }

        if (thermalDrive.isValidYear(_productionYear) && thermalDrive.isValidLifetime(_lifetime)
            && thermalDrive.isValidTemperature(_temperature)) hasErrors = false;
        if (!hasErrors) {
//            System.out.println("Валидация прошла успешно: ошибок нет.");
//            DatabaseManager.addDevice(thermalDrive);
            DatabaseManager.updateDevice(thermalDrive);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }
}
