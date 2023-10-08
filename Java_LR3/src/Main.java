import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File sourceFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Source.txt");
        File destinationFile = new File("C://Users/nktt/IdeaProjects/Java_LR3", "Destination.txt");

        if (sourceFile.exists()) {
            try {
                boolean created = false;
                if (!destinationFile.exists()) {
                    created = destinationFile.createNewFile();
                }
                if (created) {
                    System.out.println("Файл создан!");
                    // Читаем исходный файл
                    try {
                        FileReader fileReader = new FileReader("Source.txt");
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println(line);
                        }
                        bufferedReader.close();
                    } catch (IOException ex) {
                        System.out.println("Ошибка при чтении файла!");
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
