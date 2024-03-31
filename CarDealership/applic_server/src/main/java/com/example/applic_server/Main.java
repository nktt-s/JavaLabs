package com.example.applic_server;

import com.example.applic_server.controllers.SerMainController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SerMainController controller = new SerMainController();
        controller.connect();
        controller.get_workers();
        //controller.start_menu();

    }

    public static void main(String[] args) {
        launch();
    }
}