package com.example.car_dealership_client.admin_controllers;

import com.example.car_dealership_client.models.Admin;
import com.example.car_dealership_client.models.ApplicationData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdmClosedController {
    @FXML
    AnchorPane applics_anchor;
    List<ApplicationData> closed_applics;
    Admin admin;

    public void switchToMainMenu(ActionEvent go_back_clicked) throws IOException {
        Stage stage = (Stage)((Node)go_back_clicked.getSource()).getScene().getWindow();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/adm_main.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
//        SerMainController menuController = menuLoader.getController();
//        menuController.connect();
//        menuController.prepare_main_menu(waiting_applic, rejected_applics, finished_applics);

        stage.setScene(menuScene);
        stage.show();

    }

    public void update_applications(ActionEvent accept_clicked) throws IOException {
        Stage stage = (Stage)((Node)accept_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/cars_sold.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AdmClosedController controller = loader.getController();
        controller.prepare_applications(closed_applics, admin);

        stage.setScene(scene);
        stage.show();
    }

    public void prepare_applications(List<ApplicationData> applic_data, Admin admin){
        this.admin = admin;
        closed_applics = applic_data;
        render_applications();
    }

    public void render_applications() {
//        render_buttons();
        render_applic_text();
    }

    public void render_applic_text(){
        double fp_top_anchor = 10;
        for (ApplicationData curr : closed_applics) {
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
            fp_top_anchor += 80;
            AnchorPane.setLeftAnchor(fp, 10.0);
            applics_anchor.getChildren().addAll(fp);
        }
    }


    public void render_buttons(){
        List<Button> finish_btn_list = new ArrayList<>();
        for (int i=0; i<closed_applics.size(); i++){
            Button fin_btn = new Button();
            fin_btn.setPrefWidth(150);
            fin_btn.setId("finish_button_" + i);

            fin_btn.setOnAction(event -> {
                try {
                    clear_application(event, fin_btn.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fin_btn.setText("Clear");

            finish_btn_list.add(fin_btn);
        }

        double top_padding = 43;
        double left_acc_padding = 430;
        for(int i=0; i<closed_applics.size(); i++){
            AnchorPane.setTopAnchor(finish_btn_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(finish_btn_list.get(i), left_acc_padding);

            top_padding += 80;
        }



        applics_anchor.setCenterShape(true);
        applics_anchor.getChildren().addAll(finish_btn_list);
    }
    public void clear_application(ActionEvent accept_clicked, String acc_id) throws IOException {
        int acc_app_num = Integer.parseInt(acc_id.substring(acc_id.length() - 1));
        closed_applics.get(acc_app_num).set_status("Cleared");
        admin.sendApplicationToServer(closed_applics.get(acc_app_num));
        closed_applics.remove(acc_app_num);
        AdmMainController.update_closed_applications(closed_applics);
        update_applications(accept_clicked);
    }


}
