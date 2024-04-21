package com.example.car_dealership_client;

import com.example.car_dealership_client.controllers.NameEnterController;
import com.example.car_dealership_client.models.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
//    System.setProperty("log4j.configurationFile","C:/Users/nktt/IdeaProjects/CarDealership/applic_client/src/main/resources/log4j2.xml");
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");

    @Override
    public void start(Stage stage) throws IOException {
        loggerMain.info("Программа запущена");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/car_dealership_client/login_page.fxml"));
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
        configureDB();
        launch();
    }

    private static void configureDB() {
        Properties properties = new Properties();
        HashMap<String, String> configInfo = new HashMap<>();

        try {
            File file = new File("C:/Users/nktt/IdeaProjects/CarDealership/applic_client/src/main/resources/application.properties");
            properties.load(new FileReader(file));

            configInfo.put("databaseName", properties.getProperty("databaseName"));
            configInfo.put("databaseUser", properties.getProperty("databaseUser"));
            configInfo.put("databasePassword", properties.getProperty("databasePassword"));

        } catch (IOException e) {
            loggerMain.error("Error in configureDB() method");
        }
        DatabaseManager.setValuesForConnection(configInfo.get("databaseName"), configInfo.get("databaseUser"), configInfo.get("databasePassword"));
    }
}