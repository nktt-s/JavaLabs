package com.example.javafx_project.controllers;

import com.example.javafx_project.DatabaseManager;
import com.example.javafx_project.devices.Lawnmower;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class AddLawnmower {
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

        int _cuttingHeight;
        try {
            _cuttingHeight = Integer.parseInt(cuttingHeight.getText());
            errorMessage_Nan.setText("");
        } catch (NumberFormatException e) {
            errorMessage_Nan.setText("Проверьте числовые значения!");
            hasErrors = true;
            return;
        }

        boolean _isMulchingEnabled = isMulchingEnabled.isSelected();
        boolean _isOn = isOn.isSelected();

        Lawnmower lawnmower = new Lawnmower(_manufacturer, _model, _powerSource,
            _productionYear, _lifetime, _cuttingHeight, _isMulchingEnabled, _isOn);

        if (!lawnmower.isValidYear(_productionYear)) {
            errorMessage_productionYear.setText("Установлено недопустимое значение " +
                "в поле 'Год производства' (2000-" + lawnmower.getCurrentYear() + ")");
            hasErrors = true;
        } else {
            errorMessage_productionYear.setText("");
        }
        if (!lawnmower.isValidLifetime(_lifetime)) {
            errorMessage_lifetime.setText("Установлено недопустимое значение " +
                "в поле 'Срок службы' (3-20 лет)");
            hasErrors = true;
        } else {
            errorMessage_lifetime.setText("");
        }
        if (!lawnmower.isValidCuttingHeight(_cuttingHeight)) {
            errorMessage_cuttingHeight.setText("Установлено недопустимое значение " +
                "в поле 'Высота среза' (20-100 мм)");
            hasErrors = true;
        } else {
            errorMessage_cuttingHeight.setText("");
        }

        if (lawnmower.isValidYear(_productionYear) && lawnmower.isValidLifetime(_lifetime)
            && lawnmower.isValidCuttingHeight(_cuttingHeight)) hasErrors = false;
        if (!hasErrors) {
//            System.out.println("Валидация прошла успешно: ошибок нет.");
            DatabaseManager.addDevice(lawnmower);

            AppController appController = new AppController();
            Scene currentScene = cancelButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            appController.start(stage);
        }
    }
}
