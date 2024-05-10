package com.example.car_dealership_client.controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.admin_controllers.AdminMainController;
import com.example.car_dealership_client.client_controllers.ClientMainController;
import com.example.car_dealership_client.models.Client;
import com.example.car_dealership_client.models.Admin;
import com.example.car_dealership_client.models.Seller;
import com.example.car_dealership_client.seller_controllers.SellerMainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NameEnterController {
    @FXML
    ChoiceBox<String> userTypeChoice;
    @FXML
    TextField nameTextField;
    @FXML
    Label enterNameLabel;
    @FXML
    AnchorPane nameEnterAnchor;
    ObservableList<String> user_types = FXCollections.observableArrayList();
    private Stage stage;
    private Client client;
    private Admin admin;
    private Seller seller;
    @FXML
    Label passwordLabel;
    @FXML
    Label passwordLabelAuth;
    @FXML
    PasswordField userPassword;
    @FXML
    PasswordField userPasswordConfirmation;
    @FXML
    Button loginButton;
    @FXML
    Button signUpButton;
    Button backButton = new Button();
    Boolean signingUp = false;
    Socket createSocket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public void prepareEnterName() throws IOException {
        createSocket = new Socket("localhost", 3308);
        oos = new ObjectOutputStream(createSocket.getOutputStream());
        ois = new ObjectInputStream(createSocket.getInputStream());
        nameTextField.requestFocus();
        prepareTypeChoice();
        prepareFormElements();
        loadUserLoginForm();
    }

    public void signUpEnter(ActionEvent signUpClicked) throws IOException, ClassNotFoundException {
        String name = nameTextField.getText();

        if (name.length() > 9) {
            enterNameLabel.setText("Enter shorter name!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (name.isEmpty()) {
            enterNameLabel.setText("Your name cannot be empty!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (userTypeChoice.getValue() == null) {
            enterNameLabel.setText("You didn't choose your role!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (!userPassword.getText().equals(userPasswordConfirmation.getText())) {
            enterNameLabel.setText("Passwords are not equal!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (userPassword.getText().isEmpty()) {
            enterNameLabel.setText("Your name cannot be empty!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (userTypeChoice.getValue().equals("Client")) {
            registerClient(signUpClicked, name, userPassword.getText());
        } else if (userTypeChoice.getValue().equals("Seller")) {
            registerSeller(signUpClicked, name, userPassword.getText());
        }
    }

    public void registerClient(ActionEvent event, String name, String password) throws IOException, ClassNotFoundException {
        checkWithServer("Signup", name, password, event);
    }

    public void registerSeller(ActionEvent event, String name, String password) throws IOException, ClassNotFoundException {
        checkWithServer("Signup", name, password, event);
    }

    public void loginEnter(ActionEvent enterNameClicked) throws IOException, ClassNotFoundException {

        String name = nameTextField.getText();
        if (name.length() > 9) {
            enterNameLabel.setText("Enter shorter name!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (name.isEmpty()) {
            enterNameLabel.setText("Your name cannot be empty!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (userTypeChoice.getValue() == null) {
            enterNameLabel.setText("You didn't choose your role!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        } else if (userTypeChoice.getValue().equals("Admin")) {
            checkWithServer("Login", "Admin", userPassword.getText(), enterNameClicked);
        } else {
            checkWithServer("Login", name, userPassword.getText(), enterNameClicked);
        }
    }

    public void loadUser(ActionEvent event, String name, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        switch (userTypeChoice.getValue()) {
            case "Client":
                loadClient(event, name, socket, ois, oos);
                break;
            case "Admin":
                loadAdmin(event, name, socket, ois, oos);
                break;
            case "Seller":
                loadSeller(event, name, socket, ois, oos);
                break;
        }
    }

    public void loadAdmin(ActionEvent enterNameClicked, String adminName, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        stage = (Stage) ((Node) enterNameClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        AdminMainController controller = fxmlLoader.getController();
        controller.prepareMainMenu(adminName, admin);
        controller.connect(socket, ois, oos);
        stage.setScene(scene);
        stage.setTitle("OCDS: Online Car Dealership System | Admin Main page");
        stage.show();
    }

    public void loadClient(ActionEvent enterNameClicked, String clientName, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        stage = (Stage) ((Node) enterNameClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client/client_main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        ClientMainController controller = fxmlLoader.getController();
        controller.prepareMainMenu(clientName, client);
        controller.connect(socket, ois, oos);
        stage.setScene(scene);
        stage.setTitle("OCDS: Online Car Dealership System | Client Main page");
        stage.show();
    }

    // TODO
    public void loadSeller(ActionEvent enterNameClicked, String sellerName, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        stage = (Stage) ((Node) enterNameClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/seller_main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        SellerMainController controller = fxmlLoader.getController();
        controller.prepareMainMenu(sellerName, seller);
        controller.connect(socket, ois, oos);
        stage.setScene(scene);
        stage.setTitle("OCDS: Online Car Dealership System | Seller Main page");
        stage.show();
    }

    public void prepareFormElements() {
        loginButton.setOnAction(event -> {
            try {
                loginEnter(event);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        signUpButton.setOnAction(event -> {
            try {
                signUpButtonClicked(event);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        backButton.setPrefWidth(80);
        backButton.setText("Go back");
        backButton.setOnAction(event -> loadUserLoginForm());
    }


    public void clearForm() {
        nameEnterAnchor.getChildren().removeAll(userPassword, userPasswordConfirmation, loginButton, signUpButton, passwordLabel, passwordLabelAuth);
    }

    public void prepareTypeChoice() {
        user_types.addAll("Admin", "Client", "Seller");
        userTypeChoice.setItems(user_types);
        userTypeChoice.setValue("Client");
        userTypeChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Action to be taken when choice box value changes
                switch (newValue) {
                    case "Client", "Seller":
                        loadUserLoginForm();
                        break;
                    case "Admin":
                        loadAdminLoginForm();
                        break;
                }
            }
        });
    }

    public void loadUserLoginForm() {
        signingUp = false;
        clearForm();

        nameEnterAnchor.setCenterShape(true);
        nameEnterAnchor.getChildren().addAll(userPassword, loginButton, signUpButton, passwordLabel);
    }

    public void signUpButtonClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        if (signingUp) {
            signUpEnter(event);
        } else {
            loadSignUpForm();
        }
    }

    public void loadSignUpForm() {
        clearForm();
        signingUp = true;
        passwordLabelAuth.setVisible(true);
        userPasswordConfirmation.setVisible(true);

        nameEnterAnchor.setCenterShape(true);
        nameEnterAnchor.getChildren().addAll(passwordLabel, userPassword, passwordLabelAuth, userPasswordConfirmation, signUpButton);
    }

    public void loadAdminLoginForm() {
        signingUp = false;
        clearForm();
        nameEnterAnchor.getChildren().addAll(passwordLabel, userPassword, loginButton);
    }

    public void checkWithServer(String desire, String name, String password, ActionEvent event) throws IOException, ClassNotFoundException {
        oos.writeObject(desire);
        oos.writeObject(name);
        oos.writeObject(userTypeChoice.getValue());
        oos.writeObject(password);
        switch ((String) ois.readObject()) {
            case "Correct":
                loadUser(event, name, createSocket, ois, oos);
                break;
            case "Incorrect":
                enterNameLabel.setText("Incorrect login or password!");
                enterNameLabel.setTextFill(Paint.valueOf("red"));
                break;
            case "Created":
                loadUserLoginForm();
                break;
            case "Exist":
                enterNameLabel.setText("This account already exists!");
                enterNameLabel.setTextFill(Paint.valueOf("red"));
                loadUserLoginForm();
                break;
        }
    }
};
