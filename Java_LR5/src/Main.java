import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) {
        GardeningDeviceManager deviceManager = new GardeningDeviceManager();

        Lawnmower lawnmower = new Lawnmower("Karcher", "2019", "gasoline");
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "2021", "solar panels");
        GreenhouseThermostat thermostat = new GreenhouseThermostat("Thermovent", "2018", "mains power supply");

        deviceManager.addDevice(lawnmower);
        deviceManager.addDevice(autoWatering);
        deviceManager.addDevice(thermostat);

        System.out.println(ANSI_BLUE + "\n\t\t\t=== Лабораторная работа #5 ===\n\t\tВыполнил студент группы ИКПИ-14 Сергеев Н.В.\n" + ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        while (true) {
            showMenu();
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
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
                        operateDevices(deviceManager);
                        break;

                    case 6:
                        System.out.println("Завершение работы...");
                        return;

                    default:
                        System.out.println(ANSI_RED + "Введено неверное значение, попробуйте ещё раз!" + ANSI_RESET);
                        break;
                }
            }
        }
    }

    public static void showMenu() {
        System.out.println("\nВыберите действие: ");
        System.out.println("\t1. Просмотр объектов");
        System.out.println("\t2. Добавление объекта.");
        System.out.println("\t3. Изменение объекта.");
        System.out.println("\t4. Удаление объекта.");
        System.out.println("\t5. Работа с устройствами.");
        System.out.println("\t6. Выход из программы.");
    }

    public static void showIncorrectInputMessage() {
        System.out.println(ANSI_RED + "Введено неверное значение!" + ANSI_RESET);
    }

    public static void addDevice(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Добавление устройства ===" + ANSI_RESET);
        System.out.println("Введите тип устройства:\n\t1 - Газонокосилка\n\t2 - Автополив\n\t" +
                "3 - Термопривод для теплиц");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            int deviceType = sc.nextInt();

            System.out.println("\tВведите производителя: ");
            String manufacturer = sc.nextLine();
            System.out.println("\tВведите модель: ");
            String model = sc.nextLine();
            System.out.println("\tВведите источник питания: ");
            String powerSource = sc.nextLine();
            GardeningDevice device;

            switch (deviceType) {
                case 1:
                    device = new Lawnmower(manufacturer, model, powerSource);
                    break;
                case 2:
                    device = new AutoWatering(manufacturer, model, powerSource);
                    break;
                case 3:
                    device = new GreenhouseThermostat(manufacturer, model, powerSource);
                    break;
                default:
                    showIncorrectInputMessage();
                    return;
            }
            deviceManager.addDevice(device);
            System.out.println(ANSI_GREEN + "Устройство успешно добавлено!" + ANSI_RESET);
        }
    }

    public static void editDevice(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Изменение устройства ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
            System.out.println(ANSI_RED + "Отсутствуют устройства для изменения!" + ANSI_RESET);
        } else {
            deviceManager.showObjects();
            System.out.println("Выберите устройство для изменения: ");
            Scanner sc = new Scanner(System.in);
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                if (0 <= choice && choice < deviceManager.getDevices().size()) {
                    GardeningDevice device = deviceManager.getDevices().get(choice);

                    System.out.println("Введите нового производителя (оставьте пустым, чтобы не изменять): ");
                    String newManufacturer = sc.nextLine();
                    if (!newManufacturer.isEmpty()) {
                        device.setManufacturer(newManufacturer);
                    }

                    System.out.println("Введите новую модель (оставьте пустым, чтобы не изменять): ");
                    String newModel = sc.nextLine();
                    if (!newModel.isEmpty()) {
                        device.setModel(newModel);
                    }

                    System.out.println("Введите новый источник питания (оставьте пустым, чтобы не изменять): ");
                    String newPowerSource = sc.nextLine();
                    if (!newPowerSource.isEmpty()) {
                        device.setPowerSource(newPowerSource);
                    }

                    System.out.println(ANSI_GREEN + "Устройство успешно изменено!" + ANSI_RESET);
                } else {
                    showIncorrectInputMessage();
                }
            } else {
                showIncorrectInputMessage();
            }
        }
    }

    public static void removeDevice(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Удаление устройства ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
            System.out.println(ANSI_RED + "Отсутствуют устройства для удаления!" + ANSI_RESET);
        } else {
            deviceManager.showObjects();
            System.out.println("Выберите устройство для удаления: ");
            Scanner sc = new Scanner(System.in);
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                if (0 <= choice && choice < deviceManager.getDevices().size()) {
                    deviceManager.removeDevice(choice);
                    System.out.println(ANSI_GREEN + "Устройство успешно удалено!" + ANSI_RESET);
                    deviceManager.showObjects();
                } else {
                    showIncorrectInputMessage();
                }
            } else {
                showIncorrectInputMessage();
            }
        }
    }

    public static void operateDevices(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Функциональная работа с устройствами ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
            System.out.println(ANSI_RED + "Отсутствуют устройства для выполнения операций!" + ANSI_RESET);
        } else {
            deviceManager.showObjects();
            Scanner sc = new Scanner(System.in);
            int choice;
            while (true) {
                try {
                    System.out.println("Выберите объект для выполнения операций: ");
                    choice = sc.nextInt();
                    sc.nextLine();

                    if (choice < 0 || choice > deviceManager.getDevices().size()) {
                        System.out.println(ANSI_RED + "Неверный индекс устройства, попробуйте ещё раз!" + ANSI_RESET);
                    } else {
                        break;
                    }
                } catch (InputMismatchException ex) {
                    System.out.println(ANSI_RED + "Ошибка: неверное значение. Введите число." + ANSI_RESET);
                    sc.nextLine();
                }
            }

            GardeningDevice device = deviceManager.getDevices().get(choice);

            System.out.println("Выберите операцию для выполнения: ");
            System.out.println("1. Включение устройства");
            System.out.println("2. Выключение устройства");
            System.out.println("3. Выполнение действия");

            int operation;

            while (true) {
                try {
                    System.out.print("Введите номер операции: ");
                    operation = sc.nextInt();
                    sc.nextLine();

                    if (operation >= 1 && operation <= 3) {
                        break;
                    } else {
                        System.out.println(ANSI_RED + "Неверный номер операции. Попробуйте снова." + ANSI_RESET);
                    }
                } catch (java.util.InputMismatchException ex) {
                    System.out.println(ANSI_RED + "Ошибка: неверное значение. Введите число." + ANSI_RESET);
                    sc.nextLine();
                }
            }

            switch (operation) {
                case 1:
                    device.turnOn();
                    break;
                case 2:
                    device.turnOff();
                    break;
                case 3:
                    device.performAction();
                    break;
                default:
                    System.out.println(ANSI_RED + "Введено неверное значение. Операция не выполнена." + ANSI_RESET);
            }
        }
    }
}