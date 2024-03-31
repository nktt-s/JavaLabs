package com.example.car_dealership_client.seller_controllers;

import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Worker;
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

public class SellerJobsController {
    List<ApplicationData> jobs_applics;
    @FXML
    AnchorPane applics_anchor;
    private List<ApplicationData> progress_applics;
    private String name;
    private Worker worker;


    public void switchToMainMenu(ActionEvent go_back_clicked) throws IOException {
        Stage stage = (Stage)((Node)go_back_clicked.getSource()).getScene().getWindow();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/seller_views/work-main.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        WorkerMainController menuController = menuLoader.getController();
//        menuController.connect();
        menuController.prepare_main_menu(worker, jobs_applics);

        stage.setScene(menuScene);
        stage.show();

    }
    public void prepare_jobs(List<ApplicationData> progress_applics, String name, Worker worker){
        this.jobs_applics = progress_applics;
//        System.out.println("Progress applics inflitrated: " + this.jobs_applics);
        this.name = name;
        this.worker = worker;
        render_applications();
    }


        public void update_applications(ActionEvent event) throws IOException {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/car_dealership_client/seller_views/work-jobs.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            SellerJobsController applicController = loader.getController();
            applicController.prepare_jobs(jobs_applics, name, worker);

            stage.setScene(scene);
            stage.show();
        }
        public void render_applications(){
        render_buttons();
        render_applic_text();
    }

    public void render_applic_text(){
        double fp_top_anchor = 10;
        for (ApplicationData curr : jobs_applics) {
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
        for (int i=0; i<jobs_applics.size(); i++){
            Button fin_btn = new Button();
            fin_btn.setPrefWidth(150);
            fin_btn.setId("finish_button_" + i);

            fin_btn.setOnAction(event -> {
                try {
                    finish_application(event, fin_btn.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fin_btn.setText("Finish job");

            finish_btn_list.add(fin_btn);
        }

        double top_padding = 43;
        double left_acc_padding = 230;
        for(int i=0; i<jobs_applics.size(); i++){
            AnchorPane.setTopAnchor(finish_btn_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(finish_btn_list.get(i), left_acc_padding);

            top_padding += 80;
        }

        applics_anchor.setCenterShape(true);
        applics_anchor.getChildren().addAll(finish_btn_list);
    }
    public void finish_application(ActionEvent accept_clicked, String acc_id) throws IOException {
        int acc_app_num = Integer.parseInt(acc_id.substring(acc_id.length() - 1));
        jobs_applics.get(acc_app_num).set_status("Finished");
//        System.out.println("Job about to finish: " + jobs_applics.get(acc_app_num).get_status());
        worker.sendApplicationToServer(jobs_applics.get(acc_app_num));
        jobs_applics.remove(acc_app_num);
//        WorkerMainController.update_all_applications(jobs_applics);
//        WorkerMainController.delete_application(acc_app_num);
        update_applications(accept_clicked);
    }

}


