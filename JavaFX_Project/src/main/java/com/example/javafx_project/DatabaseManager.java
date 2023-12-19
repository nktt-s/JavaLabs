package com.example.javafx_project;

import com.example.javafx_project.devices.AutoWatering;
import com.example.javafx_project.devices.GardeningDevice;
import com.example.javafx_project.devices.Lawnmower;
import com.example.javafx_project.devices.ThermalDrive;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager implements Serializable {
    static String url = "jdbc:mysql://localhost:3306/";
    static String name;
    static String user;
    static String password;

    public static void setValuesForConnection(String dbName, String dbUser, String dbPassword) {
        url += dbName;
        name = dbName;
        user = dbUser;
        password = dbPassword;
    }

    public static ArrayList<GardeningDevice> getAllDevices() {
        ArrayList<GardeningDevice> devices = new ArrayList<>();
        String query = "SELECT * FROM devices";

        try (Connection connection = DriverManager.getConnection(url, name, password);
            PreparedStatement prepStatement = connection.prepareStatement(query);
            ResultSet resultSet = prepStatement.executeQuery()) {

            while (resultSet.next()) {
                GardeningDevice device = getResultSet(resultSet);
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    private static GardeningDevice getResultSet(ResultSet resultSet) throws SQLException {
        GardeningDevice device;
        String type = resultSet.getString("Type");

        if (type.equals("Lawnmower")) {
            device = new Lawnmower();
            if (resultSet.getBoolean("isOn")) {
                device.turnOn();
            }
        } else if (type.equals("AutoWatering")) {
            device = new AutoWatering();
            if (resultSet.getBoolean("isOn")) {
                device.turnOn();
            }
//            ((RadiatorRemote) thermostat).setWattage(rs.getInt("wattage"));
        } else {
            device = new ThermalDrive();
            if (resultSet.getBoolean("isOn")) {
                device.turnOn();
            }
        }

        device.setId();
        device.setManufacturer(resultSet.getString("Manufacturer"));
        device.setModel(resultSet.getString("Model"));
        device.setPowerSource(resultSet.getString("PowerSource"));
        device.setProductionYear(resultSet.getInt("ProductionYear"));
        device.setLifetime(resultSet.getInt("Lifetime"));
        device.setIntensity(resultSet.getInt("Intensity"));

        return device;
    }
}
