package com.example.javafx_project;

import com.example.javafx_project.controllers.AppController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppController controller = new AppController();
        controller.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}