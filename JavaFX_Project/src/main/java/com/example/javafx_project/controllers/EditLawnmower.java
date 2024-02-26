package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.AutoWatering;
import com.example.javafx_project.devices.GardeningDevice;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditLawnmower {
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
    private TextField waterPressure;

    @FXML
    private CheckBox isSprinklerAttached;
    @FXML
    private CheckBox isWinterMode;
    @FXML
    private CheckBox isOn;

    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;

    private boolean hasErrors = true;

    public void start(Stage stage) {
//        manufacturer.setText(DatabaseManager.getDevice());


        manufacturer.setText("manufacturer");
        model.setText("model");
        powerSource.setText("mains power");
        productionYear.setText("2020");
        lifetime.setText("5");
        waterPressure.setText("40");
        isSprinklerAttached.setSelected(false);
        isWinterMode.setSelected(false);
        isOn.setSelected(false);
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

        int _waterPressure;
        try {
            _waterPressure = Integer.parseInt(waterPressure.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        boolean _isSprinklerAttached = isSprinklerAttached.isSelected();
        boolean _isWinterMode = isWinterMode.isSelected();
        boolean _isOn = isOn.isSelected();

        AutoWatering autoWatering = new AutoWatering(_manufacturer, _model, _powerSource,
            _productionYear, _lifetime, _waterPressure, _isSprinklerAttached, _isWinterMode, _isOn);

        if (!autoWatering.isValidYear(_productionYear)) {
            errorMessage_productionYear.setText("Установлено недопустимое значение " +
                "в поле 'Год производства' (" + GardeningDevice.MIN_YEAR + "-" + autoWatering.getCurrentYear() + ")");
            hasErrors = true;
        } else {
            errorMessage_productionYear.setText("");
        }
        if (!autoWatering.isValidLifetime(_lifetime)) {
            errorMessage_lifetime.setText("Установлено недопустимое значение " +
                "в поле 'Срок службы' (3-20 лет)");
            hasErrors = true;
        } else {
            errorMessage_lifetime.setText("");
        }
        if (!autoWatering.isValidWaterPressure(_waterPressure)) {
            errorMessage_waterPressure.setText("Установлено недопустимое значение " +
                "в поле 'Давление воды' (20-80 psi)");
            hasErrors = true;
        } else {
            errorMessage_waterPressure.setText("");
        }

        if (autoWatering.isValidYear(_productionYear) && autoWatering.isValidLifetime(_lifetime)
            && autoWatering.isValidWaterPressure(_waterPressure)) hasErrors = false;
        if (!hasErrors) {
//            System.out.println("Валидация прошла успешно: ошибок нет.");
            DatabaseManager.addDevice(autoWatering);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }
}
