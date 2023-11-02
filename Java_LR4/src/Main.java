import javax.xml.stream.FactoryConfigurationError;
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
                        if (args.length == 1) {
                            System.out.println("\tОтсутствует имя исходного файла.");
                            System.out.println("\tВведите его после ключа '--csv' и повторите попытку.\n");
                            return;
                        } else {
                            //TODO РЕАЛИЗОВАТЬ ПРОВЕРКУ НА ОТСУТСТВИЕ ФАЙЛА С ТАКИМ ИМЕНЕМ
                            writeWordFrequencyToCSV(createWordFrequencyMap(args[1]), args[1]);
                            System.out.println();
                        }
                        return;

                    case "--csv-few":
                        if (args.length == 1) {
                            System.out.println("\tОтсутствуют имена исходных файлов.");
                            System.out.println("\tВведите их после ключа '--csv-few' и повторите попытку.\n");
                            return;
                        } else {
                            //TODO РЕАЛИЗОВАТЬ ПРОВЕРКУ НА ОТСУТСТВИЕ ФАЙЛА С ТАКИМ ИМЕНЕМ
                            for (int i = 1; i < args.length; ++i) {
                                writeWordFrequencyToCSV(Objects.requireNonNull(createWordFrequencyMap(args[i])), args[i]);
                            }
                            System.out.println();
                        }
                        return;

                    case "--json-write":
                        if (args.length == 1) {
                            System.out.println("\tОтсутствуют имена исходных файлов.");
                            System.out.println("\tВведите их после ключа '--json-write' и повторите попытку.\n");
                        } else {
                            //TODO РЕАЛИЗОВАТЬ ПРОВЕРКУ НА ОТСУТСТВИЕ ФАЙЛА С ТАКИМ ИМЕНЕМ
                            for (int i = 1; i < args.length; ++i) {
                                writeCharAnalysisToJSON(createCharAnalysisMap(args[i]));
                            }
                        }
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
        System.out.println("CSV-файл успешно создан: " + inputFileName + "_frequency_analysis.csv");
    }

    public static HashMap<String, String> createCharAnalysisMap(String fileName) {
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("Название файла", fileName);

        //TODO РЕАЛИЗОВАТЬ МАССИВ ОБЪЕКТОВ В ФАЙЛЕ JSON (по требованиям)

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                text.append(line);
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

            resultMap.put("Самый повторяющийся символ", String.valueOf(mostCommonChar));
            resultMap.put("Количество повторений самого частого символа", String.valueOf(mostCommonCharCount));
            resultMap.put("Самый редкий символ", String.valueOf(rarestChar));
            resultMap.put("Количество повторений самого редкого символа", String.valueOf(rarestCharCount));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return resultMap;
    }

    public static void writeCharAnalysisToJSON(HashMap<String, String> resultMap) {
        try {
            PrintWriter writer = new PrintWriter(resultMap.get("Название файла") + "_analysis.json");
            writer.println("{");
            boolean first = true;
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                if (!first) {
                    writer.println(",");
                }
                writer.print("\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"");
                first = false;
            }
            writer.println("\n}");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println("JSON-файл успешно создан для файла " + resultMap.get("Название файла") + ": " + resultMap.get("Название файла") + "_analysis.json");
    }
}
