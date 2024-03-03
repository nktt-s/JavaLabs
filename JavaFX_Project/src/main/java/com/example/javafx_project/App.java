package com.example.javafx_project;

import com.example.javafx_project.controllers.AppController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class App extends Application {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @Override
    public void start(Stage stage) throws IOException {
        loggerMain.info("Программа запущена");
        AppController controller = new AppController();
        controller.start(stage);
    }

    public static void main(String[] args) {
        configureDB();
        launch();
    }

    private static void configureDB() {
        Properties properties = new Properties();
        HashMap<String, String> configInfo = new HashMap<>();

        try {
            File file = new File("C:/Users/nktt/IdeaProjects/JavaFX_Project/application.properties");
            properties.load(new FileReader(file));

            configInfo.put("databaseName", properties.getProperty("databaseName"));
            configInfo.put("databaseUser", properties.getProperty("databaseUser"));
            configInfo.put("databasePassword", properties.getProperty("databasePassword"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        DatabaseManager.setValuesForConnection(configInfo.get("databaseName"), configInfo.get("databaseUser"), configInfo.get("databasePassword"));
    }
}