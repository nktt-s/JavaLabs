import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Использование: java WordFrequencyAnalyzer <имя_текстового_файла>");
            return;
        }

        String inputFileName = args[0];
        File inputFile = new File("C:/Users/nktt/IdeaProjects/Java_LR4/src", "text.txt");
        String outputFileName = "word_frequency.csv";

        try {
            if (!inputFile.exists()) {
                System.out.println("Ошибка! Исходного файла не существует.");
                System.out.println("Работа программы будет завершена.");
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            Map<String, Integer> wordFrequencyMap = new TreeMap<>();
            String line;

            while ((line = reader.readLine()) != null) {
                StringBuilder wordBuilder = new StringBuilder();
                for (int i = 0; i < line.length(); ++i) {
                    char c = line.charAt(i);
                    if (Character.isLetterOrDigit(c)) {
                        wordBuilder.append(c);
                    } else {
                        if (!wordBuilder.isEmpty()) {
                            String word = wordBuilder.toString().toLowerCase();
                            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                            wordBuilder.setLength(0); // Очищаем StringBuilder для следующего слова
                        }
                    }
                }
                if (!wordBuilder.isEmpty()) {
                    String word = wordBuilder.toString().toLowerCase();
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
            reader.close();

            // Определяем самое частое и самое редкое слово
            String mostCommonWord = null;
            int mostCommonWordCount = 0;
            String leastCommonWord = null;
            int leastCommonWordCount = Integer.MAX_VALUE;

            for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();

                if (count > mostCommonWordCount) {
                    mostCommonWord = word;
                    mostCommonWordCount = count;
                }

                if (count < leastCommonWordCount) {
                    leastCommonWord = word;
                    leastCommonWordCount = count;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
            writer.write("Word,Frequency,Frequency in %,Most frequently word,Count most frequently word,Less frequently word,Count less frequently word");
            writer.newLine();
            System.out.println("Size of map: " + wordFrequencyMap.size());
            for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();
                double frequencyPercent = (double) count / wordFrequencyMap.size();
                writer.write(String.format("%s, %d, %.4f, %s, %d, %s, %d", word, count, frequencyPercent, mostCommonWord, mostCommonWordCount, leastCommonWord, leastCommonWordCount));
                writer.newLine();
            }

            writer.close();
            System.out.println("CSV-файл создан: " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
