module com.example.applic_client {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.car_dealership_client to javafx.fxml;
    opens com.example.car_dealership_client.controllers to javafx.fxml;
    exports com.example.car_dealership_client;
    opens com.example.car_dealership_client.client_controllers to javafx.fxml;
    opens com.example.car_dealership_client.admin_controllers to javafx.fxml;
    opens com.example.car_dealership_client.seller_controllers to javafx.fxml;
}