package com.example.javafx_project.fileManagers;

public class FileManager {
        public static int getTypeOfFile(String fileName) {
        if (fileName.matches("[a-zA-Z0-9\\-_]+\\.dat")) {
            return 1;
        } else if (fileName.matches("[a-zA-Z0-9\\-_]+\\.json")) {
            return 2;
        } else return 0;
    }
}
