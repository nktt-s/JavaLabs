module com.example.javafx_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires mysql.connector.j;
    requires com.google.gson;


    opens com.example.javafx_project to javafx.fxml;
    exports com.example.javafx_project;
    exports com.example.javafx_project.devices;
    exports com.example.javafx_project.controllers;
    opens com.example.javafx_project.controllers to javafx.fxml;
    exports com.example.javafx_project.fileManagers;
    opens com.example.javafx_project.fileManagers to javafx.fxml;
}