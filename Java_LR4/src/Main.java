import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("\n\t\t\t=== Лабораторная работа #4 ===\n\t\tВыполнил студент группы ИКПИ-14 Сергеев Н.В.\n");
        while (true) {

            if (args.length == 0) {
                showIncorrectInputMessage();
                break;
            }

            else {
                switch (args[0]) {
                    case "--csv":
                        if (args[1].isEmpty()) {
                            System.out.println("Отсутствует имя исходного файла.");
                            System.out.println("Введите его после ключа '--csv' и повторите попытку.");
                            return;
                        } else {
                            writeWordFrequencyToCSV(Objects.requireNonNull(createWordFrequencyMap(args[1])), args[1]);
                            // Objects.requireNonNull Позволяет гарантированно получить Not null объект на выходе
                        }
                        return;

                    case "--csv-few":
                        return;

                    case "--json-write":
                        return;

                    case "--json-read":
                        return;

                    case "--help":
                        showMenu();
                        return;

                    default:
                        showIncorrectInputMessage();
                        return;
                }
            }
        }

//        for (String fileName : args) {
//            Map<String, Integer> wordFrequencyMap = createWordFrequencyMap(fileName);
//        }
//        String inputFileName = args[0];
//        Map<String, Integer> wordFrequencyMap = createWordFrequencyMap(inputFileName);
//        if (wordFrequencyMap != null) {
//            writeWordFrequencyToCSV(wordFrequencyMap);
//        }
    }

    public static void showMenu() {
        System.out.println("\t\t\t=== Возможности программы ===\n");
        System.out.println("\tЗапись в CSV-файл\t\t\t\t| --csv <имя файла>");
        System.out.println("\tЗапись в CSV-файл для n-ого количества файлов\t| --csv-few <имя файла> <...>");
        System.out.println("\tЗапись JSON-файла для n-ого количества файлов\t| --json-write <имя файла> <...>");
        System.out.println("\tЧтение JSON-файла\t\t\t\t| --json-read <имя файла>");
        System.out.println("\tСправка\t\t\t\t\t\t| --help\n");
    }

    public static void showIncorrectInputMessage() {
        System.out.println("\tЧтобы посмотреть справку по работе с программой, введите: java Main --help\n");
    }

    public static HashMap<String, Integer> createWordFrequencyMap(String inputFileName) {
        HashMap<String, Integer> wordFrequencyMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))){
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (char c : line.toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        stringBuilder.append(c);
                    } else if (!stringBuilder.isEmpty()) {
                        String word = stringBuilder.toString().toLowerCase();
                        wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                        stringBuilder.setLength(0);
                    }
                }
                if (!stringBuilder.isEmpty()) {
                    String word = stringBuilder.toString().toLowerCase();
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return wordFrequencyMap;
    }

    public static void writeWordFrequencyToCSV(HashMap<String, Integer> wordFrequencyMap, String inputFileName) {
        ArrayList<Map.Entry<String, Integer>> sortedWordFrequencyList = new ArrayList<>(wordFrequencyMap.entrySet());
//        sortedWordFrequencyList.sort(Comparator.comparingInt(entry -> -entry.getValue()));
        sortedWordFrequencyList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        String mostCommonWord = sortedWordFrequencyList.get(0).getKey();
        int mostCommonWordCount = sortedWordFrequencyList.get(0).getValue();
        String rarestWord = sortedWordFrequencyList.get(sortedWordFrequencyList.size() - 1).getKey();
        int rarestWordCount = sortedWordFrequencyList.get(sortedWordFrequencyList.size() - 1).getValue();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFileName + "_frequency_analysis.csv"))){

            writer.write("Word,Frequency,Frequency in %,Most common word,Count of most common word,Rarest word,Count of rarest word");
            writer.newLine();

            for (HashMap.Entry<String, Integer> entry : sortedWordFrequencyList) {
                String word = entry.getKey();
                int count = entry.getValue();
                // Округляем число до целого, затем умножаем на 100 и делим на 100, чтобы получить число с точностью до 2 знаков после запятой
                double frequencyPercent = Math.round((double) count * 100 / wordFrequencyMap.size() * 100.0) / 100.0;
                String percentWithPoint = String.valueOf(frequencyPercent).replace(',', '.') + "%";
                if (word.equals(mostCommonWord)) {
                    writer.write(String.format("%s, %d, %s, %s, %d, %s, %d",
                            word, count, percentWithPoint, mostCommonWord, mostCommonWordCount, rarestWord, rarestWordCount));
                } else {
                    writer.write(String.format("%s, %d, %s", word, count, percentWithPoint));
                }
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Ошибка при записи в CSV-файл: " + ex + "\n");
        }
        System.out.println("CSV-файл успешно создан: " + inputFileName + "_frequency_analysis.csv\n");

    }
}
