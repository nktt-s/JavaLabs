import java.io.*;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
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
                            System.out.println(ANSI_RED + "\t\tОшибка! Отсутствует имя исходного файла.");
                            System.out.println("\t  Введите его после ключа '--csv' и повторите попытку.\n" + ANSI_RESET);
                            return;
                        } else {
                            if (new File(args[1]).exists()) {
                                CSV.writeWordFrequencyToCSV(CSV.createWordFrequencyMap(args[1]), args[1]);
                                System.out.println();
                            } else {
                                System.out.println(ANSI_RED + "\t\t   Ошибка! Файл с таким именем не найден." + ANSI_RESET);
                            }
                            System.out.println();
                        }
                        return;

                    case "--csv-few":
                        if (args.length == 1) {
                            System.out.println(ANSI_RED + "\t\tОтсутствуют имена исходных файлов.");
                            System.out.println("\tВведите их после ключа '--csv-few' и повторите попытку.\n" + ANSI_RESET);
                            return;
                        } else {
                            for (int i = 1; i < args.length; ++i) {
                                if (new File(args[i]).exists()) {
                                    CSV.writeWordFrequencyToCSV(CSV.createWordFrequencyMap(args[i]), args[i]);
                                } else {
                                    System.out.println(ANSI_RED + "\t\tОшибка! Файл с именем '" + args[i] + "' не найден." + ANSI_RESET);
                                }
                            }
                            System.out.println();
                        }
                        return;

                    case "--json-write":
                        if (args.length == 1) {
                            System.out.println(ANSI_RED + "\tОтсутствуют имена исходных файлов.");
                            System.out.println("\tВведите их после ключа '--json-write' и повторите попытку.\n" + ANSI_RESET);
                        } else {
                            for (int i = 1; i < args.length; ++i) {
                                if (new File(args[i]).exists()) {
                                    JSON.writeToJSON(JSON.createCharAnalysisMap(args[i]), i == 1, i == args.length - 1);
                                } else {
                                    System.out.println(ANSI_RED + "\t\tОшибка! Файл с именем '" + args[i] + "' не найден." + ANSI_RESET);
                                }
                            }
                            System.out.println();
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
}
