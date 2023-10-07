import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void showMenu() {
        System.out.println("\nВыберите действие: ");
        System.out.println("\t1. Просмотр объектов.");
        System.out.println("\t2. Изменение объектов.");
        System.out.println("\t3. Вычисление параметра.");
        System.out.println("\t4. Добавление объекта.");
        System.out.println("\t5. Удаление объекта.");
        System.out.println("\t6. Выход из программы.");
    }

    public static void showObjects(ArrayList<Vehicle> array) {
        for (int i = 0; i < array.size(); ++i) {
            System.out.println(i + " | " + array.get(i).print());
        }
    }

    public static void main(String[] args) {

        LightVehicle newCar = new LightVehicle("newCar", 2017, "Sedan");
        LightVehicle oldCar = new LightVehicle("oldCar", 2005, "Pickup");
        Airplane boeing737 = new Airplane("Boeing 737", 1968, 2592);

        ArrayList<Vehicle> array = new ArrayList<>(Arrays.asList(newCar, oldCar, boeing737));
        Scanner sc = new Scanner(System.in);
        while(true) {
            showMenu();
            if (sc.hasNextInt()) {
                int choiceCase = sc.nextInt();
                switch (choiceCase) {
                case 1:
                    showObjects(array);
                    break;

                case 2:
                    showObjects(array);
                    System.out.print("Выберите объект для изменения: ");
                    Scanner sc_modify = new Scanner(System.in);
                    if (sc_modify.hasNextInt()) {
                        int choice = sc_modify.nextInt();
                        if (0 <= choice && choice < array.size()) {
                            System.out.print("Введите название: ");
                            array.get(choice).setName(sc_modify.next());
                            System.out.print("Введите год выпуска: ");
                            while (true) {
                                if (sc_modify.hasNextInt()) {
                                    array.get(choice).setProductionYear(sc_modify.nextInt());
                                    break;
                                } else {
                                    System.out.println("Введено неверное значение!");
                                    System.out.print("Введите год выпуска: ");
                                    sc_modify.next();
                                }
                            }
                            if (array.get(choice) instanceof LightVehicle) {
                                System.out.print("Введите тип кузова: ");
                                ((LightVehicle) array.get(choice)).setBodyType(sc_modify.next());
                            } else if (array.get(choice) instanceof Airplane) {
                                System.out.print("Введите дальность полёта: ");
                                while (true) {
                                    if (sc_modify.hasNextInt()) {
                                        ((Airplane) array.get(choice)).setFlightDistance(sc_modify.nextInt());
                                        break;
                                    } else {
                                        System.out.println("Введено неверное значение!");
                                        System.out.print("Введите дальность полёта: ");
                                        sc_modify.next();
                                    }
                                }
                            }
                        } else {
                            System.out.println("Введено неверное значение!");
                        }
                    } else {
                        System.out.println("Введено неверное значение!");
                        sc_modify.next();
                    }
                    break;

                case 3:
                    int newestYear = 0, newestVehicle = 0;
                    for (int i = 0; i < array.size(); ++i) {
                        if (array.get(i).getProductionYear() < newestYear) {
                            newestYear = array.get(i).getProductionYear();
                            newestVehicle = i;
                        }
                    }
                    System.out.println("Самое новое транспортное средство: ");
                    System.out.println(array.get(newestVehicle).print());
                    break;

                case 4:
                    System.out.println("Выберите тип объекта:\n1. LightVehicle\n2. Airplane");
                    Scanner sc_add = new Scanner(System.in);
                    if (sc_add.hasNextInt()) {
                        int choice = sc_add.nextInt();
                        if (choice == 1) {
                            array.addLast(new LightVehicle());
                            System.out.print("Введите название: ");
                            array.getLast().setName(sc_add.next());
                            System.out.print("Введите год выпуска: ");

                            while (true) {
                                if (sc_add.hasNextInt()) {
                                    array.getLast().setProductionYear(sc_add.nextInt());
                                    break;
                                } else {
                                    System.out.println("Введено неверное значение!");
                                    System.out.print("Введите год выпуска: ");
                                    sc_add.next();
                                }
                            }
                            System.out.print("Введите тип кузова: ");
                            ((LightVehicle) array.getLast()).setBodyType(sc_add.next());
                        } else if (choice == 2) {
                            array.addLast(new Airplane());
                            System.out.print("Введите название: ");
                            array.getLast().setName(sc_add.next());
                            System.out.print("Введите год выпуска: ");
                            while (true) {
                                if (sc_add.hasNextInt()) {
                                    array.getLast().setProductionYear(sc_add.nextInt());
                                    break;
                                } else {
                                    System.out.println("Введено неверное значение!");
                                    System.out.print("Введите год выпуска: ");
                                    sc_add.next();
                                }
                            }
                            System.out.print("Введите дальность полёта: ");
                            while (true) {
                                if (sc_add.hasNextInt()) {
                                    ((Airplane) array.getLast()).setFlightDistance(sc_add.nextInt());
                                    break;
                                } else {
                                    System.out.println("Введено неверное значение!");
                                    System.out.print("Введите дальность полёта: ");
                                    sc_add.next();
                                }
                            }
                        } else {
                            System.out.println("Введено неверное значение!");
                        }
                    } else {
                        System.out.println("Введено неверное значение!");
                        sc_add.next();
                    }
                    break;

                case 5:
                    showObjects(array);
                    System.out.print("Выберите объект для удаления: ");
                    Scanner sc_del = new Scanner(System.in);
                    if (sc_del.hasNextInt()) {
                        int choice = sc_del.nextInt();
                        if (0 <= choice && choice < array.size()) {
                            array.remove(choice);
                            System.out.println("Объект успешно удалён!");
                        } else {
                            System.out.println("Введено неверное значение!");
                        }
                    } else {
                        System.out.println("Введено неверное значение!");
                        sc_del.next();
                    }
                    break;

                case 6:
                    System.out.println("Завершение работы...");
                    return;

                default:
                    System.out.println("Введено неверное значение, попробуйте ещё раз!");
                }
            } else {
                System.out.println("Введено неверное значение, попробуйте ещё раз!");
                sc.next();
            }

        }
    }
}
