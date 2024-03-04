package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.AutoWatering;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.fileManagers.BinaryFileManager;
import com.example.javafx_project.fileManagers.FileManager;
import com.example.javafx_project.fileManagers.JsonFileManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EditAutoWatering {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @FXML
    private Label errorMessage_productionYear;
    @FXML
    private Label errorMessage_lifetime;
    @FXML
    private Label errorMessage_waterPressure;
    @FXML
    private Label errorMessage_Nan;
    @FXML
    private Label fileMessage;

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
    private TextField filename;

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
    @FXML
    private Button exportButton;

    private boolean hasErrors = true;

    private AutoWatering device;

    public void start(Stage stage, GardeningDevice _device) {
        device = (AutoWatering) _device;

        AutoWatering deviceFromDB = (AutoWatering) DatabaseManager.getDevice(_device.getId());

        manufacturer.setText(Objects.requireNonNull(deviceFromDB).getManufacturer());
        model.setText(Objects.requireNonNull(deviceFromDB).getModel());
        powerSource.setText(Objects.requireNonNull(deviceFromDB).getPowerSource());
        productionYear.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getProductionYear()));
        lifetime.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getLifetime()));
        waterPressure.setText(String.valueOf(Objects.requireNonNull(deviceFromDB).getWaterPressure()));
        isSprinklerAttached.setSelected(deviceFromDB.isIsSprinklerAttached());
        isWinterMode.setSelected(deviceFromDB.isIsWinterMode());
        isOn.setSelected(deviceFromDB.isIsOn());
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка отмены изменения автополива");
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

        int _waterPressure;
        try {
            _waterPressure = Integer.parseInt(waterPressure.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            loggerMain.warn("Попытка ввести нечисловые символы в поле 'Давление воды'");
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        boolean _isSprinklerAttached = isSprinklerAttached.isSelected();
        boolean _isWinterMode = isWinterMode.isSelected();
        boolean _isOn = isOn.isSelected();

        AutoWatering autoWatering = new AutoWatering(id, _manufacturer, _model, _powerSource, _productionYear, _lifetime, _waterPressure, _isSprinklerAttached, _isWinterMode, _isOn);

        if (!autoWatering.isValidYear(_productionYear)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Год производства'");
            errorMessage_productionYear.setText("Установлено недопустимое значение " + "в поле 'Год производства' (" + GardeningDevice.MIN_YEAR + "-" + autoWatering.getCurrentYear() + ")");
            hasErrors = true;
        } else {
            errorMessage_productionYear.setText("");
        }
        if (!autoWatering.isValidLifetime(_lifetime)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Срок службы'");
            errorMessage_lifetime.setText("Установлено недопустимое значение " + "в поле 'Срок службы' (3-20 лет)");
            hasErrors = true;
        } else {
            errorMessage_lifetime.setText("");
        }
        if (!autoWatering.isValidWaterPressure(_waterPressure)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Давление воды'");
            errorMessage_waterPressure.setText("Установлено недопустимое значение " + "в поле 'Давление воды' (20-80 psi)");
            hasErrors = true;
        } else {
            errorMessage_waterPressure.setText("");
        }

        if (autoWatering.isValidYear(_productionYear) && autoWatering.isValidLifetime(_lifetime) && autoWatering.isValidWaterPressure(_waterPressure))
            hasErrors = false;
        if (!hasErrors) {
            loggerMain.info("Изменён автополив с ID = " + autoWatering.getId());
            DatabaseManager.updateDevice(autoWatering);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }

    @FXML
    public void onExportButtonClicked() {
        loggerMain.info("Нажата кнопка экспорта автополива");
        fileMessage.setText("");
        String filenameValue = filename.getText();
        switch (FileManager.getTypeOfFile(filenameValue)) {
            case 1:
                if (BinaryFileManager.writeToBinaryFile(device, filenameValue)) {
                    fileMessage.setText("Файл успешно записан!");
                    loggerMain.info("Успешный экспорт автополива");
                } else {
                    fileMessage.setText("Произошла ошибка!");
                    loggerMain.error("Ошибка при экспорте автополива");
                }
                break;

            case 2:
                if (JsonFileManager.writeToJSON(device, filenameValue)) {
                    fileMessage.setText("Файл успешно записан");
                    loggerMain.info("Успешный экспорт автополива с ID = " + device.getId());
                } else {
                    fileMessage.setText("Произошла ошибка!");
                    loggerMain.error("Ошибка при экспорте автополива");
                }
                break;

            default:
                fileMessage.setText("Расширение файла не соответствует .dat или .json");
                break;
        }
    }
}
