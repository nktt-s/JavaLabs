package com.example.javafx_project.fileManagers;

import com.example.javafx_project.devices.AutoWatering;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.Lawnmower;
import com.example.javafx_project.devices.ThermalDrive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinaryFileManager {
    private static final Logger logger = LogManager.getLogger("FilesLogger");

    public static boolean isValidFileName(String fileName) {
        String fileNameRegex = "^[^\\\\/:*?\"<>|.]*$";
        Pattern pattern = Pattern.compile(fileNameRegex);
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }

    public static boolean writeToBinaryFile(GardeningDevice device, String fileName) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> result = executorService.submit(() -> {
            File file = new File("C:/Users/nktt/IdeaProjects/JavaFX_Project/data/" + fileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(device);
                logger.info("Успешная запись устройства с ID = " + device.getId() + " в бинарный файл: " + fileName);
                return true;
            } catch (IOException e) {
                logger.error("Ошибка при записи в бинарный файл");
                return false;
            }
        });

        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при записи в бинарный файл");
            return false;
        } finally {
            executorService.shutdown();
        }
    }

        public static GardeningDevice readFromBinaryFile(File file) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            GardeningDevice device = (GardeningDevice) objectInputStream.readObject();
            if (device instanceof Lawnmower || device instanceof AutoWatering || device instanceof ThermalDrive) {
                logger.info("Чтение из бинарного файла: информация об устройстве типа " + device.getType() + " успешно получена");
                return device;
            } else {
                logger.error("Чтение из бинарного файла: информация получена, но объект не идентифицирован");
            }
        } catch (ClassNotFoundException | IOException e) {
            logger.error("Чтение из бинарного файла: произошла ошибка чтения файла");
//            e.printStackTrace();
        }
        return null;
    }

        public static byte[] getByteData(GardeningDevice device) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(device);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("Ошибка во время получения двоичных файлов");
//            e.printStackTrace();
        }
        return null;
    }

}
