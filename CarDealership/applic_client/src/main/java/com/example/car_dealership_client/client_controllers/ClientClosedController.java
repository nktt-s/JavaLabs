package com.example.car_dealership_client.client_controllers;

import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientClosedController {
    @FXML
    AnchorPane applics_anchor;
    List<ApplicationData> closed_applics;
    Client client;

    public void switchToMainMenu(ActionEvent go_back_clicked) throws IOException {
        Stage stage = (Stage)((Node)go_back_clicked.getSource()).getScene().getWindow();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/client_main.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        ClientMainController menuController = menuLoader.getController();
        menuController.prepare_main_menu(client);

        stage.setScene(menuScene);
        stage.show();

    }

    public void update_applications(ActionEvent accept_clicked) throws IOException {
        Stage stage = (Stage)((Node)accept_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/closed.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ClientProgressController controller = loader.getController();
        controller.prepare_applications(client, closed_applics);

        stage.setScene(scene);
        stage.show();
    }

    public void prepare_applications(Client client,List<ApplicationData> applic_data){
        this.client = client;
        closed_applics = applic_data;
        render_applications();
    }

    public void render_applications() {
        render_applic_text();
    }

    public void render_applic_text(){
        double fp_top_anchor = 10;
        for (int i=0; i<closed_applics.size();i++) {
            ApplicationData curr = closed_applics.get(i);
            FlowPane fp = new FlowPane(0, 10);
            fp.setPrefWidth(600);
            List<Label> lbl_list = new ArrayList<>();
            lbl_list.add(new Label(" " + curr.get_status()));
            lbl_list.add(new Label(" " + curr.get_name()));
            lbl_list.add(new Label(" " + curr.get_type()));
            lbl_list.add(new Label(" " + curr.get_date()));
            lbl_list.forEach(lbl -> {
                lbl.setPrefWidth(70);
                lbl.setStyle("-fx-border-color: grey;");
            });
            lbl_list.add(new Label(" " + curr.get_text()));
            lbl_list.get(4).setPrefWidth(300);

            lbl_list.forEach(lbl -> {
                lbl.setStyle("-fx-border-color: grey;");
                lbl.setPrefHeight(30);
            });

            fp.getChildren().addAll(lbl_list);
            AnchorPane.setTopAnchor(fp, fp_top_anchor);
            fp_top_anchor += 40;
            AnchorPane.setLeftAnchor(fp, 10.0);
            applics_anchor.getChildren().addAll(fp);
        }
    }


}
