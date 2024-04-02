package com.example.applic_server;

import com.example.applic_server.controllers.SerMainController;
import com.example.applic_server.models.DataBaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Main extends Application {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @Override
    public void start(Stage stage) throws IOException {
        loggerMain.info("Программа запущена");
        SerMainController controller = new SerMainController();
        controller.connect();
        controller.get_workers();
        // controller.start_menu();
    }

    public static void main(String[] args) {
        configureDB();
        launch();
    }

    public static void configureDB() {
        Properties properties = new Properties();
        HashMap<String, String> configInfo = new HashMap<>();

        try {
            File file = new File("C:/Users/nktt/IdeaProjects/CarDealership/applic_server/src/main/resources/application.properties");
            properties.load(new FileReader(file));

            configInfo.put("databaseName", properties.getProperty("databaseName"));
            configInfo.put("databaseUser", properties.getProperty("databaseUser"));
            configInfo.put("databasePassword", properties.getProperty("databasePassword"));


        } catch (IOException e) {
            loggerMain.error("Ошибка в методе configureDB()");
            e.printStackTrace();
        }
        DataBaseHandler.serValuesForConnection(configInfo.get("databaseName"), configInfo.get("databaseUser"), configInfo.get("databasePassword"));
    }
}