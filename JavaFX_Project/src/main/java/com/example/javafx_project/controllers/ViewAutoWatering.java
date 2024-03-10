package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.AutoWatering;
import com.example.javafx_project.devices.GardeningDevice;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class ViewAutoWatering {
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
    private TextField waterPressure;

    @FXML
    private CheckBox isSprinklerAttached;
    @FXML
    private CheckBox isWinterMode;
    @FXML
    private CheckBox isOn;

    @FXML
    private Button backButton;
    @FXML
    private Button startButton;

    @FXML
    private Label errorMessage;
    @FXML
    private Label statusMessage;

    private AutoWatering device;

    public void start(Stage stage, GardeningDevice _device) {
        errorMessage.setText("");
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
        disableFields();
    }

    @FXML
    private void onStartDeviceButtonClicked() {
        if (device.isIsOn()) {
            loggerMain.info("Запущен автополив с ID = " + device.getId());
            errorMessage.setText("");
            statusMessage.setText("Автополив запущен!\nИдёт полив растений...");
            backButton.setDisable(true);
            startButton.setDisable(true);
            addBlinkAnimation(startButton);
            loggerMain.info("Завершена работа газонокосилки с ID = " + device.getId());
        } else {
            loggerMain.error("Попытка запуска невключённого автополива с ID = " + device.getId());
            errorMessage.setText("Устройство ещё не включено!");
        }
    }

    private void addBlinkAnimation(Button button) {
        startButton.setText("Запущено");
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.2);
        fadeTransition.setCycleCount(10);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> {
            statusMessage.setText("Полив завершён!");
            backButton.setDisable(false);
            startButton.setDisable(false);
            startButton.setText("Запустить");
        });
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
        waterPressure.setDisable(true);
        isSprinklerAttached.setDisable(true);
        isWinterMode.setDisable(true);
        isOn.setDisable(true);
    }
}