package com.example.javafx_project;

import com.example.javafx_project.devices.*;
import com.example.javafx_project.devices.GardeningDevice;

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
        String query = "SELECT * FROM AllDevices";

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

                int cuttingHeight = resultSet.getInt("CuttingHeight");
                boolean isMulchingEnabled = resultSet.getBoolean("isMulchingEnabled");

                int waterPressure = resultSet.getInt("WaterPressure");
                boolean isSprinklerAttached = resultSet.getBoolean("isSprinklerAttached");
                boolean isWinterMode = resultSet.getBoolean("isWinterMode");

                int temperature = resultSet.getInt("Temperature");
                boolean isProtectiveFunctionOn = resultSet.getBoolean("isProtectiveFunctionOn");

                switch (type) {
                    case "Lawnmower":
                        Lawnmower lawnmower = new Lawnmower(id, manufacturer, model, powerSource, productionYear, lifetime, cuttingHeight, isMulchingEnabled, isOn);
                        devices.add(lawnmower);
                        break;

                    case "AutoWatering":
                        AutoWatering autoWatering = new AutoWatering(id, manufacturer, model, powerSource, productionYear, lifetime, waterPressure, isSprinklerAttached, isWinterMode, isOn);
                        devices.add(autoWatering);
                        break;

                    case "ThermalDrive":
                        ThermalDrive thermalDrive = new ThermalDrive(id, manufacturer, model, powerSource, productionYear, lifetime, temperature, isProtectiveFunctionOn, isOn);
                        devices.add(thermalDrive);
                        break;

                    default:
                        System.err.println("Unknown device type!");
                        return null;
                }
            }
            connection.close();
            if (devices.isEmpty()) {
                return null;
            } else {
                return devices;
            }

        } catch (SQLException e) {
            System.err.println("SQLException on getting devices!");
            return null;
//            throw new RuntimeException(e);
        }
    }

    public static void addDevice(GardeningDevice device) {
        String query = "INSERT INTO AllDevices (id, Type, isOn, Manufacturer, Model, PowerSource, ProductionYear, Lifetime, " +
            "CuttingHeight, isMulchingEnabled, WaterPressure, isSprinklerAttached, isWinterMode, Temperature, isProtectiveFunctionOn) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement prepStatement = connection.prepareStatement(query);

            prepStatement.setInt(DatabaseAttributes.ID.ordinal(), device.getId());
            prepStatement.setString(DatabaseAttributes.TYPE.ordinal(), device.getType());
            prepStatement.setBoolean(DatabaseAttributes.IS_ON.ordinal(), device.getIsOn());
            prepStatement.setString(DatabaseAttributes.MANUFACTURER.ordinal(), device.getManufacturer());
            prepStatement.setString(DatabaseAttributes.MODEL.ordinal(), device.getModel());
            prepStatement.setString(DatabaseAttributes.POWER_SOURCE.ordinal(), device.getPowerSource());
            prepStatement.setInt(DatabaseAttributes.PRODUCTION_YEAR.ordinal(), device.getProductionYear());
            prepStatement.setInt(DatabaseAttributes.LIFETIME.ordinal(), device.getLifetime());

            if (device instanceof Lawnmower) {
                prepStatement.setInt(DatabaseAttributes.CUTTING_HEIGHT.ordinal(), ((Lawnmower) device).getCuttingHeight());
                prepStatement.setBoolean(DatabaseAttributes.IS_MULCHING_ENABLED.ordinal(), ((Lawnmower) device).isIsMulchingEnabled());
                prepStatement.setNull(DatabaseAttributes.WATER_PRESSURE.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_SPRINKLER_ATTACHED.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_WINTER_MODE.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.TEMPERATURE.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_PROTECTIVE_FUNCTION_ON.ordinal(), Types.NULL);
            } else if (device instanceof AutoWatering) {
                prepStatement.setInt(DatabaseAttributes.WATER_PRESSURE.ordinal(), ((AutoWatering) device).getWaterPressure());
                prepStatement.setBoolean(DatabaseAttributes.IS_SPRINKLER_ATTACHED.ordinal(), ((AutoWatering) device).isIsSprinklerAttached());
                prepStatement.setBoolean(DatabaseAttributes.IS_WINTER_MODE.ordinal(), ((AutoWatering) device).isIsWinterMode());
                prepStatement.setNull(DatabaseAttributes.CUTTING_HEIGHT.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_MULCHING_ENABLED.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.TEMPERATURE.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_PROTECTIVE_FUNCTION_ON.ordinal(), Types.NULL);
            } else if (device instanceof ThermalDrive) {
                prepStatement.setInt(DatabaseAttributes.TEMPERATURE.ordinal(), ((ThermalDrive) device).getTemperature());
                prepStatement.setBoolean(DatabaseAttributes.IS_PROTECTIVE_FUNCTION_ON.ordinal(), ((ThermalDrive) device).isIsProtectiveFunctionOn());
                prepStatement.setNull(DatabaseAttributes.CUTTING_HEIGHT.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_MULCHING_ENABLED.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.WATER_PRESSURE.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_SPRINKLER_ATTACHED.ordinal(), Types.NULL);
                prepStatement.setNull(DatabaseAttributes.IS_WINTER_MODE.ordinal(), Types.NULL);
            } else {
                System.err.println("Unknown device type!");
                return;
            }

            prepStatement.execute();

        } catch (SQLException ex) {
            System.err.println("SQLException on inserting device!");
            ex.printStackTrace();
        }
    }

    public static void deleteDevice(GardeningDevice device) {

    }

    public static GardeningDevice getDevice(int id) {
        String query = "SELECT * FROM AllDevices WHERE id = ?";

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
        String deleteQuery = "DELETE FROM AllDevices WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, login, password); PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            connection.close();
            // System.out.println("Запись успешно обновлена");
            // System.out.println("Запись не была обновлена");
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

enum DatabaseAttributes {
    NONE, ID, TYPE, IS_ON, MANUFACTURER, MODEL, POWER_SOURCE, PRODUCTION_YEAR, LIFETIME,
    CUTTING_HEIGHT, IS_MULCHING_ENABLED, WATER_PRESSURE, IS_SPRINKLER_ATTACHED, IS_WINTER_MODE, TEMPERATURE, IS_PROTECTIVE_FUNCTION_ON
}
