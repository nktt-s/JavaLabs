import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSON {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static ArrayList<Object> createCharAnalysisList(String fileName) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(fileName); // Название файла

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

            list.add(String.valueOf(stringBuilder.length())); // Общее количество символов

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

            list.add(String.valueOf(mostCommonChar));       // Самый частый символ
            list.add(String.valueOf(mostCommonCharCount));  // Количество повторений самого частого символа
            list.add(String.valueOf(rarestChar));           // Самый редкий символ
            list.add(String.valueOf(rarestCharCount));      // Количество повторений самого редкого символа
            list.add(String.valueOf(mostCommonWord));       // Самое частое слово
            list.add(String.valueOf(mostCommonWordCount));  // Количество повторений самого частого слова
            list.add(String.valueOf(rarestWord));           // Самое редкое слово
            list.add(String.valueOf(rarestWordCount));      // Количество повторений самого редкого слова

        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при чтении текстового файла " + ex.getMessage() + ANSI_RESET);
        }
        return list;
    }

    public static void writeCharAnalysisToJSON(ArrayList<Object> list, boolean isFirst, boolean isLast) {
        try (FileWriter writer = new FileWriter("frequency_analysis.json", !isFirst)) {
            StringBuilder jsonBuilder = new StringBuilder();
            if (isFirst) {
                jsonBuilder.append("{\n");
            }
            jsonBuilder.append("\t\"").append(list.get(0)).append("\": {\n");
            jsonBuilder.append("\t\t\"Название файла\": \"").append(list.get(0));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Общее количество символов\": ").append(list.get(1));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самый частый символ\": \"").append(list.get(2));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого частого символа\": ").append(list.get(3));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самый редкий символ\": \"").append(list.get(4));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого редкого символа\": ").append(list.get(5));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самое частое слово\": \"").append(list.get(6));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого частого слова\": ").append(list.get(7));
            jsonBuilder.append(",\n");
            jsonBuilder.append("\t\t\"Самое редкое слово\": \"").append(list.get(8));
            jsonBuilder.append("\",\n");
            jsonBuilder.append("\t\t\"Количество повторений самого редкого слова\": ").append(list.get(9));
            if (isLast) {
                jsonBuilder.append("\n\t}\n}");
            } else {
                jsonBuilder.append("\n\t},\n");
            }
            writer.write(jsonBuilder.toString());

        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при записи в JSON " + ex.getMessage() + ANSI_RESET);
        }
        System.out.println(ANSI_GREEN + "\tJSON-файл успешно создан для файла '" + list.get(0) + "': 'frequency_analysis.json'" + ANSI_RESET);
    }

    public static void readFromJSON(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line.replaceAll("[{},]", ""));
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ANSI_RED + "JSON-файл с таким именем не найден! " + ex.getMessage() + ANSI_RESET);
        } catch (IOException ex) {
            System.err.println(ANSI_RED + "Ошибка при чтении JSON-файла! " + ex.getMessage() + ANSI_RESET);
        }
    }
}
