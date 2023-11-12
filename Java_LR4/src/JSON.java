import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSON {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static HashMap<String, String> createCharAnalysisMap(String fileName) {
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("Название файла", fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            HashMap<String, Integer> wordFrequencyMap = new HashMap<>();
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                text.append(line);
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

            resultMap.put("Общее количество символов", String.valueOf(text.length()));
            HashMap<Character, Integer> charFrequencyMap = new HashMap<>();
            for (char c : text.toString().toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    charFrequencyMap.put(c, charFrequencyMap.getOrDefault(c, 0) + 1);
                }
            }
            char mostCommonChar = 0;
            int mostCommonCharCount = 0;
            char rarestChar = 0;
            int rarestCharCount = Integer.MAX_VALUE;
            for (Map.Entry<Character, Integer> entry : charFrequencyMap.entrySet()) {
                char c = entry.getKey();
                int frequency = entry.getValue();

                if (frequency > mostCommonCharCount) {
                    mostCommonChar = c;
                    mostCommonCharCount = frequency;
                }
                if (frequency < rarestCharCount) {
                    rarestChar = c;
                    rarestCharCount = frequency;
                }
            }

            reader.close();
            reader = new BufferedReader(new FileReader(fileName));
            String line2;
            while ((line2 = reader.readLine()) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (char c : line2.toCharArray()) {
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

            ArrayList<Map.Entry<String, Integer>> sortedWordFrequencyList = new ArrayList<>(wordFrequencyMap.entrySet());
            sortedWordFrequencyList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            String mostCommonWord = sortedWordFrequencyList.get(0).getKey();
            int mostCommonWordCount = sortedWordFrequencyList.get(0).getValue();
            String rarestWord = sortedWordFrequencyList.get(sortedWordFrequencyList.size() - 1).getKey();
            int rarestWordCount = sortedWordFrequencyList.get(sortedWordFrequencyList.size() - 1).getValue();

            resultMap.put("Самый повторяющийся символ", String.valueOf(mostCommonChar));
            resultMap.put("Количество повторений самого частого символа", String.valueOf(mostCommonCharCount));
            resultMap.put("Самый редкий символ", String.valueOf(rarestChar));
            resultMap.put("Количество повторений самого редкого символа", String.valueOf(rarestCharCount));
            resultMap.put("Самое частое слово", mostCommonWord);
            resultMap.put("Количество повторений самого частого слова", String.valueOf(mostCommonWordCount));
            resultMap.put("Самое редкое слово", rarestWord);
            resultMap.put("Количество повторений самого редкого слова", String.valueOf(rarestWordCount));
        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при чтении текстового файла " + ex.getMessage() + ANSI_RESET);
        }
        return resultMap;
    }

    public static void writeCharAnalysisToJSON(HashMap<String, String> resultMap) {
        try (FileWriter writer = new FileWriter("frequency_analysis.json")) {
//            PrintWriter writer = new PrintWriter(resultMap.get("Название файла") + "_analysis.json");
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{").append("\n");
            boolean first = true;
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                if (!first) {
                    jsonBuilder.append(",").append("\n");
                }
                jsonBuilder.append("    \"").append(entry.getKey());
                jsonBuilder.append("\": \"").append(entry.getValue()).append("\"");
                first = false;
            }
            jsonBuilder.append("\n").append("}");
            System.out.println(jsonBuilder);
            writer.write(jsonBuilder.toString());
//            writer.write("{");
//            boolean first = true;
//            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
//                if (!first) {
//                    writer.write(",");
//                }
//                writer.write("\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"");
//                first = false;
//            }
//            writer.write("\n}");
        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при записи в JSON " + ex.getMessage() + ANSI_RESET);
        }
        System.out.println(ANSI_GREEN + "\tJSON-файл успешно создан для файла '" + resultMap.get("Название файла") + "': 'frequency_analysis.json'" + ANSI_RESET);
    }
}
