import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final int CURRENT_YEAR = 2023;

    public static void main(String[] args) {
        GardeningDeviceManager deviceManager = getGardeningDeviceManager();

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
                        operateDevices(deviceManager, sc);
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

    private static GardeningDeviceManager getGardeningDeviceManager() {
        GardeningDeviceManager deviceManager = new GardeningDeviceManager();

        Lawnmower lawnmower1 = new Lawnmower("Karcher", "MODEL_12", "бензин", 2010, 8);
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "MODEL_81", "солнечные панели", 2014, 10);
        ThermalDrive thermostat = new ThermalDrive("Thermovent", "MODEL_77", "сетевое питание", 2018, 6);
        Lawnmower lawnmower2 = new Lawnmower("Karcher", "MODEL_36", "аккумулятор", 2021, 8);

        deviceManager.addDevice(lawnmower1);
        deviceManager.addDevice(autoWatering);
        deviceManager.addDevice(thermostat);
        deviceManager.addDevice(lawnmower2);
        return deviceManager;
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
            System.out.println("\tВведите год производства (2000 - " + CURRENT_YEAR + "): ");
            int productionYear;
            if (sc.hasNextInt()) {
                productionYear = sc.nextInt();
            } else {
                System.out.println(ANSI_RED + "Введено неверное значение! Установлен год производства по умолчанию (2000 год)." + ANSI_RESET);
                productionYear = 2000;
            }

            System.out.println("\tВведите срок службы устройства (3 года - 20 лет): ");
            int expectedLifetime;
            if (sc.hasNextInt()) {
                expectedLifetime = sc.nextInt();
            } else {
                System.out.println(ANSI_RED + "Введено неверное значение! Установлен срок службы по умолчанию (5 лет)." + ANSI_RESET);
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
                if (expectedProductionYear.isEmpty()) {
                    try {
                        int newProductionYear = Integer.parseInt(expectedProductionYear);
                        device.setProductionYear(newProductionYear);
                    } catch (NumberFormatException ex) {
                        System.out.println(ANSI_RED + "Введено неверное значение! Год производства остался без изменений." + ANSI_RESET);
                    }
                }

                System.out.println("Введите новый срок службы (3 года - 20 лет) или оставьте поле пустым, чтобы не изменять: ");
                String expectedLifetime = sc.nextLine();
                if (expectedLifetime.isEmpty()) {
                    try {
                        int newLifetime = Integer.parseInt(expectedLifetime);
                        device.setLifetime(newLifetime);
                    } catch (NumberFormatException ex) {
                        System.out.println(ANSI_RED + "Введено неверное значение! Срок службы остался без изменений." + ANSI_RESET);
                    }
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

    public static void operateDevices(GardeningDeviceManager deviceManager, Scanner scanner) {
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
                System.out.println("\t1. Включение устройства");
                System.out.println("\t2. Выключение устройства");
                System.out.println("\t3. Выполнение действия");

                int operation;

                while (true) {
                    try {
                        System.out.print("Введите номер операции: ");
                        operation = sc.nextInt();
//                        sc.nextLine();

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
                        device.turnOn(scanner);
                        break;
                    case 2:
                        device.turnOff();
                        break;
                    case 3:
                        device.performAction(scanner);
                        break;
                    default:
                        System.out.println(ANSI_RED + "\nВведено неверное значение. Операция не выполнена.\n" + ANSI_RESET);
                }
            }
        }
    }
}