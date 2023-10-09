import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File sourceFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Source.txt");
        File destinationFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Destination.txt");

        if (sourceFile.exists()) {
            try {
                boolean created = false;
                if (!destinationFile.exists()) {
                    created = destinationFile.createNewFile();
                    System.out.println("Файл создан!");
                }
                if (destinationFile.exists() || created) {
                    while (true) {
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Выберите действие:\n\t1. Построчный вывод содержимого исходного файла\n\t   и запись результатов в файл назначения.\n\t2. Выход из программы.");
                        if (sc.hasNextInt()) {
                            int choice = sc.nextInt();
                            switch (choice) {
                                case 1:
                                    try {
                                        BufferedReader buffReader = new BufferedReader(new FileReader("Source.txt"));
                                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("Destination.txt"));
                                        String line;
                                        while ((line = buffReader.readLine()) != null) {
                                            System.out.println(line);
//                                            sc.next();
                                            int number = Integer.parseInt(line);
                                            if (number % 3 == 0 && number % 7 != 0) {
                                                buffWriter.write(number + "\n");
                                            }
                                        }
                                        buffReader.close();
                                        buffWriter.close();
                                        System.out.println("Результаты успешно сохранены!");
                                    } catch (FileNotFoundException ex) {
                                        System.out.println("Ошибка! Файла назначения не существует.");
                                        ex.printStackTrace();
                                    }
                                    break;
                                case 2:
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
            } catch (IOException ex) {
                System.out.println("Ошибка при создании файла!");
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Исходный файл не найден!");
        }
    }
}
