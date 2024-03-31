package com.example.car_dealership_client.admin_controllers;

import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdmApplicController {
    @FXML
    AnchorPane applics_anchor;
    List<ApplicationData> waiting_applic;
    List<ApplicationData> rejected_applics;
    List<ApplicationData> finished_applics;
//    Server server;
    Admin admin;
    String name;


    public void switchToMainMenu(ActionEvent go_back_clicked) throws IOException {
        Stage stage = (Stage)((Node)go_back_clicked.getSource()).getScene().getWindow();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/adm_views/adm-main.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        AdmMainController menuController = menuLoader.getController();
//        menuController.prepare_main_menu(name, client);

        stage.setScene(menuScene);
        stage.show();

    }

    public void update_applications(ActionEvent accept_clicked) throws IOException {
        Stage stage = (Stage)((Node)accept_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/adm_views/applications.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AdmApplicController applicController = loader.getController();
        applicController.prepare_applications(waiting_applic, name, admin);

        stage.setScene(scene);
        stage.show();
    }

    public void prepare_applications(List<ApplicationData> applic_data, String name, Admin admin){
        waiting_applic = applic_data;
        this.name = name;
        this.admin = admin;
        render_applications();
    }

    public void render_applications() {
        render_buttons();
        render_applic_text();
        System.out.println(waiting_applic);
    }

    public void render_applic_text(){
        double fp_top_anchor = 10;
       for (int i=0; i<waiting_applic.size();i++) {
           ApplicationData curr = waiting_applic.get(i);
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
        List<Button> accept_btn_list = new ArrayList<>();
        List<Button> reject_btn_list = new ArrayList<>();
        List<ChoiceBox<String>> worker_choicebox_list = new ArrayList<>();
        for (int i=0; i<waiting_applic.size(); i++){
            ChoiceBox<String> worker_choice = new ChoiceBox<>();
            Button acc_btn = new Button();
            Button rej_btn = new Button();
            worker_choice.setPrefWidth(150);
            acc_btn.setPrefWidth(150);
            rej_btn.setPrefWidth(150);
            worker_choice.setId("worker_choice_" + i);
            acc_btn.setId("accept_button_" + i);
            rej_btn.setId("reject_button_" + i);

            acc_btn.setOnAction(event -> {
                try {
                    if (!(worker_choice.getValue() == null)){
                        accept_application(event, acc_btn.getId(), worker_choice.getValue());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            rej_btn.setOnAction(event -> {
                try {
                    reject_application(event, acc_btn.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            acc_btn.setText("Accept");
            rej_btn.setText("Reject");
            worker_choice.getItems().addAll(AdmMainController.get_workers_list());

            accept_btn_list.add(acc_btn);
            reject_btn_list.add(rej_btn);
            worker_choicebox_list.add(worker_choice);
        }

        double top_padding = 43;
        double left_acc_padding = 175;
        double left_rej_padding = 375;
        double left_choice_padding = 10;
        for(int i=0; i<waiting_applic.size(); i++){
            AnchorPane.setTopAnchor(accept_btn_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(accept_btn_list.get(i), left_acc_padding);

            AnchorPane.setTopAnchor(worker_choicebox_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(worker_choicebox_list.get(i), left_choice_padding);

            AnchorPane.setTopAnchor(reject_btn_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(reject_btn_list.get(i), left_rej_padding);
            top_padding += 80;
        }



        applics_anchor.setCenterShape(true);
        applics_anchor.getChildren().addAll(accept_btn_list);
        applics_anchor.getChildren().addAll(reject_btn_list);
        applics_anchor.getChildren().addAll(worker_choicebox_list);
    }
    public void accept_application(ActionEvent accept_clicked, String acc_id, String worker) throws IOException {
        int acc_app_num = Integer.parseInt(acc_id.substring(acc_id.length() - 1));
        waiting_applic.get(acc_app_num).set_status("In Progress");
        waiting_applic.get(acc_app_num).set_worker(worker);
        AdmMainController.add_progress_application(waiting_applic.get(acc_app_num));
        System.out.println("After settings and adding worker:" + waiting_applic.get(acc_app_num).get_worker());
        admin.sendApplicationToServer(waiting_applic.get(acc_app_num));
        waiting_applic.remove(acc_app_num);
        update_applications(accept_clicked);
//        System.out.println(waiting_applic.get());
    }

    public void reject_application(ActionEvent cancel_clicked, String acc_id) throws IOException{
        int acc_app_num = Integer.parseInt(acc_id.substring(acc_id.length() - 1));
        waiting_applic.get(acc_app_num).set_status("Rejected");
        AdmMainController.add_closed_application(waiting_applic.get(acc_app_num));
        admin.sendApplicationToServer(waiting_applic.get(acc_app_num));
        waiting_applic.remove(acc_app_num);
        update_applications(cancel_clicked);
    }

}
