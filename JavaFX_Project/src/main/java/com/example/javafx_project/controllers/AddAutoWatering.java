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

public class AddAutoWatering {
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
    private Button importButton;

    private boolean hasErrors = true;

    public void start(Stage stage) {
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

    public void setFields(AutoWatering device) {
        manufacturer.setText(device.getManufacturer());
        model.setText(device.getModel());
        powerSource.setText(device.getPowerSource());
        productionYear.setText(String.valueOf(device.getProductionYear()));
        lifetime.setText(String.valueOf(device.getLifetime()));
        waterPressure.setText(String.valueOf(device.getWaterPressure()));
        isSprinklerAttached.setSelected(device.isIsSprinklerAttached());
        isWinterMode.setSelected(device.isIsWinterMode());
        isOn.setSelected(device.isIsOn());
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка отмены добавления автополива");
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

        AutoWatering autoWatering = new AutoWatering(_manufacturer, _model, _powerSource, _productionYear, _lifetime, _waterPressure, _isSprinklerAttached, _isWinterMode, _isOn);

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
            loggerMain.info("Добавлен новый автополив с ID = " + autoWatering.getId());
            DatabaseManager.addDevice(autoWatering);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }

    @FXML
    private void onImportButtonClicked() {
        loggerMain.info("Нажата кнопка импорта автополива");
        fileMessage.setText("");
        String filenameValue = filename.getText();
        File file = new File("C:/Users/nktt/IdeaProjects/JavaFX_Project/data/" + filenameValue);
        switch (FileManager.getTypeOfFile(filenameValue)) {
            case 1:
                if (file.exists()) {
                    GardeningDevice device = BinaryFileManager.readFromBinaryFile(file);
                    if (device == null) {
                        fileMessage.setText("Ошибка при чтении файла!");
                    } else {
                        if (device.getType().equals("AutoWatering")) {
                            setFields((AutoWatering) device);
                            loggerMain.info("Успешный импорт автополива");
                        } else {
                            fileMessage.setText("В файле содержится другое устройство!");
                        }
                    }
                } else {
                    fileMessage.setText("Файл не найден!");
                }
                break;

            case 2:
                if (file.exists()) {
                    GardeningDevice device = JsonFileManager.readFromJSON(file);
                    if (device == null) {
                        fileMessage.setText("Произошла ошибка при чтении JSON-файла");
                    } else {
                        if (device.getType().equals("AutoWatering")) {
                            setFields((AutoWatering) device);
                            loggerMain.info("Успешный импорт автополива");
                        } else {
                            fileMessage.setText("В файле содержится другое устройство!");
                        }
                    }
                } else {
                    fileMessage.setText("Файл не найден!");
                }
                break;

            default:
                fileMessage.setText("Расширение файла не соответствует .dat или .json");
                break;
        }
    }
}
