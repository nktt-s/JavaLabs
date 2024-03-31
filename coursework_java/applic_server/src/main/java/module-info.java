module com.example.applic_server {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;
    requires org.slf4j;
    requires com.google.gson;
    requires jbcrypt;

    opens com.example.applic_server to javafx.fxml, com.google.gson;
    opens com.example.applic_server.models to javafx.fxml, com.google.gson;
    opens com.example.applic_server.controllers to javafx.fxml, com.google.gson;
    exports com.example.applic_server;
}