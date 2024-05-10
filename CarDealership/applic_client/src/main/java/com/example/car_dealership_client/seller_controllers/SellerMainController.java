package com.example.car_dealership_client.seller_controllers;

import com.example.car_dealership_client.Main;
import com.example.car_dealership_client.client_controllers.ClientInProgressController;
import com.example.car_dealership_client.client_controllers.ClientInStockController;
import com.example.car_dealership_client.client_controllers.ClientSoldController;
import com.example.car_dealership_client.controllers.NameEnterController;
import com.example.car_dealership_client.models.ApplicationData;
import com.example.car_dealership_client.models.Seller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SellerMainController {
    private static final Logger loggerMain = LogManager.getLogger("MainLogger");
    @FXML
    Label sellerNameHeader;
    private Stage stage;
    private static String sellerName;
    private static Seller seller;
    private static List<ApplicationData> progress_applics;

    public static List<ApplicationData> get_all_applications() {
        return progress_applics;
    }

    public void setHeaderName(String sellerName) {
        SellerMainController.sellerName = sellerName;
        sellerNameHeader.setText("Welcome, " + sellerName + "!");
    }

    public void prepareMainMenu(Seller seller) {
        setHeaderName(sellerName);
        SellerMainController.seller = seller;
    }

    public void prepareMainMenu(Seller seller, List<ApplicationData> applics) {
        setHeaderName(sellerName);
        SellerMainController.seller = seller;
        progress_applics = applics;
    }

    public void prepareMainMenu(String sellerName, Seller seller) {
        setHeaderName(sellerName);
        SellerMainController.seller = seller;
        SellerMainController.sellerName = sellerName;
    }

    public void connect(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            seller = new Seller(socket, ois, oos);
            seller.sendNameToServer(sellerName);
            Thread sellerThread = new Thread(seller);
            sellerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onInStockButtonClicked(ActionEvent inStockClicked) throws IOException {
        loggerMain.info("От имени продавца {} нажата кнопка получения автомобилей в наличии", sellerName);
        stage = (Stage) ((Node) inStockClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("seller/cars_in_stock.fxml"));
        fxmlloader.load();
        SellerInStockController controller = fxmlloader.getController();
        controller.start(stage, seller, sellerName);
    }

    public void onMyActiveOrdersButtonClicked(ActionEvent buttonClicked) throws IOException {
        loggerMain.info("От имени продавца {} нажата кнопка получения автомобилей в процессе продажи", sellerName);
        stage = (Stage) ((Node) buttonClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/cars_in_progress.fxml"));
        fxmlLoader.load();
//        SellerInProgressController controller = fxmlLoader.getController();
//        controller.start(stage, seller, sellerName);
    }

    public void onMyCompletedOrdersButtonClicked(ActionEvent buttonClicked) throws IOException {
        loggerMain.info("От имени продавца {} нажата кнопка получения завершённых заказов автомобилей", sellerName);
        stage = (Stage) ((Node) buttonClicked.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("seller/cars_sold.fxml"));
        fxmlLoader.load();
//        SellerSoldController controller = fxmlLoader.getController();
//        controller.start(stage, seller, sellerName);
    }

    public void onLogOutButtonClicked(ActionEvent logOutClicked) throws IOException {
        stage = (Stage) ((Node) logOutClicked.getSource()).getScene().getWindow();
        stage.setTitle("OCDS: Online Car Dealership System | Login page");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        NameEnterController controller = loader.getController();
        controller.prepareEnterName();
        stage.setScene(scene);
        stage.show();
        seller.stop_connection();
    }

    public static void update_all_applications(List<ApplicationData> applics) {
        progress_applics = applics;
    }
}
