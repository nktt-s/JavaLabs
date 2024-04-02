package com.example.car_dealership_client.client_controllers;

import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClientApplyController {
    @FXML
    ChoiceBox<String> type_choice;
    @FXML
    TextField applicant_name;
    @FXML
    TextField applic_text;
    @FXML
    Label error_label;
    ObservableList<String> types = FXCollections.observableArrayList();
    Client client;
    List<ApplicationData> waiting_applic;
    String name;

    public void switchToMainMenu(ActionEvent go_back_clicked) throws IOException {
        Stage stage = (Stage) ((Node) go_back_clicked.getSource()).getScene().getWindow();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/client_main.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        ClientMainController menuController = menuLoader.getController();
        menuController.prepare_main_menu(name, client);

        stage.setScene(menuScene);
        stage.show();

    }

    public void onSubmitClicked(ActionEvent onsubmcliked) throws IOException {

        if (type_choice.getValue() == null) {
            error_label.setText("Choose the type");
        } else if (applic_text.getText().isEmpty()) {
            error_label.setText("Enter application itself");
        } else if (applic_text.getText().length() > 38) {
            error_label.setText("Be concise - enter shorter text");
        } else {
            ApplicationData submitted_application = new ApplicationData(-1, "On Wait", name,
                type_choice.getValue(), "Today", applic_text.getText(), null);
            client.sendApplicationToServer(submitted_application);
            switchToMainMenu(onsubmcliked);

        }

    }

    public void prepare_applications(Client client, String name) {
        this.client = client;
        this.name = name;
        types.addAll("ResidentialConstruction", "CommercialConstruction", "Renovation", "InteriorDesign", "Landscaping", "ElectricalWork", "Plumbing", "Roofing", "Painting", "FoundationRepair", "Materials");
        type_choice.setItems(types);
        type_choice.setValue("Other");
        applic_text.requestFocus();
    }


}
