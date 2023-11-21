import java.io.*;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class JSONFileHandler {
    protected static final Logger logger = LogManager.getLogger(JSONFileHandler.class);

    private static boolean isValidFileName(String fileName){
        return fileName.matches("^[a-zA-Z0-9_.-]+$");
}

    public static void saveDeviceToJSON(GardeningDeviceManager deviceManager, Scanner scanner) {
        logger.info("Запуск действия - сохранение данных об устройстве в JSON.");
        if (deviceManager.getDevices().isEmpty()) {
            logger.warn("Попытка сохранения объекта в JSON из пустого списка.");
            System.out.println(ANSI_RED + "Ошибка - отсутствуют устройства для сохранения в JSON!" + ANSI_RESET);
            return;
        }
        int id;
        System.out.print("Введите значение ID устройства: ");
        if (scanner.hasNextInt()) {
            id = scanner.nextInt();
            if (deviceManager.getDeviceByID(id) == null) {
                logger.error("Попытка ввода неверного значения ID устройства.");
                System.out.println(ANSI_RED + "Введено неверное значение ID.\n" + ANSI_RESET);
                return;
            }
        } else {
            logger.warn("Попытка ввода недопустимого значения ID устройства.");
            System.out.println(ANSI_RED + "Введено недопустимое значение. Установлено значение по умолчанию (1)." + ANSI_RESET);
            scanner.nextLine();
            id = 1;
        }
        scanner.nextLine();

        System.out.print("Введите имя выходного файла JSON: ");
        String fileName = scanner.nextLine();

        if (isValidFileName(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File file;
            if (fileName.endsWith(".json")) {
                file = new File("C:/Users/nktt/IdeaProjects/Java_LR5_Maven/json/", fileName);
            } else {
                file = new File("C:/Users/nktt/IdeaProjects/Java_LR5_Maven/json/", fileName + ".json");
            }

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(deviceManager.getDeviceByID(id), writer);
                logger.info("Данные об устройстве успешно сохранены в JSON.");
                System.out.println(ANSI_GREEN + "Данные об устройстве успешно сохранены.\n" + ANSI_RESET);
            } catch (IOException ex) {
                logger.error("Ошибка при сохранении данных устройства в JSON.");
                System.out.println(ANSI_RED + "Ошибка при сохранении данных устройства в JSON\n" + ANSI_RESET);
            }
        } else {
            logger.warn("Попытка ввода неверного имени для выходного файла.");
            System.out.println(ANSI_RED + "Введено неверное имя выходного файла!" + ANSI_RESET);
        }
    }

    public static void loadDeviceFromJSON(GardeningDeviceManager deviceManager, Scanner scanner) {
        logger.info("Запуск действия - загрузка устройства из файла JSON.");

        scanner.nextLine();
        System.out.print("Введите имя файла JSON: ");
        String fileName = scanner.nextLine();

        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }
        File file = new File("C:/Users/nktt/IdeaProjects/Java_LR5_Maven/json/", fileName);

        if (file.exists()) {
            StringBuilder json = new StringBuilder();

            try {
                String line;
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                while ((line = bufferedReader.readLine()) != null) {
                    json.append(line);
                }
                String jsonString = json.toString();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                GardeningDevice jsonObject = null;


                try {
                    jsonObject = gson.fromJson(jsonString, Lawnmower.class);
                    switch (jsonObject.getClass().toString().substring(6)) {
                        case "Lawnmower" -> jsonObject = gson.fromJson(jsonString, Lawnmower.class);
                        case "AutoWatering" -> jsonObject = gson.fromJson(jsonString, AutoWatering.class);
                        case "ThermalDrive" -> jsonObject = gson.fromJson(jsonString, ThermalDrive.class);
                        default -> {
                            logger.error("Содержимое JSON не соответствует ни одному устройству.");
                            System.out.println(ANSI_RED + "Содержимое JSON не соответствует ни одному устройству." + ANSI_RESET);
                        }
                    }
                } catch (RuntimeException ex) {
                    logger.error("Ошибка в ходе работы с JSON.");
                }

                if (jsonObject != null) {
                    jsonObject.setId();
                    deviceManager.addDevice(jsonObject);
                    System.out.print(ANSI_GREEN + "Устройство '");
                    switch (jsonObject.getClass().toString().substring(6)) {
                        case "Lawnmower" -> System.out.print("Газонокосилка'");
                        case "AutoWatering" -> System.out.print("Автополив'");
                        case "ThermalDrive" -> System.out.print("Термопривод'");
                    }
                    logger.info("Данные об устройстве успешно получены.");
                    System.out.println(" успешно добавлено!" + ANSI_RESET);
                    deviceManager.showObjects();
                }

            } catch (IOException ex) {
                logger.error("Ошибка при чтении файла.");
                System.out.println(ANSI_RED + "Ошибка при чтении файла." + ANSI_RESET);
            }
        } else {
            logger.error("Попытка чтения несуществующего файла.");
            System.out.println(ANSI_RED + "Ошибка - файл с таким именем не существует!" + ANSI_RESET);
        }
    }




    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
}
