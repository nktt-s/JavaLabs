import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File sourceFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Source.txt");
        File destinationFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Destination.txt");

        Scanner scBin = new Scanner(System.in);

        Worker anna = new Worker("Alexeeva", "operator", "female", 1975);
        Worker valentin = new Worker("Ivanov", "electrician", "male", 1958); // Пора на пенсию
        Worker viktor = new Worker("Petrov", "locksmith", "male", 1959);
        Worker elena = new Worker("Alexandrova", "operator", "female", 1963); // Пора на пенсию
        Worker eugeniy = new Worker("Mihaylov", "mechanic", "male", 1957); // Пора на пенсию
        ArrayList<Worker> array = new ArrayList<>(Arrays.asList(anna, valentin, viktor, elena, eugeniy));

        // Создаём бинарный файл
        File sourceBinFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "BinaryFile.txt");
        try {
            boolean created = false;
            if (!sourceBinFile.exists()) {
                created = sourceBinFile.createNewFile();
                System.out.println("Бинарный файл создан!");
            }
            if (sourceBinFile.exists() || created) {
                try {
                    FileOutputStream fos = new FileOutputStream("BinaryFile.txt");
                    for (Worker worker : array) {
                        String s = worker.getAllData();
                        byte[] data = s.getBytes();
                        fos.write(data, 0, data.length);
                        fos.flush();
                    }
                    fos.close();
                    scBin.close();

                    byte[] data = new byte[(int) sourceBinFile.length()];
                    FileInputStream fis = new FileInputStream(sourceBinFile);
//                    DataInputStream dis = new DataInputStream(fis);
                    int ch;
                    int counter = 0;
                    String year = "";
                    while ((ch = fis.read()) != -1) {
                        // TODO - Разобраться, как проверять пол работника
                        if (counter == 4) {
                                System.out.println(year);
//                            if (Integer.parseInt(year) < 1960) {
//                                System.out.println(year);
//                            }
                            counter = 0;
                            year = "";
                        }
                        if (48 <= ch && ch <= 57) { // Отобрали цифры (год рождения)
                            ++counter;
                            year += (char) ch;
                        }
//                        System.out.print((char) ch);
                    }

//                    System.out.println(fis.read(data, 0, data.length));
                    fis.close();

                } catch (FileNotFoundException ex) {
                    System.out.println("Исходный бинарный файл не найден!");
                } catch (IOException ex) {
                    System.out.println("Ошибка при закрытии потока!");
                }
            }
        } catch (IOException ex) {
            System.out.println("Ошибка при создании файла!");
            System.out.println(ex.getMessage());
        }

//        if (sourceFile.exists()) {
//            try {
//                boolean created = false;
//                if (!destinationFile.exists()) {
//                    created = destinationFile.createNewFile();
//                    System.out.println("Файл создан!");
//                }
//                if (destinationFile.exists() || created) {
//                    while (true) {
//                        Scanner sc = new Scanner(System.in);
//                        System.out.println("Выберите действие:\n\t1. Построчный вывод содержимого исходного файла\n\t   и запись результатов в файл назначения.\n\t2. Выход из программы.");
//                        if (sc.hasNextInt()) {
//                            int choice = sc.nextInt();
//                            switch (choice) {
//                                case 1:
//                                    try {
//                                        BufferedReader buffReader = new BufferedReader(new FileReader("Source.txt"));
//                                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("Destination.txt"));
//                                        String line;
//                                        while ((line = buffReader.readLine()) != null) {
//                                            System.out.println(line);
////                                            sc.next();
//                                            int number = Integer.parseInt(line);
//                                            if (number % 3 == 0 && number % 7 != 0) {
//                                                buffWriter.write(number + "\n");
//                                            }
//                                        }
//                                        buffReader.close();
//                                        buffWriter.close();
//                                        System.out.println("Результаты успешно сохранены!");
//                                    } catch (FileNotFoundException ex) {
//                                        System.out.println("Ошибка! Файла назначения не существует.");
//                                        ex.printStackTrace();
//                                    }
//                                    break;
//                                case 2:
//                                    System.out.println("Завершение работы...");
//                                    return;
//                                default:
//                                    System.out.println("Введено неверное значение, попробуйте ещё раз!");
//                            }
//                        } else {
//                            System.out.println("Введено неверное значение, попробуйте ещё раз!");
//                            sc.next();
//                        }
//                    }
//                }
//            } catch (IOException ex) {
//                System.out.println("Ошибка при создании файла!");
//                System.out.println(ex.getMessage());
//            }
//        } else {
//            System.out.println("Исходный файл не найден!");
//        }
    }
}
