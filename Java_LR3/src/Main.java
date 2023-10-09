import java.io.*;

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
                    try {
                        BufferedReader buffReader = new BufferedReader(new FileReader("Source.txt"));
                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("Destination.txt"));
                        String line;
                        while ((line = buffReader.readLine()) != null) {
                            int number = Integer.parseInt(line);
                            if (number % 3 == 0 && number % 7 != 0) {
                                buffWriter.write(number + "\n");
                            }
                        }
                        buffReader.close();
                        buffWriter.close();
                        System.out.println("Работа программы успешно завершена!");
                    } catch (FileNotFoundException ex) {
                        System.out.println("Ошибка! Файла назначения не существует.");
                        ex.printStackTrace();
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
