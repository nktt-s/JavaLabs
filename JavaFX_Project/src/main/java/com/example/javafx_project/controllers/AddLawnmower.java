package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.Lawnmower;
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

public class AddLawnmower {
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
    private TextField cuttingHeight;
    @FXML
    private TextField filename;

    @FXML
    private CheckBox isMulchingEnabled;
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
        powerSource.setText("battery power");
        productionYear.setText("2020");
        lifetime.setText("5");
        cuttingHeight.setText("40");
        isMulchingEnabled.setSelected(true);
        isOn.setSelected(false);
    }

    public void setFields(Lawnmower device) {
        manufacturer.setText(device.getManufacturer());
        model.setText(device.getModel());
        powerSource.setText(device.getPowerSource());
        productionYear.setText(String.valueOf(device.getProductionYear()));
        lifetime.setText(String.valueOf(device.getLifetime()));
        cuttingHeight.setText(String.valueOf(device.getCuttingHeight()));
        isMulchingEnabled.setSelected(device.isIsMulchingEnabled());
        isOn.setSelected(device.isIsOn());
    }

    @FXML
    private void onCancelButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка отмены добавления газонокосилки");
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

        Lawnmower lawnmower = new Lawnmower(_manufacturer, _model, _powerSource,
            _productionYear, _lifetime, _cuttingHeight, _isMulchingEnabled, _isOn);

        if (!lawnmower.isValidYear(_productionYear)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Год производства'");
            errorMessage_productionYear.setText("Установлено недопустимое значение " +
                "в поле 'Год производства' (" + GardeningDevice.MIN_YEAR + "-" + lawnmower.getCurrentYear() + ")");
            hasErrors = true;
        } else {
            errorMessage_productionYear.setText("");
        }
        if (!lawnmower.isValidLifetime(_lifetime)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Срок службы'");
            errorMessage_lifetime.setText("Установлено недопустимое значение " +
                "в поле 'Срок службы' (3-20 лет)");
            hasErrors = true;
        } else {
            errorMessage_lifetime.setText("");
        }
        if (!lawnmower.isValidCuttingHeight(_cuttingHeight)) {
            loggerMain.warn("Попытка ввести недопустимое значение в поле 'Высота среза'");
            errorMessage_cuttingHeight.setText("Установлено недопустимое значение " +
                "в поле 'Высота среза' (20-100 мм)");
            hasErrors = true;
        } else {
            errorMessage_cuttingHeight.setText("");
        }

        if (lawnmower.isValidYear(_productionYear) && lawnmower.isValidLifetime(_lifetime)
            && lawnmower.isValidCuttingHeight(_cuttingHeight)) hasErrors = false;
        if (!hasErrors) {
            loggerMain.info("Добавлена новая газонокосилка с ID = " + lawnmower.getId());
            DatabaseManager.addDevice(lawnmower);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }

    @FXML
    private void onImportButtonClicked() {
        loggerMain.info("Нажата кнопка импорта газонокосилки");
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
                        if (device.getType().equals("Lawnmower")) {
                            setFields((Lawnmower) device);
                            loggerMain.info("Успешный импорт газонокосилки");
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
                        if (device.getType().equals("Lawnmower")) {
                            setFields((Lawnmower) device);
                            loggerMain.info("Успешный импорт газонокосилки");
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
