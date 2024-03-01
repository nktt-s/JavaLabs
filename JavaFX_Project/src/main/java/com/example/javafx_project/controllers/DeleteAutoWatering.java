package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class DeleteAutoWatering {
    @FXML
    private Label confirmationMessage;
    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;

    private int id;

    public void start(Stage stage, int _id) {
        id = _id;
        confirmationMessage.setText("Вы действительно хотите безвозвратно удалить устройство с ID = " + id + "?");
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

        DatabaseManager.deleteDevice(id);

        AppController appController = new AppController();
        Scene currentScene = cancelButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        appController.start(stage);
    }
}
