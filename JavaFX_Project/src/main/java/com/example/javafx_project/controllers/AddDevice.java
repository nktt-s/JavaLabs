package com.example.javafx_project.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddDevice {

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
    private TextField cuttingHeight;
    @FXML
    private TextField waterPressure;

    @FXML
    private CheckBox protectiveFunction;
    @FXML
    private CheckBox isMulchingEnabled;
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


    public void start(Stage stage) {
        manufacturer.setText("manufacturer");
        model.setText("model");
        powerSource.setText("powerSource");
        productionYear.setText("productionYear");
        productionYear.setText("productionYear");
        lifetime.setText("lifetime");
        temperature.setText("temperature");
        cuttingHeight.setText("cuttingHeight");




    }



    @FXML
    private void onCancelButtonClicked() throws IOException {
        AppController appController = new AppController();
        Scene currentScene = cancelButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        appController.start(stage);
    }

    @FXML
    private void onApplyButtonClicked() {

    }

}
