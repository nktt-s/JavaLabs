package com.example.car_dealership_client.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final Logger loggerDB = LogManager.getLogger("DatabaseLogger");
    static String url = "jdbc:mysql://localhost:3307/";
    static String name;
    static String login;
    static String password;

    public static void setValuesForConnection(String databaseName, String databaseLogin, String databasePassword) {
        url += databaseName;
        name = databaseName;
        login = databaseLogin;
        password = databasePassword;
    }

    public static ArrayList<Car> getAllStockCars() {
        loggerDB.info("Вызван метод получения всех автомобилей");
        ArrayList<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM AllStockCars";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String seller = resultSet.getString("seller");
                String manufacturer = resultSet.getString("manufacturer");
                String model = resultSet.getString("model");
                String color = resultSet.getString("color");
                int productionYear = resultSet.getInt("productionYear");

                Car car = new Car(id, seller, manufacturer, model, color, productionYear);
                cars.add(car);
            }
            connection.close();
            if (cars.isEmpty()) {
                return null;
            } else {
                return cars;
            }

        } catch (SQLException e) {
            System.err.println("SQLException on getting cars!");
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteCar(int id) {
        loggerDB.info("Вызван метод удаления автомобиля");
        String query = "DELETE FROM AllStockCars WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            connection.close();

        } catch (SQLException e) {
            System.err.println("SQLException on deleting car!");
//            e.printStackTrace();
        }
    }

    public static Car getCar(int id) {
        loggerDB.info("Вызван метод добавления одного автомобиля");
        String query = "SELECT * FROM AllStockCars WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idCar = resultSet.getInt("id");
                String seller = resultSet.getString("seller");
                String manufacturer = resultSet.getString("manufacturer");
                String model = resultSet.getString("model");
                String color = resultSet.getString("color");
                int productionYear = resultSet.getInt("productionYear");
                return new Car(idCar, seller, manufacturer, model, color, productionYear);
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("SQLException on getting car!");
//            e.printStackTrace();
        }
        return null;
    }

    public static void updateCar(Car car) {
        loggerDB.info("Вызван метод изменения автомобиля");
        String query = "UPDATE AllStockCars SET seller = ?, manufacturer = ?, model = ?, color = ?, productionYear = ? WHERE id = ?;";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, car.getSeller());
            prepStatement.setString(2, car.getManufacturer());
            prepStatement.setString(3, car.getModel());
            prepStatement.setString(4, car.getColor());
            prepStatement.setInt(5, car.getProductionYear());
            prepStatement.setInt(6, car.getId());

            prepStatement.execute();
            connection.close();

        } catch (SQLException ex) {
            System.err.println("SQLException on updating car!");
        }
    }

    enum DatabaseAttributes {
        NONE, ID, SELLER, MANUFACTURER, MODEL, COLOR, PRODUCTION_YEAR
    }


}
