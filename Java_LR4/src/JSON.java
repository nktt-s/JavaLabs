import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
            HashMap<String, Integer> wordFrequencyMap = CSV.createWordFrequencyMap(fileName);

            String mostCommonWord = "";
            int mostCommonWordCount = 0;
            String rarestWord = "";
            int rarestWordCount = Integer.MAX_VALUE;

            for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
                String str = entry.getKey();
                int frequency = entry.getValue();

                if (frequency > mostCommonWordCount) {
                    mostCommonWord = str;
                    mostCommonWordCount = frequency;
                }
                if (frequency < rarestWordCount) {
                    rarestWord = str;
                    rarestWordCount = frequency;
                }
            }

            HashMap<Character, Integer> charFrequencyMap = new HashMap<>();
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line.toLowerCase());
            }

            for (char c : stringBuilder.toString().toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        charFrequencyMap.put(c, charFrequencyMap.getOrDefault(c, 0) + 1);
                    }
            }

            resultMap.put("Общее количество символов", String.valueOf(stringBuilder.length()));

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

            resultMap.put("Самый частый символ", String.valueOf(mostCommonChar));
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

    public static void writeToJSON(HashMap<String, String> resultMap, boolean isFirst, boolean isLast) {
        try (FileWriter writer = new FileWriter("frequency_analysis.json", !isFirst)) {
            StringBuilder jsonBuilder = new StringBuilder();
            if (isFirst) {
                jsonBuilder.append("{\n");
            }
            jsonBuilder.append("\t\"").append(resultMap.get("Название файла")).append("\": {\n");
            jsonBuilder.append("\t\t\"Название файла\": \"").append(resultMap.get("Название файла"));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Общее количество символов\": ").append(resultMap.get("Общее количество символов"));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самый частый символ\": \"").append(resultMap.get("Самый частый символ"));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого частого символа\": ").append(resultMap.get("Количество повторений самого частого символа"));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самый редкий символ\": \"").append(resultMap.get("Самый редкий символ"));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого редкого символа\": ").append(resultMap.get("Количество повторений самого редкого символа"));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самое частое слово\": \"").append(resultMap.get("Самое частое слово"));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого частого слова\": ").append(resultMap.get("Количество повторений самого частого слова"));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самое редкое слово\": \"").append(resultMap.get("Самое редкое слово"));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого редкого слова\": ").append(resultMap.get("Количество повторений самого редкого слова"));
            if (isLast) {
                jsonBuilder.append("\n\t}\n}");
            } else {
                jsonBuilder.append("\n\t},\n");
            }
//            System.out.println(jsonBuilder);
            writer.write(jsonBuilder.toString());

        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при записи в JSON " + ex.getMessage() + ANSI_RESET);
        }
        System.out.println(ANSI_GREEN + "\tJSON-файл успешно создан для файла '" + resultMap.get("Название файла") + "': 'frequency_analysis.json'" + ANSI_RESET);
    }
}
