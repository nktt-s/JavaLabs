package com.example.javafx_project.fileManagers;

import com.example.javafx_project.devices.AutoWatering;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.Lawnmower;
import com.example.javafx_project.devices.ThermalDrive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JsonFileManager {
    private static final Logger logger = LogManager.getLogger("FilesLogger");

    private static boolean isValidFileNameJSON(String filename) {
        try {
            JsonParser parser = new JsonParser();
            parser.parse(filename + ".json");
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static boolean writeToJSON(GardeningDevice device, String filename) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        File file = new File("C:/Users/nktt/IdeaProjects/JavaFX_Project/data/" + filename);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Future<Void> jsonFuture = executorService.submit(() -> {
                try (FileWriter writer = new FileWriter(file)) {
                    gson.toJson(device, writer);
                    logger.info("Успешная запись устройства с ID = " + device.getId() + "в JSON-файл: " + filename);
                } catch (IOException e) {
                    logger.error("Ошибка при записи в JSON-файл");
                }
                return null;
            });
            jsonFuture.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при выполнении записи в JSON-файл");
        } finally {
            executorService.shutdown();
        }
        return false;
    }

    public static GardeningDevice readFromJSON(File file) {
        if (file.exists()) {
            StringBuilder contentJSON = new StringBuilder();
            try {
                String line;
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                while ((line = bufferedReader.readLine()) != null) {
                    contentJSON.append(line);
                }
                String jsonString = contentJSON.toString();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                GardeningDevice jsonObject = null;

                try {
                    jsonObject = gson.fromJson(jsonString, Lawnmower.class);
                    switch (jsonObject.getType()) {
                        case "Lawnmower" -> jsonObject = gson.fromJson(jsonString, Lawnmower.class);
                        case "AutoWatering" -> jsonObject = gson.fromJson(jsonString, AutoWatering.class);
                        case "ThermalDrive" -> jsonObject = gson.fromJson(jsonString, ThermalDrive.class);
                        default ->
                            logger.error("Ошибка при чтении из JSON: содержимое JSON-файла не соответствует ни одному устройству");
                    }
                } catch (RuntimeException e) {
                    logger.error("Ошибка при чтении из JSON");
                }

                if (jsonObject != null) {
                    if (jsonObject.getType().equals("Lawnmower") || jsonObject.getType().equals("AutoWatering") || jsonObject.getType().equals("ThermalDrive")) {
                        logger.info("Информация об устройстве типа " + jsonObject.getType() + " успешно получена из JSON");
                    } else {
                        logger.error("Ошибка при чтении из JSON: данные не идентифицированы");
                    }
                    return jsonObject;
                }

            } catch (IOException e) {
                logger.error("Ошибка при чтении JSON-файла");
            }
        } else {
            logger.error("Ошибка при чтении из JSON: файл с таким именем не найден");
        }
        return null;
    }
}
