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
            System.err.println("SQLException on getting all cars!");
//            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Car> getAllCarsByClient(String tableName, String clientName) {
        loggerDB.info("Вызван метод получения автомобилей клиента {} в процессе покупки", clientName);
        ArrayList<Car> cars = new ArrayList<>();
        String query;
        if (isValidTableName(tableName)) {
            query = "SELECT * FROM " + tableName + " WHERE buyer = '" + clientName + "'";
        } else {
            loggerDB.error("Недопустимое имя таблицы при вызове метода getAllCarsByClient!");
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String seller = resultSet.getString("seller");
                String buyer = resultSet.getString("buyer");
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
            System.err.println("SQLException on getting cars of client " + clientName + "!");
//            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Car> getAllCarsBySeller(String tableName, String sellerName) {
        loggerDB.info("Вызван метод получения автомобилей продавца {} в процессе покупки", sellerName);
        ArrayList<Car> cars = new ArrayList<>();
        String query;
        if (isValidTableName(tableName)) {
            query = "SELECT * FROM " + tableName + " WHERE seller = '" + sellerName + "'";
        } else {
            loggerDB.error("Недопустимое имя таблицы при вызове метода getAllCarsBySeller!");
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String seller = resultSet.getString("seller");
                String buyer = resultSet.getString("buyer");
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
            System.err.println("SQLException on getting cars of seller " + sellerName + "!");
//            e.printStackTrace();
            return null;
        }
    }

    public static int getNextId(String tableName) {
        loggerDB.info("Вызван метод получения свободного ID");
        String query;
        if (isValidTableName(tableName)) {
            query = "SELECT MAX(id) FROM " + tableName;
        } else {
            loggerDB.error("Недопустимое имя таблицы при вызове метода getNextId!");
            return 0;
        }

        int nextId = 0;
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement idStatement = connection.createStatement();
            ResultSet idResultSet = idStatement.executeQuery(query);

            if (idResultSet.next()) {
                nextId = idResultSet.getInt(DatabaseAttributes.ID.ordinal()) + 1;
            }

        } catch (SQLException e) {
            System.err.println("SQLException on getting next id!");
        }
        return nextId;
    }

    public static void addCar(Car car) {
        loggerDB.info("Вызван метод добавления автомобиля");
        String query = "INSERT INTO AllStockCars (id, seller, buyer, manufacturer, model, color, productionYear) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement idStatement = connection.createStatement();
            ResultSet idResultSet = idStatement.executeQuery("SELECT MAX(id) FROM AllStockCars");

            int nextId = 0;
            if (idResultSet.next()) {
                nextId = idResultSet.getInt(DatabaseAttributes.ID.ordinal()) + 1;
            }

            PreparedStatement prepStatement = connection.prepareStatement(query);

            prepStatement.setInt(DatabaseAttributes.ID.ordinal(), nextId);
            prepStatement.setString(DatabaseAttributes.SELLER.ordinal(), car.getSeller());
            prepStatement.setString(DatabaseAttributes.BUYER.ordinal(), car.getBuyer());
            prepStatement.setString(DatabaseAttributes.MANUFACTURER.ordinal(), car.getManufacturer());
            prepStatement.setString(DatabaseAttributes.MODEL.ordinal(), car.getModel());
            prepStatement.setString(DatabaseAttributes.COLOR.ordinal(), car.getColor());
            prepStatement.setInt(DatabaseAttributes.PRODUCTION_YEAR.ordinal(), car.getProductionYear());
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQLException on adding car!");
        }
    }

    public static void deleteCar(int id, String tableName) {
        loggerDB.info("Вызван метод удаления автомобиля");
        String query;
        if (isValidTableName(tableName)) {
            query = "DELETE FROM " + tableName + " WHERE id = ?";
        } else {
            loggerDB.error("Недопустимое имя таблицы при вызове метода deleteCar!");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            statement.execute();
            connection.close();

        } catch (SQLException e) {
            System.err.println("SQLException on deleting car!");
//            e.printStackTrace();
        }
    }

    public static void moveCarFromStockToInProgress(int id, String clientName) {
        loggerDB.info("Вызван метод перемещения автомобиля c ID = {} из Stock в InProgress", id);
        String query = "SELECT * FROM AllStockCars WHERE id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Statement idStatement = connection.createStatement();
                ResultSet idResultSet = idStatement.executeQuery("SELECT MAX(id) FROM AllInProgressCars");

                int nextId = 0;
                if (idResultSet.next()) {
                    nextId = idResultSet.getInt(DatabaseAttributes.ID.ordinal()) + 1;
                }

                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO AllInProgressCars (id, seller, buyer, manufacturer, model, color, productionYear) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStatement.setInt(DatabaseAttributes.ID.ordinal(), nextId);
                insertStatement.setString(DatabaseAttributes.SELLER.ordinal(), resultSet.getString("seller"));
                insertStatement.setString(DatabaseAttributes.BUYER.ordinal(), clientName);
                insertStatement.setString(DatabaseAttributes.MANUFACTURER.ordinal(), resultSet.getString("manufacturer"));
                insertStatement.setString(DatabaseAttributes.MODEL.ordinal(), resultSet.getString("model"));
                insertStatement.setString(DatabaseAttributes.COLOR.ordinal(), resultSet.getString("color"));
                insertStatement.setInt(DatabaseAttributes.PRODUCTION_YEAR.ordinal(), resultSet.getInt("productionYear"));
                insertStatement.executeUpdate();
            }

            deleteCar(id, "AllStockCars");

            connection.close();
        } catch (SQLException e) {
            System.err.println("SQLException on moving car from Stock to InProgress!");
//            e.printStackTrace();
        }
    }

    public static void moveCarFromInProgressToStock(int id) {
        loggerDB.info("Вызван метод перемещения автомобиля c ID = {} из InProgress в Stock", id);
        String query = "SELECT * FROM AllInProgressCars WHERE id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Statement idStatement = connection.createStatement();
                ResultSet idResultSet = idStatement.executeQuery("SELECT MAX(id) FROM AllStockCars");

                int nextId = 0;
                if (idResultSet.next()) {
                    nextId = idResultSet.getInt(DatabaseAttributes.ID.ordinal()) + 1;
                }

                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO AllStockCars (id, seller, buyer, manufacturer, model, color, productionYear) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStatement.setInt(DatabaseAttributes.ID.ordinal(), nextId);
                insertStatement.setString(DatabaseAttributes.SELLER.ordinal(), resultSet.getString("seller"));
                insertStatement.setString(DatabaseAttributes.BUYER.ordinal(), null);
                insertStatement.setString(DatabaseAttributes.MANUFACTURER.ordinal(), resultSet.getString("manufacturer"));
                insertStatement.setString(DatabaseAttributes.MODEL.ordinal(), resultSet.getString("model"));
                insertStatement.setString(DatabaseAttributes.COLOR.ordinal(), resultSet.getString("color"));
                insertStatement.setInt(DatabaseAttributes.PRODUCTION_YEAR.ordinal(), resultSet.getInt("productionYear"));
                insertStatement.executeUpdate();
            }

            deleteCar(id, "AllInProgressCars");

            connection.close();
        } catch (SQLException e) {
            System.err.println("SQLException on moving car from InProgress to Stock!");
//            e.printStackTrace();
        }
    }

    public static void moveCarFromInProgressToSold(int id) {
        loggerDB.info("Вызван метод перемещения автомобиля c ID = {} из InProgress в Sold", id);
        String query = "SELECT * FROM AllInProgressCars WHERE id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Statement idStatement = connection.createStatement();
                ResultSet idResultSet = idStatement.executeQuery("SELECT MAX(id) FROM AllSoldCars");

                int nextId = 0;
                if (idResultSet.next()) {
                    nextId = idResultSet.getInt(DatabaseAttributes.ID.ordinal()) + 1;
                }

                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO AllSoldCars (id, seller, buyer, manufacturer, model, color, productionYear) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStatement.setInt(DatabaseAttributes.ID.ordinal(), nextId);
                insertStatement.setString(DatabaseAttributes.SELLER.ordinal(), resultSet.getString("seller"));
                insertStatement.setString(DatabaseAttributes.BUYER.ordinal(), "buyer");
                insertStatement.setString(DatabaseAttributes.MANUFACTURER.ordinal(), resultSet.getString("manufacturer"));
                insertStatement.setString(DatabaseAttributes.MODEL.ordinal(), resultSet.getString("model"));
                insertStatement.setString(DatabaseAttributes.COLOR.ordinal(), resultSet.getString("color"));
                insertStatement.setInt(DatabaseAttributes.PRODUCTION_YEAR.ordinal(), resultSet.getInt("productionYear"));
                insertStatement.executeUpdate();
            }

            deleteCar(id, "AllInProgressCars");

            connection.close();
        } catch (SQLException e) {
            System.err.println("SQLException on moving car from InProgress to Sold!");
//            e.printStackTrace();
        }
    }

    public static Car getCar(int id, String tableName) {
        loggerDB.info("Вызван метод получения автомобиля");
        String query;
        if (isValidTableName(tableName)) {
            query = "SELECT * FROM " + tableName + " WHERE id = ?";
        } else {
            loggerDB.error("Недопустимое имя таблицы при вызове метода getCar!");
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
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
            loggerDB.error("Недопустимое имя таблицы при вызове метода updateCar!");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, car.getSeller());
            statement.setString(2, car.getBuyer());
            statement.setString(3, car.getManufacturer());
            statement.setString(4, car.getModel());
            statement.setString(5, car.getColor());
            statement.setInt(6, car.getProductionYear());
            statement.setInt(7, car.getId());

            statement.execute();
            connection.close();

        } catch (SQLException ex) {
            System.err.println("SQLException on updating car!");
        }
    }

    private static final Set<String> validTableNames = new HashSet<>(Arrays.asList("AllStockCars", "AllInProgressCars", "AllSoldCars"));

    private static boolean isValidTableName(String tableName) {
        return validTableNames.contains(tableName);
    }

    enum DatabaseAttributes {
        NONE, ID, SELLER, BUYER, MANUFACTURER, MODEL, COLOR, PRODUCTION_YEAR
    }


}
