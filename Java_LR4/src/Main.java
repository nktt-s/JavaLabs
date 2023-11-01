import javax.sql.rowset.spi.SyncResolver;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
//        while (true) {
//            System.out.println("Lab.Work #4");
//            if (args[0].equals("-h") || args[0].equals("--help")) {
//                System.out.println("Использование: java Main <имя_текстового_файла>");
//            } else {
//                System.out.println("Справка: java Main -h или java Main --help");
//            }
//        }
        for (String fileName : args) {
            Map<String, Integer> wordFrequencyMap = createWordFrequencyMap(fileName);
        }
        String inputFileName = args[0];
        Map<String, Integer> wordFrequencyMap = createWordFrequencyMap(inputFileName);
        if (wordFrequencyMap != null) {
            writeWordFrequencyToCSV(wordFrequencyMap);
        }
    }

    public static Map<String, Integer> createWordFrequencyMap(String inputFileName) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

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
            return null;
        }
        return wordFrequencyMap;
    }

    public static void writeWordFrequencyToCSV(Map<String, Integer> wordFrequencyMap) {
        List<Map.Entry<String, Integer>> sortedWordFrequencyList = new ArrayList<>(wordFrequencyMap.entrySet());
//        sortedWordFrequencyList.sort(Comparator.comparingInt(entry -> -entry.getValue()));
        sortedWordFrequencyList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        String mostCommonWord = sortedWordFrequencyList.get(0).getKey();
        int mostCommonWordCount = sortedWordFrequencyList.get(0).getValue();
        String rarestWord = sortedWordFrequencyList.get(sortedWordFrequencyList.size() - 1).getKey();
        int rarestWordCount = sortedWordFrequencyList.get(sortedWordFrequencyList.size() - 1).getValue();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("word_frequency.csv"))){

            writer.write("Word,Frequency,Frequency in %,Most common word,Count of most common word,Rarest word,Count of rarest word");
            writer.newLine();

            for (Map.Entry<String, Integer> entry : sortedWordFrequencyList) {
                String word = entry.getKey();
                int count = entry.getValue();
                // Округляем число до целого, затем умножаем на 100 и делим на 100, чтобы получить число с точностью до 2 знаков после запятой
                double frequencyPercent = Math.round((double) count * 100 / wordFrequencyMap.size() * 100.0) / 100.0;
                String percentWithPoint = String.valueOf(frequencyPercent).replace(',', '.') + "%";
                if (word.equals(mostCommonWord)) {
                    writer.write(String.format("%s, %d, %s, %s, %d, %s, %d", word, count, percentWithPoint, mostCommonWord, mostCommonWordCount, rarestWord, rarestWordCount));
                } else {
                    writer.write(String.format("%s, %d, %s", word, count, percentWithPoint));
                }
                writer.newLine();
            }
            writer.close();

        } catch (IOException ex) {
            System.err.println("Ошибка при записи в CSV-файл: " + ex);
        }
        System.out.println("CSV-файл успешно создан: word_frequency.csv");
    }
}
