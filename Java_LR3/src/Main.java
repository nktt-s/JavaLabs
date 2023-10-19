import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        File sourceFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Source.txt");
        File destinationFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Destination.txt");

        // Создание бинарного файла и заполнение файла данными о работниках завода
        String fileName = "workers_data.dat";
        createWorkersFile(fileName);

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
                        System.out.println("Выберите действие:\n\t1. Часть 1. Построчный вывод содержимого исходного файла\n\t   и запись результатов в файл назначения.\n\t2. Часть 2. Вывод данных в соответствии с условием задачи.\n\t3. Выход из программы.");
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
                                    System.out.println("Информация о мужчинах, выходящих на пенсию: ");
                                    printRetirementEligibleWorkers(fileName, "male");
                                    System.out.println();
                                    System.out.println("Информация о женщинах, выходящих на пенсию: ");
                                    printRetirementEligibleWorkers(fileName, "female");
                                    break;

                                case 3:
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

    public static void createWorkersFile(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)) {
            writeWorkersData(dataOutputStream, "Alexeeva", "operator", "female", 1975);
            writeWorkersData(dataOutputStream, "Ivanov", "electrician", "male", 1958); // пора на пенсию
            writeWorkersData(dataOutputStream, "Petrov", "locksmith", "male", 1959); // пора на пенсию
            writeWorkersData(dataOutputStream, "Alexandrova", "operator", "female", 1963); // пора на пенсию
            writeWorkersData(dataOutputStream, "Mihaylov", "mechanic", "male", 1967);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeWorkersData(DataOutputStream dataOutputStream, String lastName, String position, String gender, int birthYear) throws IOException {
        dataOutputStream.writeUTF(lastName);
        dataOutputStream.writeUTF(position);
        dataOutputStream.writeUTF(gender);
        dataOutputStream.writeInt(birthYear);
    }

    public static void printRetirementEligibleWorkers(String fileName, String genderFilter) {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             DataInputStream dataInputStream = new DataInputStream(fileInputStream)) {
            int currentYear = 2023;

            // Построчное чтение данных о работниках из бинарного файла
            while (dataInputStream.available() > 0) {
                String lastName = dataInputStream.readUTF();
                String position = dataInputStream.readUTF();
                String gender = dataInputStream.readUTF();
                int birthYear = dataInputStream.readInt();

                // Проверка фильтра по полу и условия достижения пенсионного возраста
                // 60 лет для мужчин, 55 лет для женщин
                if (gender.equalsIgnoreCase(genderFilter) && (currentYear - birthYear) >= getRetirementAge(gender)) {
                    System.out.println("Фамилия: " + lastName);
                    System.out.println("Должность: " + position);
                    System.out.println("Пол: " + gender);
                    System.out.println("Год рождения: " + birthYear);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getRetirementAge(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 60;
        } else {
            return 55;
        }
    }
}
