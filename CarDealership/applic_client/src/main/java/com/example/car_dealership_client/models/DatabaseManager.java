package com.example.car_dealership_client.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public static ArrayList<Car> getAllCars(String tableName) {
        loggerDB.info("Вызван метод получения всех автомобилей");
        ArrayList<Car> cars = new ArrayList<>();
        String query;
        if (isValidTableName(tableName)) {
            query = "SELECT * FROM " + tableName;
        } else {
            System.err.println("Недопустимое имя таблицы при вызове метода getAllCars!");
            loggerDB.error("Недопустимое имя таблицы при вызове метода getAllCars!");
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String seller = resultSet.getString("seller");
                String buyer;
                if (tableName.equals("AllStockCars")) {
                    buyer = null;
                } else {
                    buyer = resultSet.getString("buyer");
                }
                String manufacturer = resultSet.getString("manufacturer");
                String model = resultSet.getString("model");
                String color = resultSet.getString("color");
                int productionYear = resultSet.getInt("productionYear");

                Car car = new Car(id, seller, buyer, manufacturer, model, color, productionYear);
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
//            e.printStackTrace();
            return null;
        }
    }

    public static void deleteCar(int id, String tableName) {
        loggerDB.info("Вызван метод удаления автомобиля");
        String query;
        if (isValidTableName(tableName)) {
            query = "DELETE FROM " + tableName + " WHERE id = ?";
        } else {
            System.err.println("Недопустимое имя таблицы при вызове метода deleteCar!");
            loggerDB.error("Недопустимое имя таблицы при вызове метода deleteCar!");
            return;
        }

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

    public static Car getCar(int id, String tableName) {
        loggerDB.info("Вызван метод добавления одного автомобиля");
        String query;
        if (isValidTableName(tableName)) {
            query = "SELECT * FROM " + tableName + " WHERE id = ?";
        } else {
            System.err.println("Недопустимое имя таблицы при вызове метода getCar!");
            loggerDB.error("Недопустимое имя таблицы при вызове метода getCar!");
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idCar = resultSet.getInt("id");
                String seller = resultSet.getString("seller");
                String buyer = resultSet.getString("buyer");
                String manufacturer = resultSet.getString("manufacturer");
                String model = resultSet.getString("model");
                String color = resultSet.getString("color");
                int productionYear = resultSet.getInt("productionYear");
                return new Car(idCar, seller, buyer, manufacturer, model, color, productionYear);
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("SQLException on getting car!");
//            e.printStackTrace();
        }
        return null;
    }

    public static void updateCar(Car car, String tableName) {
        loggerDB.info("Вызван метод изменения автомобиля");
        String query;
        if (isValidTableName(tableName)) {
            query = "UPDATE " + tableName + " SET seller = ?, buyer = ?, manufacturer = ?, model = ?, color = ?, productionYear = ? WHERE id = ?;";
        } else {
            System.err.println("Недопустимое имя таблицы при вызове метода updateCar!");
            loggerDB.error("Недопустимое имя таблицы при вызове метода updateCar!");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, car.getSeller());
            prepStatement.setString(2, car.getBuyer());
            prepStatement.setString(3, car.getManufacturer());
            prepStatement.setString(4, car.getModel());
            prepStatement.setString(5, car.getColor());
            prepStatement.setInt(6, car.getProductionYear());
            prepStatement.setInt(7, car.getId());

            prepStatement.execute();
            connection.close();

        } catch (SQLException ex) {
            System.err.println("SQLException on updating car!");
        }
    }

    private static final Set<String> validTableNames = new HashSet<>(Arrays.asList(
        "AllStockCars",
        "AllInProgressCars"
    ));

    private static boolean isValidTableName(String tableName) {
        return validTableNames.contains(tableName);
    }

    enum DatabaseAttributes {
        NONE, ID, SELLER, MANUFACTURER, MODEL, COLOR, PRODUCTION_YEAR
    }


}
