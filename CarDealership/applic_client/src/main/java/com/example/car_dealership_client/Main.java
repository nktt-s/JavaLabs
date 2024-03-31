package com.example.car_dealership_client;

import com.example.car_dealership_client.controllers.NameEnterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import javafx.scene.image.Image;
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/car_dealership_client/name-enter.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setResizable(false);
        stage.setTitle("OCDS: Online Car Dealership System | Login page");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("dealership.png")));
        stage.getIcons().add(icon);
        NameEnterController controller = fxmlLoader.getController();
        controller.prepare_enter_name();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}