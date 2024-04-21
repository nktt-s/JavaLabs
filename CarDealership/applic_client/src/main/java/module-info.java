module com.example.applic_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.dlsc.formsfx;
    requires org.apache.logging.log4j;

    opens com.example.car_dealership_client to javafx.fxml, javafx.base;
    opens com.example.car_dealership_client.controllers to javafx.fxml;
    exports com.example.car_dealership_client;
    exports com.example.car_dealership_client.models;
    opens com.example.car_dealership_client.client_controllers to javafx.fxml;
    opens com.example.car_dealership_client.seller_controllers to javafx.fxml;
    exports com.example.car_dealership_client.admin_controllers;
    opens com.example.car_dealership_client.admin_controllers to javafx.base, javafx.fxml;
}