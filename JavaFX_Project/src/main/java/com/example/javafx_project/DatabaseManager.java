package com.example.javafx_project;

import com.example.javafx_project.devices.*;
import com.example.javafx_project.devices.GardeningDevice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static ArrayList<GardeningDevice> getAllDevices() {
        loggerDB.info("Вызван метод получения всех устройств");
        ArrayList<GardeningDevice> devices = new ArrayList<>();
        String query = "SELECT * FROM AllDevices";

        try {
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
        loggerDB.info("Вызван метод добавления устройства");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
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

                switch (device) {
                    case Lawnmower lawnmower -> {
                        prepStatement.setInt(DatabaseAttributes.CUTTING_HEIGHT.ordinal(), lawnmower.getCuttingHeight());
                        prepStatement.setBoolean(DatabaseAttributes.IS_MULCHING_ENABLED.ordinal(), lawnmower.isIsMulchingEnabled());
                        prepStatement.setNull(DatabaseAttributes.WATER_PRESSURE.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_SPRINKLER_ATTACHED.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_WINTER_MODE.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.TEMPERATURE.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_PROTECTIVE_FUNCTION_ON.ordinal(), Types.NULL);
                    }
                    case AutoWatering autoWatering -> {
                        prepStatement.setInt(DatabaseAttributes.WATER_PRESSURE.ordinal(), autoWatering.getWaterPressure());
                        prepStatement.setBoolean(DatabaseAttributes.IS_SPRINKLER_ATTACHED.ordinal(), autoWatering.isIsSprinklerAttached());
                        prepStatement.setBoolean(DatabaseAttributes.IS_WINTER_MODE.ordinal(), autoWatering.isIsWinterMode());
                        prepStatement.setNull(DatabaseAttributes.CUTTING_HEIGHT.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_MULCHING_ENABLED.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.TEMPERATURE.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_PROTECTIVE_FUNCTION_ON.ordinal(), Types.NULL);
                    }
                    case ThermalDrive thermalDrive -> {
                        prepStatement.setInt(DatabaseAttributes.TEMPERATURE.ordinal(), thermalDrive.getTemperature());
                        prepStatement.setBoolean(DatabaseAttributes.IS_PROTECTIVE_FUNCTION_ON.ordinal(), thermalDrive.isIsProtectiveFunctionOn());
                        prepStatement.setNull(DatabaseAttributes.CUTTING_HEIGHT.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_MULCHING_ENABLED.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.WATER_PRESSURE.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_SPRINKLER_ATTACHED.ordinal(), Types.NULL);
                        prepStatement.setNull(DatabaseAttributes.IS_WINTER_MODE.ordinal(), Types.NULL);
                    }
                    default -> {
                        loggerDB.error("Неизвестный тип устройства при добавлении");
                        return;
                    }
                }

                prepStatement.execute();
                connection.close();

            } catch (SQLException ex) {
                loggerDB.error("Ошибка при установлении соединения с БД при добавлении");
                System.err.println("SQLException on inserting device!");
//            ex.printStackTrace();
            }

        });
        executorService.shutdown();
    }

    public static void deleteDevice(int id) {
        loggerDB.info("Вызван метод удаления устройства");
        String query = "DELETE FROM AllDevices WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            connection.close();

        } catch (SQLException e) {
            System.err.println("SQLException on deleting device!");
//            e.printStackTrace();
        }
    }

    public static GardeningDevice getDevice(int id) {
        loggerDB.info("Вызван метод добавления одного устройства");
        String query = "SELECT * FROM AllDevices WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(DatabaseAttributes.ID.ordinal(), id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idDevice = resultSet.getInt("id");
                String type = resultSet.getString("Type");
                String manufacturer = resultSet.getString("Manufacturer");
                String model = resultSet.getString("Model");
                String powerSource = resultSet.getString("PowerSource");
                int productionYear = resultSet.getInt("ProductionYear");
                int lifetime = resultSet.getInt("Lifetime");
                boolean isOn = resultSet.getBoolean("isOn");

                switch (type) {
                    case "Lawnmower":
                        int cuttingHeight = resultSet.getInt("CuttingHeight");
                        boolean isMulchingEnabled = resultSet.getBoolean("isMulchingEnabled");
                        return new Lawnmower(idDevice, manufacturer, model, powerSource, productionYear, lifetime, cuttingHeight, isMulchingEnabled, isOn);

                    case "AutoWatering":
                        int waterPressure = resultSet.getInt("WaterPressure");
                        boolean isSprinklerAttached = resultSet.getBoolean("isSprinklerAttached");
                        boolean isWinterMode = resultSet.getBoolean("isWinterMode");
                        return new AutoWatering(idDevice, manufacturer, model, powerSource, productionYear, lifetime, waterPressure, isSprinklerAttached, isWinterMode, isOn);

                    case "ThermalDrive":
                        int temperature = resultSet.getInt("Temperature");
                        boolean isProtectiveFunctionOn = resultSet.getBoolean("isProtectiveFunctionOn");
                        return new ThermalDrive(idDevice, manufacturer, model, powerSource, productionYear, lifetime, temperature, isProtectiveFunctionOn, isOn);
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("SQLException on getting device!");
//            e.printStackTrace();
        }
        return null;
    }

    public static void updateDevice(GardeningDevice device) {
        loggerDB.info("Вызван метод изменения устройства");
        String query = "UPDATE AllDevices SET isOn = ?, Manufacturer = ?, Model = ?, PowerSource = ?, ProductionYear = ?, Lifetime = ?, " +
            "CuttingHeight = ?, isMulchingEnabled = ?, WaterPressure = ?, isSprinklerAttached = ?, isWinterMode = ?, Temperature = ?, isProtectiveFunctionOn = ? WHERE id = ?;";

        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement prepStatement = connection.prepareStatement(query);

            prepStatement.setBoolean(1, device.getIsOn());
            prepStatement.setString(2, device.getManufacturer()); // "NEW_MANUFACTURER"
            prepStatement.setString(3, device.getModel());
            prepStatement.setString(4, device.getPowerSource());
            prepStatement.setInt(5, device.getProductionYear());
            prepStatement.setInt(6, device.getLifetime());
            prepStatement.setInt(14, device.getId());

            switch (device) {
                case Lawnmower lawnmower -> {
                    prepStatement.setInt(7, lawnmower.getCuttingHeight());
                    prepStatement.setBoolean(8, lawnmower.isIsMulchingEnabled());
                    prepStatement.setNull(9, Types.NULL);
                    prepStatement.setNull(10, Types.NULL);
                    prepStatement.setNull(11, Types.NULL);
                    prepStatement.setNull(12, Types.NULL);
                    prepStatement.setNull(13, Types.NULL);
                }
                case AutoWatering autoWatering -> {
                    prepStatement.setInt(9, autoWatering.getWaterPressure());
                    prepStatement.setBoolean(10, autoWatering.isIsSprinklerAttached());
                    prepStatement.setBoolean(11, autoWatering.isIsWinterMode());
                    prepStatement.setNull(7, Types.NULL);
                    prepStatement.setNull(8, Types.NULL);
                    prepStatement.setNull(12, Types.NULL);
                    prepStatement.setNull(13, Types.NULL);
                }
                case ThermalDrive thermalDrive -> {
                    prepStatement.setInt(12, thermalDrive.getTemperature());
                    prepStatement.setBoolean(13, thermalDrive.isIsProtectiveFunctionOn());
                    prepStatement.setNull(7, Types.NULL);
                    prepStatement.setNull(8, Types.NULL);
                    prepStatement.setNull(9, Types.NULL);
                    prepStatement.setNull(10, Types.NULL);
                    prepStatement.setNull(11, Types.NULL);
                }
                default -> {
                    System.err.println("Unknown device type on updating device!");
                    return;
                }
            }

            prepStatement.execute();
            connection.close();

        } catch (SQLException ex) {
            System.err.println("SQLException on updating device!");
        }
    }
}

enum DatabaseAttributes {
    NONE, ID, TYPE, IS_ON, MANUFACTURER, MODEL, POWER_SOURCE, PRODUCTION_YEAR, LIFETIME,
    CUTTING_HEIGHT, IS_MULCHING_ENABLED, WATER_PRESSURE, IS_SPRINKLER_ATTACHED, IS_WINTER_MODE, TEMPERATURE, IS_PROTECTIVE_FUNCTION_ON
}
