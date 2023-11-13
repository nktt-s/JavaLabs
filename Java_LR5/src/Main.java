import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static void main(String[] args) {
        Lawnmower lawnmower = new Lawnmower("Karcher", "2019", "gasoline");
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "2021", "solar panels");
        ThermalActuator thermalActuator = new ThermalActuator("Thermovent", "2018", "mains power supply");

        ArrayList<GardeningDevice> gardeningDevices = new ArrayList<>(Arrays.asList(lawnmower, autoWatering, thermalActuator));
        System.out.println(ANSI_BLUE + "\n\t\t\t=== Лабораторная работа #5 ===\n\t\tВыполнил студент группы ИКПИ-14 Сергеев Н.В.\n" + ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        while (true) {
            showMenu();
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        showObjects(gardeningDevices);
                        break;

                    case 2:

                        break;

                    case 3:
                        if (gardeningDevices.isEmpty()) {
                            System.out.println(ANSI_RED + "Отсутствуют объекты для изменения!" + ANSI_RESET);
                            break;
                        }
                        showObjects(gardeningDevices);
                        System.out.print("Выберите объект для изменения: ");
                        Scanner sc_modify = new Scanner(System.in);
                        // TODO
                        break;

                    case 4:
                        if (gardeningDevices.isEmpty()) {
                            System.out.println(ANSI_RED + "Отсутствуют объекты для удаления!" + ANSI_RESET);
                            break;
                        }
                        showObjects(gardeningDevices);
                        System.out.println("Выберите объект для удаления: ");
                        Scanner sc_del = new Scanner(System.in);
                        if (sc_del.hasNextInt()) {
                            int choice_del = sc_del.nextInt();
                            if (1 <= choice_del && choice_del <= gardeningDevices.size()) {
                                gardeningDevices.remove(choice_del - 1);
                                System.out.println(ANSI_GREEN + "Объект успешно удалён!" + ANSI_RESET);
                                showObjects(gardeningDevices);
                            } else {
                                System.out.println(ANSI_RED + "Введено неверное значение!" + ANSI_RESET);
                            }
                        } else {
                            System.out.println(ANSI_RED + "Введено неверное значение!" + ANSI_RESET);
                            sc_del.next();
                        }
                        break;

                    case 5:
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

    public static void showObjects(ArrayList<GardeningDevice> gardeningDevices) {
        System.out.println("\nID | Manufacturer | Model | Power Supply");
        System.out.println("=================================================");
        if (gardeningDevices.isEmpty()) {
            System.out.println("------------------ NO OBJECTS -------------------");
        } else {
            for (int i = 1; i <= gardeningDevices.size(); ++i) {
                System.out.println(" " + i + " | " + gardeningDevices.get(i - 1).print());
            }
        }
    }
}