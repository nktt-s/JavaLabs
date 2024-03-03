package com.example.javafx_project;

import com.example.javafx_project.devices.GardeningDevice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinaryFileManager {
    private static final Logger logger = LogManager.getLogger("FileFilesLogger");

    public static boolean isValidFileName(String fileName) {
        String fileNameRegex = "^[^\\\\/:*?\"<>|.]*$";
        Pattern pattern = Pattern.compile(fileNameRegex);
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }

    public static boolean writeIntoBinaryFile(GardeningDevice device, String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(device);
            logger.info("Успешная запись PK=" + device.getId() + " в бинарный файл: " + fileName);
            return true;
        } catch (Exception e) {
            logger.error("Error when writing into binary file!");
        }
        return false;
    }

}
