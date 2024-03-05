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

public class ViewLawnmower {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

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
    private Button backButton;
    @FXML
    private Button applyButton;
    @FXML
    private Button startDeviceButton;

    @FXML
    private Label errorMessage;

    private Lawnmower device;

    public void start(Stage stage, GardeningDevice _device) {
        errorMessage.setText("");
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
        disableFields();
    }

    @FXML
    private void onStartDeviceButtonClicked() {

        if (device.isIsOn()) {
            errorMessage.setText("");
            // TODO: Добавить имитацию функционала

        } else {
            errorMessage.setText("Устройство ещё не включено!");
        }
    }

    @FXML
    private void onBackButtonClicked() throws IOException {
        loggerMain.info("Нажата кнопка возвращения в главное меню");
        AppController appController = new AppController();
        Scene currentScene = backButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        appController.start(stage);
    }

    private void disableFields() {
        manufacturer.setDisable(true);
        model.setDisable(true);
        powerSource.setDisable(true);
        productionYear.setDisable(true);
        lifetime.setDisable(true);
        cuttingHeight.setDisable(true);
        isMulchingEnabled.setDisable(true);
        isOn.setDisable(true);
    }
}