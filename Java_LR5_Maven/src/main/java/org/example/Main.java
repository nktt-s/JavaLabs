package org.example;

import java.util.Scanner;
import java.util.InputMismatchException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {
    protected static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Начало работы программы.");
        GardeningDeviceManager deviceManager = getGardeningDeviceManager();

        System.out.println(ANSI_BLUE + "\n\t\t\t=== Лабораторная работа #5 ===\n\t\tВыполнил студент группы ИКПИ-14 Сергеев Н.В.\n" + ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        while (true) {
            showMenu();
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        logger.info("Запуск действия - просмотр устройств.");
                        deviceManager.showObjects();
                        break;

                    case 2:
                        addDevice(deviceManager);
                        break;

                    case 3:
                        editDevice(deviceManager);
                        break;

                    case 4:
                        removeDevice(deviceManager);
                        break;

                    case 5:
                        operateDevices(deviceManager, sc);
                        break;

                    case 6:
                        deviceManager.showObjects();
                        JSONFileHandler.saveDeviceToJSON(deviceManager, sc);
                        break;

                    case 7:
                        JSONFileHandler.loadDeviceFromJSON(deviceManager, sc);
                        break;

                    case 0:
                        logger.info("Завершение работы программы.");
                        System.out.println("Завершение работы...");
                        return;

                    default:
                        logger.warn("Попытка ввода неверного значения при выборе действия.");
                        System.out.println(ANSI_RED + "Введено неверное значение, попробуйте ещё раз!" + ANSI_RESET);
                        break;
                }
            } else {
                logger.warn("Попытка ввода недопустимого значения при выборе действия.");
                System.out.println(ANSI_RED + "Неверный ввод. Попробуйте ещё раз." + ANSI_RESET);
                sc.nextLine();
            }
        }
    }

    private static GardeningDeviceManager getGardeningDeviceManager() {
        GardeningDeviceManager deviceManager = new GardeningDeviceManager();

        Lawnmower lawnmower1 = new Lawnmower("Karcher", "MODEL_12", "бензин", 2010, 8);
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "MODEL_81", "солнечные панели", 2014, 10);
        ThermalDrive thermalDrive = new ThermalDrive("Thermovent", "MODEL_77", "сетевое питание", 2018, 6);
        Lawnmower lawnmower2 = new Lawnmower("Karcher", "MODEL_36", "аккумулятор", 2021, 8);

        deviceManager.addDevice(lawnmower1);
        deviceManager.addDevice(autoWatering);
        deviceManager.addDevice(thermalDrive);
        deviceManager.addDevice(lawnmower2);
        return deviceManager;
    }

    public static void showMenu() {
        logger.info("Вывод меню с действиями на экран.");
        System.out.println("Выберите действие: ");
        System.out.println("\t1. Просмотр устройств");
        System.out.println("\t2. Добавление устройства.");
        System.out.println("\t3. Изменение устройства.");
        System.out.println("\t4. Удаление устройства.");
        System.out.println("\t5. Работа с устройствами.");
        System.out.println("\t6. Сохранение в JSON.");
        System.out.println("\t7. Чтение из JSON.");
        System.out.println("\t0. Выход из программы.");
    }

    public static void addDevice(GardeningDeviceManager deviceManager) {
        logger.info("Запуск действия - добавление устройства.");
        System.out.println(ANSI_BLUE + "=== Добавление устройства ===" + ANSI_RESET);
        System.out.println("Выберите тип устройства: ");
        System.out.println("\t1. Газонокосилка");
        System.out.println("\t2. Автополив");
        System.out.println("\t3. Термопривод для теплиц");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            int deviceType = sc.nextInt();
            sc.nextLine();
            String manufacturer, model, powerSource;

            while (true) {
                System.out.print("\tВведите производителя: ");
                manufacturer = sc.nextLine();
                if (!manufacturer.isEmpty()) break;
                else {
                    logger.warn("Попытка ввода пустой строки в качестве значения атрибута 'производитель'.");
                    System.out.println(ANSI_RED + "Неверный ввод - значение не может быть пустым. Попробуйте ещё раз." + ANSI_RESET);
                }
            }

            while (true) {
                System.out.print("\tВведите модель: ");
                model = sc.nextLine();
                if (!model.isEmpty()) break;
                else {
                    logger.warn("Попытка ввода пустой строки в качестве значения атрибута 'модель'.");
                    System.out.println(ANSI_RED + "Неверный ввод - значение не может быть пустым. Попробуйте ещё раз." + ANSI_RESET);
                }
            }

            while (true) {
                System.out.print("\tВведите источник питания: ");
                powerSource = sc.nextLine();
                if (!powerSource.isEmpty()) break;
                else {
                    logger.warn("Попытка ввода пустой строки в качестве значения атрибута 'источник питания'.");
                    System.out.println(ANSI_RED + "Неверный ввод - значение не может быть пустым. Попробуйте ещё раз." + ANSI_RESET);
                }
            }

            System.out.print("\tВведите год производства (2000 - " + CURRENT_YEAR + "): ");
            int productionYear;
            if (sc.hasNextInt()) {
                productionYear = sc.nextInt();
            } else {
                logger.warn("Попытка ввода недопустимого значения года производства устройства.");
                System.out.println(ANSI_RED + "\nВведено недопустимое значение. Установлен год производства по умолчанию (2000 год)." + ANSI_RESET);
                productionYear = 2000;
                sc.nextLine();
            }

            System.out.print("\tВведите срок службы устройства (3 года - 20 лет): ");
            int expectedLifetime;
            if (sc.hasNextInt()) {
                expectedLifetime = sc.nextInt();
            } else {
                logger.warn("Попытка ввода недопустимого значения срока службы устройства.");
                System.out.println(ANSI_RED + "\nВведено недопустимое значение. Установлен срок службы по умолчанию (5 лет)." + ANSI_RESET);
                expectedLifetime = 5;
            }

            GardeningDevice device;

            switch (deviceType) {
                case 1:
                    device = new Lawnmower(manufacturer, model, powerSource, productionYear, expectedLifetime);
                    break;
                case 2:
                    device = new AutoWatering(manufacturer, model, powerSource, productionYear, expectedLifetime);
                    break;
                case 3:
                    device = new ThermalDrive(manufacturer, model, powerSource, productionYear, expectedLifetime);
                    break;
                default:
                    logger.error("Попытка ввода недопустимого значения типа устройства.");
                    System.out.println(ANSI_RED + "\nВведено неверное значение!\n" + ANSI_RESET);
                    return;
            }
            deviceManager.addDevice(device);
            logger.info("Успешное добавление нового устройства.");
            System.out.println(ANSI_GREEN + "\nУстройство успешно добавлено!" + ANSI_RESET);
            deviceManager.showObjects();
        } else {
            logger.warn("Попытка ввода недопустимого значения типа устройства.");
            System.out.println(ANSI_RED + "\nВведено неверное значение!\n" + ANSI_RESET);
        }
    }

    public static void editDevice(GardeningDeviceManager deviceManager) {
        logger.info("Запуск действия - изменение устройства.");
        System.out.println(ANSI_BLUE + "=== Изменение устройства ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
            logger.warn("Отсутствуют устройства для изменения.");
            System.out.println(ANSI_RED + "\nОтсутствуют устройства для изменения!\n" + ANSI_RESET);
        } else {
            deviceManager.showObjects();
            Scanner sc = new Scanner(System.in);
            int id;

            while (true) {
                try {
                    System.out.println("Введите ID устройства для изменения: ");
                    id = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (InputMismatchException ex) {
                    logger.warn("Попытка ввода недопустимого значения ID устройства.");
                    System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
                    sc.nextLine();
                }
            }
            if (deviceManager.getDeviceByID(id) == null) {
                logger.warn("Попытка ввода значения ID несуществующего устройства.");
                System.out.println(ANSI_RED + "\nУстройство с ID = " + id + " не найдено!\n" + ANSI_RESET);
            } else {
                GardeningDevice device = deviceManager.getDeviceByID(id);
                System.out.println("Введите нового производителя или оставьте поле пустым, чтобы не изменять: ");
                String newManufacturer = sc.nextLine();
                if (!newManufacturer.isEmpty()) {
                    device.setManufacturer(newManufacturer);
                }

                System.out.println("Введите новую модель или оставьте поле пустым, чтобы не изменять: ");
                String newModel = sc.nextLine();
                if (!newModel.isEmpty()) {
                    device.setModel(newModel);
                }

                System.out.println("Введите новый источник питания или оставьте поле пустым, чтобы не изменять: ");
                String newPowerSource = sc.nextLine();
                if (!newPowerSource.isEmpty()) {
                    device.setPowerSource(newPowerSource);
                }

                System.out.println("Введите новый год производства (2000 - " + CURRENT_YEAR + ") " + " или оставьте поле пустым, чтобы не изменять: ");
                String expectedProductionYear = sc.nextLine();
                if (!expectedProductionYear.isEmpty()) {
                    try {
                        int newProductionYear = Integer.parseInt(expectedProductionYear);
                        device.setProductionYear(newProductionYear);
                    } catch (NumberFormatException ex) {
                        logger.warn("Попытка ввода недопустимого значения года производства устройства.");
                        System.out.println(ANSI_RED + "Введено неверное значение! Год производства остался без изменений." + ANSI_RESET);
                    }
                }

                System.out.println("Введите новый срок службы (3 года - 20 лет) или оставьте поле пустым, чтобы не изменять: ");
                String expectedLifetime = sc.nextLine();
                if (!expectedLifetime.isEmpty()) {
                    try {
                        int newLifetime = Integer.parseInt(expectedLifetime);
                        device.setLifetime(newLifetime);
                    } catch (NumberFormatException ex) {
                        logger.warn("Попытка ввода недопустимого значения срока службы устройства.");
                        System.out.println(ANSI_RED + "Введено неверное значение! Срок службы остался без изменений." + ANSI_RESET);
                    }
                }
                logger.info("Успешное изменение устройства.");
                System.out.println(ANSI_GREEN + "\nУстройство успешно изменено!" + ANSI_RESET);
                deviceManager.showObjects();
            }
        }
    }

    public static void removeDevice(GardeningDeviceManager deviceManager) {
        logger.info("Запуск действия - удаление устройства.");
        System.out.println(ANSI_BLUE + "=== Удаление устройства ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
            logger.warn("Отсутствуют устройства для удаления.");
            System.out.println(ANSI_RED + "\nОтсутствуют устройства для удаления!\n" + ANSI_RESET);
        } else {
            deviceManager.showObjects();

            Scanner sc = new Scanner(System.in);
            int id;

            while (true) {
                try {
                    System.out.println("Введите ID устройства для удаления: ");
                    id = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (InputMismatchException ex) {
                    logger.warn("Попытка ввода недопустимого значения ID устройства.");
                    System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
                    sc.nextLine();
                }
            }
            deviceManager.removeDeviceByID(id);
        }
    }

    public static void operateDevices(GardeningDeviceManager deviceManager, Scanner scanner) throws InterruptedException {
        logger.info("Запуск действия - работа с устройствами.");
        System.out.println(ANSI_BLUE + "=== Функциональная работа с устройствами ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
            logger.warn("Отсутствуют устройства для выполнения операций.");
            System.out.println(ANSI_RED + "\nОтсутствуют устройства для выполнения операций!\n" + ANSI_RESET);
        } else {
            deviceManager.showObjects();

            Scanner sc = new Scanner(System.in);
            int id;

            while (true) {
                try {
                    System.out.println("Введите ID устройства для выполнения операций: ");
                    id = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (InputMismatchException ex) {
                    logger.warn("Попытка ввода недопустимого значения ID устройства.");
                    System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
                    sc.nextLine();
                }
            }
            if (deviceManager.getDeviceByID(id) == null) {
                logger.warn("Попытка ввода значения ID несуществующего устройства.");
                System.out.println(ANSI_RED + "\nУстройство с ID = " + id + " не найдено!\n" + ANSI_RESET);
            } else {
                GardeningDevice device = deviceManager.getDeviceByID(id);
                System.out.println("Выберите операцию для выполнения: ");
                System.out.println("\t1. Включение устройства");
                System.out.println("\t2. Выключение устройства");
                System.out.println("\t3. Выполнение действия");
                System.out.println("\t4. Проверить статус устройства");
                System.out.println("\t5. Проверить, истёк ли срок службы устройства\n");

                int operation;

                while (true) {
                    try {
                        System.out.print("Введите номер операции: ");
                        operation = sc.nextInt();

                        if (operation >= 1 && operation <= 5) {
                            break;
                        } else {
                            logger.warn("Попытка ввода неверного значения номера операции.");
                            System.out.println(ANSI_RED + "\nНеверный номер операции. Попробуйте снова.\n" + ANSI_RESET);
                        }
                    } catch (java.util.InputMismatchException ex) {
                        logger.warn("Попытка ввода недопустимого значения номера операции.");
                        System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
                        sc.nextLine();
                    }
                }

                switch (operation) {
                    case 1:
                        logger.info("Запуск действия - включение устройства.");
                        device.turnOn(scanner);
                        break;

                    case 2:
                        logger.info("Запуск действия - выключение устройства.");
                        device.turnOff();
                        break;

                    case 3:
                        logger.info("Запуск действия - выполнение операции устройством.");
                        device.performAction(scanner);
                        break;

                    case 4:
                        logger.info("Запуск действия - проверка статуса устройства.");
                        device.checkStatus();
                        break;

                    case 5:
                        logger.info("Запуск действия - проверка истечения срока службы устройства.");
                        device.isExpired();
                        break;

                    default:
                        logger.warn("Попытка ввода недопустимого значения номера операции.");
                        System.out.println(ANSI_RED + "\nВведено неверное значение. Операция не выполнена.\n" + ANSI_RESET);
                }
            }
        }
    }
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final int CURRENT_YEAR = 2023;
}