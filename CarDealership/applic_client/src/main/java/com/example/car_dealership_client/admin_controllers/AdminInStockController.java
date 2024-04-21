package com.example.car_dealership_client.admin_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Admin;
import com.example.car_dealership_client.models.Car;
import com.example.car_dealership_client.models.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AdminInStockController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car, Integer> idColumn;
    @FXML
    private TableColumn<Car, String> sellerColumn;
    @FXML
    private TableColumn<Car, String> manufacturerColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, String> colorColumn;
    @FXML
    private TableColumn<Car, Integer> productionYearColumn;
    @FXML
    private TableColumn<Car, Button> editColumn;
    @FXML
    private TableColumn<Car, Button> deleteColumn;
    @FXML
    Button backButton;

    List<ApplicationData> waiting_applic;
    List<ApplicationData> rejected_applics;
    List<ApplicationData> finished_applics;
    //    Server server;
    Admin admin;
    String name;

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("cars_in_stock.fxml"));
        Parent root = fxmlLoader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
//        scrollPane = new ScrollPane();
        update_cars(scrollPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setResizable(false);
        stage.setTitle("OCDS: Online Car Dealership System | Cars in stock page");
        stage.setScene(scene);
        stage.show();
    }


    public void switchToMainMenu(ActionEvent go_back_clicked) throws IOException {
        loggerMain.info("Вызван метод возвращения в главное меню Администратора");
        Stage stage = (Stage) ((Node) go_back_clicked.getSource()).getScene().getWindow();

        FXMLLoader menuLoader = new FXMLLoader(Main.class.getResource("adm_main.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        AdmMainController menuController = menuLoader.getController();
//        menuController.prepare_main_menu(name, client);

        stage.setScene(menuScene);
        stage.show();

    }

    public void update_applications(ActionEvent accept_clicked) throws IOException {
        Stage stage = (Stage) ((Node) accept_clicked.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("cars_in_stock.fxml"));
        Parent root = loader.load();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        update_cars(scrollPane);

        Scene scene = new Scene(root);
//        AdminInStockController applicController = loader.getController();
//        applicController.update_cars(scrollPane);
//        applicController.prepare_applications(waiting_applic, name, admin);

        stage.setScene(scene);
        stage.show();
    }

    public void update_cars(ScrollPane scrollPane) {
        loggerMain.info("Запущен метод обновления таблицы автомобилей");
        ArrayList<Car> carsFromDB = DatabaseManager.getAllStockCars();

        if (carsFromDB == null) return;
        ObservableList<Car> cars = FXCollections.observableArrayList(carsFromDB);

        table = new TableView<>(cars);

        table.setStyle("-fx-font-size: 16px;");
        table.setPrefWidth(950);
        table.setPrefHeight(550);

        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(30.0);
        table.getColumns().add(idColumn);

        sellerColumn = new TableColumn<>("Seller");
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("seller"));
        sellerColumn.setPrefWidth(110.0);
        table.getColumns().add(sellerColumn);

        manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        manufacturerColumn.setPrefWidth(140.0);
        table.getColumns().add(manufacturerColumn);

        modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelColumn.setPrefWidth(85.0);
        table.getColumns().add(modelColumn);

        colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setPrefWidth(85.0);
        table.getColumns().add(colorColumn);

        productionYearColumn = new TableColumn<>("Production year");
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        productionYearColumn.setPrefWidth(150.0);
        table.getColumns().add(productionYearColumn);

        editColumn = new TableColumn<>("Edit");
        editColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    Scene currentScene = editButton.getScene();
                    Stage stage = (Stage) currentScene.getWindow();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_edit_car.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                        stage.setResizable(false);
                        stage.setTitle("OCDS: Online Car Dealership System | Edit car page");
                        stage.setScene(scene);
                        EditCar controller = fxmlLoader.getController();
                        loggerMain.info("Нажата кнопка изменения газонокосилки с ID = {}", car.getId());
                        controller.start(stage, car);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    update_cars(scrollPane);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
        editColumn.setPrefWidth(70.0);
        table.getColumns().add(editColumn);

        deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    int id = car.getId();
                    String manufacturer = car.getManufacturer();
                    String model = car.getModel();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("dealership.png"))));

                    alert.setTitle("Подтверждение удаления автомобиля");
                    alert.setHeaderText("Удаление автомобиля '" + manufacturer + model + "'");
                    alert.setContentText("Вы действительно хотите безвозвратно удалить автомобиль с ID = " + id + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        loggerMain.info("Удаление автомобиля с ID = {}", id);
                        DatabaseManager.deleteCar(id);
                        update_cars(scrollPane);
                    } else {
                        loggerMain.info("Отмена удаления устройства с ID = {}", id);
                        alert.close();
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        deleteColumn.setPrefWidth(90.0);
        table.getColumns().add(deleteColumn);
        scrollPane.setContent(table);

    }

    public void prepare_applications(List<ApplicationData> applic_data, String name, Admin admin) {
        waiting_applic = applic_data;
        this.name = name;
        this.admin = admin;
        render_applications();
    }

    public void render_applications() {
        render_buttons();
        render_applic_text();
//        System.out.println(waiting_applic);
    }

    public void render_applic_text() {
//        double fp_top_anchor = 10;
//        for (int i = 0; i < waiting_applic.size(); i++) {
//            ApplicationData curr = waiting_applic.get(i);
//            FlowPane fp = new FlowPane(0, 10);
//            fp.setPrefWidth(600);
//            List<Label> lbl_list = new ArrayList<>();
//            lbl_list.add(new Label(" " + curr.get_status()));
//            lbl_list.add(new Label(" " + curr.get_name()));
//            lbl_list.add(new Label(" " + curr.get_type()));
//            lbl_list.add(new Label(" " + curr.get_date()));
//            lbl_list.forEach(lbl -> {
//                lbl.setPrefWidth(70);
//                lbl.setStyle("-fx-border-color: grey;");
//            });
//            lbl_list.add(new Label(" " + curr.get_text()));
//            lbl_list.get(4).setPrefWidth(300);
//
//            lbl_list.forEach(lbl -> {
//                lbl.setStyle("-fx-border-color: grey;");
//                lbl.setPrefHeight(30);
//            });
//
//            fp.getChildren().addAll(lbl_list);
//            AnchorPane.setTopAnchor(fp, fp_top_anchor);
//            fp_top_anchor += 80;
//            AnchorPane.setLeftAnchor(fp, 10.0);
//            applics_anchor.getChildren().addAll(fp);
//        }
    }


    public void render_buttons() {
        List<Button> accept_btn_list = new ArrayList<>();
        List<Button> reject_btn_list = new ArrayList<>();
        List<ChoiceBox<String>> worker_choicebox_list = new ArrayList<>();
        for (int i = 0; i < waiting_applic.size(); i++) {
            ChoiceBox<String> worker_choice = new ChoiceBox<>();
            Button acc_btn = new Button();
            Button rej_btn = new Button();
            worker_choice.setPrefWidth(150);
            acc_btn.setPrefWidth(150);
            rej_btn.setPrefWidth(150);
            worker_choice.setId("worker_choice_" + i);
            acc_btn.setId("accept_button_" + i);
            rej_btn.setId("reject_button_" + i);

            acc_btn.setOnAction(event -> {
                try {
                    if (!(worker_choice.getValue() == null)) {
                        accept_application(event, acc_btn.getId(), worker_choice.getValue());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            rej_btn.setOnAction(event -> {
                try {
                    reject_application(event, acc_btn.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            acc_btn.setText("Accept");
            rej_btn.setText("Reject");
            worker_choice.getItems().addAll(AdmMainController.get_workers_list());

            accept_btn_list.add(acc_btn);
            reject_btn_list.add(rej_btn);
            worker_choicebox_list.add(worker_choice);
        }

        double top_padding = 43;
        double left_acc_padding = 175;
        double left_rej_padding = 375;
        double left_choice_padding = 10;
        for (int i = 0; i < waiting_applic.size(); i++) {
            AnchorPane.setTopAnchor(accept_btn_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(accept_btn_list.get(i), left_acc_padding);

            AnchorPane.setTopAnchor(worker_choicebox_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(worker_choicebox_list.get(i), left_choice_padding);

            AnchorPane.setTopAnchor(reject_btn_list.get(i), top_padding);
            AnchorPane.setLeftAnchor(reject_btn_list.get(i), left_rej_padding);
            top_padding += 80;
        }

//        applics_anchor.setCenterShape(true);
//        applics_anchor.getChildren().addAll(accept_btn_list);
//        applics_anchor.getChildren().addAll(reject_btn_list);
//        applics_anchor.getChildren().addAll(worker_choicebox_list);
    }

    public void accept_application(ActionEvent accept_clicked, String acc_id, String worker) throws IOException {
        int acc_app_num = Integer.parseInt(acc_id.substring(acc_id.length() - 1));
        waiting_applic.get(acc_app_num).set_status("In Progress");
        waiting_applic.get(acc_app_num).set_worker(worker);
        AdmMainController.add_progress_application(waiting_applic.get(acc_app_num));
        System.out.println("After settings and adding worker:" + waiting_applic.get(acc_app_num).get_worker());
        admin.sendApplicationToServer(waiting_applic.get(acc_app_num));
        waiting_applic.remove(acc_app_num);
        update_applications(accept_clicked);
//        System.out.println(waiting_applic.get());
    }

    public void reject_application(ActionEvent cancel_clicked, String acc_id) throws IOException {
        int acc_app_num = Integer.parseInt(acc_id.substring(acc_id.length() - 1));
        waiting_applic.get(acc_app_num).set_status("Rejected");
        AdmMainController.add_closed_application(waiting_applic.get(acc_app_num));
        admin.sendApplicationToServer(waiting_applic.get(acc_app_num));
        waiting_applic.remove(acc_app_num);
        update_applications(cancel_clicked);
    }

}
