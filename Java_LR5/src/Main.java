import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) {
        GardeningDeviceManager deviceManager = new GardeningDeviceManager();

        Lawnmower lawnmower = new Lawnmower("Karcher", "2019", "gasoline");
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "2021", "solar panels");
        ThermalDrive thermostat = new ThermalDrive("Thermovent", "2018", "mains power supply");

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
        System.out.println("Выберите действие: ");
        System.out.println("\t1. Просмотр устройств");
        System.out.println("\t2. Добавление устройства.");
        System.out.println("\t3. Изменение устройства.");
        System.out.println("\t4. Удаление устройства.");
        System.out.println("\t5. Работа с устройствами.");
        System.out.println("\t6. Выход из программы.");
    }

    public static void addDevice(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Добавление устройства ===" + ANSI_RESET);
        System.out.println("Выберите тип устройства: ");
        System.out.println("\t1. Газонокосилка");
        System.out.println("\t2. Автополив");
        System.out.println("\t3. Термопривод для теплиц");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            int deviceType = sc.nextInt();
            sc.nextLine();
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
                    device = new ThermalDrive(manufacturer, model, powerSource);
                    break;
                default:
                    System.out.println(ANSI_RED + "\nВведено неверное значение!\n" + ANSI_RESET);
                    return;
            }
            deviceManager.addDevice(device);
            System.out.println(ANSI_GREEN + "\nУстройство успешно добавлено!" + ANSI_RESET);
            deviceManager.showObjects();
        } else {
            System.out.println(ANSI_RED + "\nВведено неверное значение!\n" + ANSI_RESET);
        }
    }

    public static void editDevice(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Изменение устройства ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
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
                    System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
                    sc.nextLine();
                }
            }
            if (deviceManager.getDeviceByID(id) == null) {
                System.out.println(ANSI_RED + "\nУстройство с ID = " + id + " не найдено!\n" + ANSI_RESET);
            } else {
                GardeningDevice device = deviceManager.getDeviceByID(id);

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

                System.out.println(ANSI_GREEN + "\nУстройство успешно изменено!" + ANSI_RESET);
                deviceManager.showObjects();
            }
        }
    }

    public static void removeDevice(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Удаление устройства ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
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
                    System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
                    sc.nextLine();
                }
            }
            deviceManager.removeDeviceByID(id);
        }
    }

    public static void operateDevices(GardeningDeviceManager deviceManager) {
        System.out.println(ANSI_BLUE + "=== Функциональная работа с устройствами ===" + ANSI_RESET);
        if (deviceManager.getDevices().isEmpty()) {
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
                    System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
                    sc.nextLine();
                }
            }
            if (deviceManager.getDeviceByID(id) == null) {
                System.out.println(ANSI_RED + "\nУстройство с ID = " + id + " не найдено!\n" + ANSI_RESET);
            } else {
                GardeningDevice device = deviceManager.getDeviceByID(id);

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
                            System.out.println(ANSI_RED + "\nНеверный номер операции. Попробуйте снова.\n" + ANSI_RESET);
                        }
                    } catch (java.util.InputMismatchException ex) {
                        System.out.println(ANSI_RED + "\nОшибка: неверное значение. Введите число.\n" + ANSI_RESET);
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
                        System.out.println(ANSI_RED + "\nВведено неверное значение. Операция не выполнена.\n" + ANSI_RESET);
                }
            }
        }
    }
}