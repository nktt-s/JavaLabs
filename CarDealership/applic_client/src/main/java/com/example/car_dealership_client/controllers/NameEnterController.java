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
    ChoiceBox<String> user_type_choice;
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
    Label pass_label;
    @FXML
    Label pass_label_auth;
    @FXML
    PasswordField user_password;
    @FXML
    PasswordField user_password_confirmation;
    @FXML
    Button login_button;
    @FXML
    Button signup_button;
    Button back_button = new Button();
    Boolean signing_up = false;
    Socket createSocket;
    ObjectOutputStream oos;
    ObjectInputStream ois;


    public void prepare_enter_name() throws IOException {
        createSocket = new Socket("localhost", 3308);
        oos = new ObjectOutputStream(createSocket.getOutputStream());
        ois = new ObjectInputStream(createSocket.getInputStream());
        nameTextField.requestFocus();
        prepare_type_choice();
        prepare_form_elements();
        loadUserLoginForm();

//        user_type_choice.AddListener

    }

    public void signup_enter(ActionEvent signUpClicked) throws IOException, ClassNotFoundException {
        String name = nameTextField.getText();

        if (name.length() > 9) {
            enterNameLabel.setText("Enter shorter name!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (name.isEmpty()) {
            enterNameLabel.setText("Your name cannot be empty!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (user_type_choice.getValue() == null) {
            enterNameLabel.setText("You didn't choose your role!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (!user_password.getText().equals(user_password_confirmation.getText())) {
            enterNameLabel.setText("Passwords are not equal!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (user_password.getText().isEmpty()) {
            enterNameLabel.setText("Your name cannot be empty!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (user_type_choice.getValue().equals("Client")) {
            register_client(signUpClicked, name, user_password.getText());
        }
        else if (user_type_choice.getValue().equals("Seller")) {
            register_seller(signUpClicked, name, user_password.getText());
        }
    }

    public void register_client(ActionEvent event, String name, String password) throws IOException, ClassNotFoundException {
        check_with_server("Signup", name, password, event);
    }

    public void register_seller(ActionEvent event, String name, String password) throws IOException, ClassNotFoundException {
        check_with_server("Signup", name, password, event);

    }

    public void login_enter(ActionEvent enterNameClicked) throws IOException, ClassNotFoundException {

        String name = nameTextField.getText();
        if (name.length() > 9) {
            enterNameLabel.setText("Enter shorter name!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (name.isEmpty()) {
            enterNameLabel.setText("Your name cannot be empty!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (user_type_choice.getValue() == null) {
            enterNameLabel.setText("You didn't choose your role!");
            enterNameLabel.setTextFill(Paint.valueOf("red"));
        }
        else if (user_type_choice.getValue().equals("Admin")) {
            check_with_server("Login", "Admin", user_password.getText(), enterNameClicked);
        }
        else {
            check_with_server("Login", name, user_password.getText(), enterNameClicked);
        }
    }

    public void load_user(ActionEvent event, String name, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        switch (user_type_choice.getValue()) {
            case "Client":
                load_client(event, name, socket, ois, oos);
                break;
            case "Admin":
                load_admin(event, name, socket, ois, oos);
                break;
            case "Seller":
                load_seller(event, name, socket, ois, oos);
                break;
        }
    }

    public void load_client(ActionEvent enterNameClicked, String inp_name, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        stage = (Stage) ((Node) enterNameClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        ClientMainController menuController = fxmlLoader.getController();
        menuController.prepare_main_menu(inp_name, client);
        menuController.connect(socket, ois, oos);
        stage.setScene(scene);
        stage.setTitle("OCDS: Online Car Dealership System | Client page");
        stage.show();
    }

    public void load_admin(ActionEvent enterNameClicked, String inp_name, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        stage = (Stage) ((Node) enterNameClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adm_main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        AdminMainController controller = fxmlLoader.getController();
        controller.prepare_main_menu(inp_name, admin);
        controller.connect(socket, ois, oos);
        stage.setScene(scene);
        stage.setTitle("OCDS: Online Car Dealership System | Admin page");
        stage.show();
    }


    public void load_seller(ActionEvent enterNameClicked, String inp_name, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        stage = (Stage) ((Node) enterNameClicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Seller page");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller_main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        SellerMainController controller = fxmlLoader.getController();
        controller.prepare_main_menu(inp_name, seller);
        controller.connect(socket, ois, oos);
        stage.setScene(scene);
        stage.show();
    }

    public void prepare_form_elements() {
        login_button.setOnAction(event -> {
            try {
                login_enter(event);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        signup_button.setOnAction(event -> {
            try {
                signupButtonClicked(event);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        back_button.setPrefWidth(80);
        back_button.setText("Go back");
        back_button.setOnAction(event -> loadUserLoginForm());
    }


    public void clearForm() {
        nameEnterAnchor.getChildren().removeAll(user_password, user_password_confirmation, login_button,
            signup_button, pass_label, pass_label_auth);
    }

    public void prepare_type_choice() {
        user_types.addAll("Admin", "Client", "Seller");
        user_type_choice.setItems(user_types);
        user_type_choice.setValue("Client");
        user_type_choice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
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
        signing_up = false;
        clearForm();

        nameEnterAnchor.setCenterShape(true);
        nameEnterAnchor.getChildren().addAll(user_password, login_button, signup_button, pass_label);
    }

    public void signupButtonClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        if (signing_up) {
            signup_enter(event);
        } else {
//          name_enter(event);
            loadSignupForm();

        }
    }

    public void loadSignupForm() {
        clearForm();
        signing_up = true;
        pass_label_auth.setVisible(true);
        user_password_confirmation.setVisible(true);

        nameEnterAnchor.setCenterShape(true);
        nameEnterAnchor.getChildren().addAll(pass_label, user_password, pass_label_auth, user_password_confirmation, signup_button);

    }

    public void loadAdminLoginForm() {
        signing_up = false;
        clearForm();
        nameEnterAnchor.getChildren().addAll(pass_label, user_password, login_button);


    }

    public void check_with_server(String desire, String name, String password, ActionEvent event) throws IOException, ClassNotFoundException {
        oos.writeObject(desire);
        oos.writeObject(name);
        oos.writeObject(user_type_choice.getValue());
        oos.writeObject(password);
        switch ((String) ois.readObject()) {
            case "Correct":
                load_user(event, name, createSocket, ois, oos);
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
//        close_everything(createSocket, oos, ois);
    }

//    public void close_everything(Socket socket, ObjectOutputStream oos, ObjectInputStream ois){
//        try {
//            socket.close();
//            oos.close();
//            ois.close();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//    }
};
