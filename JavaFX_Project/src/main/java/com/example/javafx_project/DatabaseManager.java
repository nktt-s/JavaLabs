package com.example.javafx_project;

import com.example.javafx_project.devices.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager implements Serializable {
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

    public static ArrayList<GardeningDevice> getAllDevices() {
        ArrayList<GardeningDevice> devices = new ArrayList<>();
        String query = "SELECT * FROM Devices";

        try {
//            System.out.println(url + "\n" + login + "\n" + password);
//            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("Type");
                boolean isOn = resultSet.getBoolean("isOn");
                String manufacturer = resultSet.getString("Manufacturer");
                String model = resultSet.getString("Model");
                String powerSource = resultSet.getString("PowerSource");
                int productionYear = resultSet.getInt("ProductionYear");
                int lifetime = resultSet.getInt("Lifetime");

                switch (type) {
                    case "Lawnmower":
                        Lawnmower lawnmower = new Lawnmower(id, manufacturer, model, powerSource, productionYear, lifetime, isOn);
                        devices.add(lawnmower);
                        break;

                    case "AutoWatering":
                        AutoWatering autoWatering = new AutoWatering(id, manufacturer, model, powerSource, productionYear, lifetime, isOn);
                        devices.add(autoWatering);
                        break;

                    case "ThermalDrive":
                        ThermalDrive thermalDrive = new ThermalDrive(id, manufacturer, model, powerSource, productionYear, lifetime, isOn);
                        devices.add(thermalDrive);
                        break;
                }
            }
            connection.close();
            if (devices.isEmpty()) {
                return null;
            } else {
                return devices;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static GardeningDevice getDevice(int id) {
        String query = "SELECT * FROM devices WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idDevice = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String manufacturer = resultSet.getString("manufacturer");
                String model = resultSet.getString("model");
                String powerSource = resultSet.getString("powerSource");
                int productionYear = resultSet.getInt("productionYear");
                int lifetime = resultSet.getInt("lifetime");
                boolean isOn = resultSet.getBoolean("isOn");

                switch (type) {
                    case "Lawnmower":
                        return new Lawnmower(idDevice, manufacturer, model, powerSource, productionYear, lifetime, isOn);

                    case "AutoWatering":
                        return new AutoWatering(idDevice, manufacturer, model, powerSource, productionYear, lifetime, isOn);

                    case "ThermalDrive":
                        return new ThermalDrive(idDevice, manufacturer, model, powerSource, productionYear, lifetime, isOn);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteDevice(int id) {
        String deleteQuery = "DELETE FROM devices WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, login, password);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // System.out.println("Запись успешно обновлена");
                connection.close();
                return true;
            } else {
                // System.out.println("Запись не была обновлена");
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
