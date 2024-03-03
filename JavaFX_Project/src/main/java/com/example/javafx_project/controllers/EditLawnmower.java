package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.Lawnmower;
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
import java.util.Objects;

public class EditLawnmower {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @FXML
    private Label errorMessage_productionYear;
    @FXML
    private Label errorMessage_lifetime;
    @FXML
    private Label errorMessage_cuttingHeight;
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
    private TextField cuttingHeight;

    @FXML
    private CheckBox isMulchingEnabled;
    @FXML
    private CheckBox isOn;

    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;

    private boolean hasErrors = true;

    private Lawnmower device;

    public void start(Stage stage, GardeningDevice _device) {
        device = (Lawnmower) _device;

        Lawnmower deviceFromDB = (Lawnmower) DatabaseManager.getDevice(_device.getId());

        manufacturer.setText(Objects.requireNonNull(deviceFromDB).getManufacturer());
        model.setText(Objects.requireNonNull(deviceFromDB).getModel());
        powerSource.setText(Objects.requireNonNull(deviceFromDB).getPowerSource());
        productionYear.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getProductionYear()));
        lifetime.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getLifetime()));
        cuttingHeight.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getCuttingHeight()));
        isMulchingEnabled.setSelected(deviceFromDB.isIsMulchingEnabled());
        isOn.setSelected(deviceFromDB.isIsOn());
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка отмены изменения газонокосилки");
        AppController appController = new AppController();
        Scene currentScene = cancelButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        appController.start(stage);
    }

    @FXML
    private void onApplyButtonClicked() throws IOException {
        int id = device.getId();

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

        int _cuttingHeight;
        try {
            _cuttingHeight = Integer.parseInt(cuttingHeight.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            loggerMain.warn("Попытка ввести нечисловые символы в поле 'Высота среза'");
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        boolean _isMulchingEnabled = isMulchingEnabled.isSelected();
        boolean _isOn = isOn.isSelected();

        Lawnmower lawnmower = new Lawnmower(id, _manufacturer, _model, _powerSource, _productionYear, _lifetime, _cuttingHeight, _isMulchingEnabled, _isOn);

        if (!lawnmower.isValidYear(_productionYear)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Год производства'");
            errorMessage_productionYear.setText("Установлено недопустимое значение " + "в поле 'Год производства' (" + GardeningDevice.MIN_YEAR + "-" + lawnmower.getCurrentYear() + ")");
            hasErrors = true;
        } else {
            errorMessage_productionYear.setText("");
        }
        if (!lawnmower.isValidLifetime(_lifetime)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Срок службы'");
            errorMessage_lifetime.setText("Установлено недопустимое значение " + "в поле 'Срок службы' (3-20 лет)");
            hasErrors = true;
        } else {
            errorMessage_lifetime.setText("");
        }
        if (!lawnmower.isValidCuttingHeight(_cuttingHeight)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Высота среза'");
            errorMessage_cuttingHeight.setText("Установлено недопустимое значение " + "в поле 'Высота среза' (20-100 мм)");
            hasErrors = true;
        } else {
            errorMessage_cuttingHeight.setText("");
        }

        if (lawnmower.isValidYear(_productionYear) && lawnmower.isValidLifetime(_lifetime) && lawnmower.isValidCuttingHeight(_cuttingHeight))
            hasErrors = false;
        if (!hasErrors) {
            loggerMain.info("Изменена газонокосилка с ID = " + lawnmower.getId());
            DatabaseManager.updateDevice(lawnmower);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }
}