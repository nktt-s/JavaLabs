import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSV {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static HashMap<String, Integer> createWordFrequencyMap(String inputFileName) {
        HashMap<String, Integer> wordFrequencyMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))){
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder strBuilder = new StringBuilder();
                for (char c : line.toLowerCase().toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        strBuilder.append(c);
                    } else if (!strBuilder.isEmpty()) {
                        String word = strBuilder.toString();
                        wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                        strBuilder.setLength(0);
                    }
                }
                if (!strBuilder.isEmpty()) {
                    String word = strBuilder.toString();
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при чтении текстового файла: " + ex.getMessage() + ANSI_RESET);
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFileName.replaceAll("\\.[^.]+$", "") + "_analysis.csv"))){
            writer.write('\uFEFF');
            writer.write("Слово;Частота;Частота в %;Самое частое слово;Кол-во повторений самого частого слова;Самое редкое слово;Кол-во повторений самого редкого слова");
            writer.newLine();

            for (HashMap.Entry<String, Integer> entry : sortedWordFrequencyList) {
                String word = entry.getKey();
                int count = entry.getValue();
                double frequency = Math.round((double) count / wordFrequencyMap.size() * 1000.0) / 1000.0;
                // Округляем число до целого, затем умножаем на 100 и делим на 100, чтобы получить число с точностью до 2 знаков после запятой
                double frequencyPercent = Math.round((double) count * 100 / wordFrequencyMap.size() * 100.0) / 100.0;
                String percentWithPoint = String.valueOf(frequencyPercent).replace(',', '.') + "%";
                if (word.equals(mostCommonWord)) {
                    writer.write(String.format("%s; %f; %s; %s; %d; %s; %d",
                            word, frequency, percentWithPoint, mostCommonWord, mostCommonWordCount, rarestWord, rarestWordCount));
                } else {
                    writer.write(String.format("%s; %f; %s", word, frequency, percentWithPoint));
                }
                writer.newLine();
            }

        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при записи в CSV-файл: " + ex + ANSI_RESET + "\n");
        }
        System.out.print(ANSI_GREEN + "\n\t\tCSV-файл успешно создан: '" + inputFileName.replaceAll("\\.[^.]+$", "") + "_analysis.csv'" + ANSI_RESET);
    }
}
