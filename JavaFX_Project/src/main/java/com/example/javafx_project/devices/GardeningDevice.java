package com.example.javafx_project.devices;

import com.example.javafx_project.DatabaseManager;
import javafx.beans.property.IntegerProperty;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;

public abstract class GardeningDevice implements Serializable {

    private String manufacturer;
    private String model;
    private String powerSource;
    public boolean isOn;
    private int lifetime;
    private int productionYear;
    protected String type;

    private int id;
    private static int nextId = 1;

    public GardeningDevice() {
        this.id = nextId++;
//        this.setUniqueId();
    }

    public void setUniqueId() {
        ArrayList<GardeningDevice> devicesFromDB = DatabaseManager.getAllDevices();
        if (devicesFromDB != null) {
//            this.id = devicesFromDB.size() + 1;
            this.id = devicesFromDB.getLast().getId() + 1;
        } else {
            this.id = 1;
        }
    }

    public GardeningDevice(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.powerSource = powerSource;
        this.productionYear = productionYear;
        this.lifetime = lifetime;
    }

    public GardeningDevice(String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
//        this.id = nextId++;
        this.setUniqueId();
        this.manufacturer = manufacturer;
        this.model = model;
        this.powerSource = powerSource;
        this.productionYear = productionYear;
        this.lifetime = lifetime;
        this.isOn = false;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public abstract void turnOn();
    public abstract void turnOff();

    public abstract void performAction();
    public boolean isValidYear(int productionYear) {
        return MIN_YEAR <= productionYear && productionYear <= CURRENT_YEAR;
    }
    public boolean isValidLifetime(int expectedLifetime) {
        return 3 <= expectedLifetime && expectedLifetime <= 20;
    }


    // Геттеры и сеттеры для свойств
    public int getId() {
        return id;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getPowerSource() {
        return powerSource;
    }
    public void setPowerSource(String powerSource) {
        this.powerSource = powerSource;
    }
    public int getProductionYear() {
        return productionYear;
    }
    public void setProductionYear(int productionYear) {
        if (isValidYear(productionYear)) {
            this.productionYear = productionYear;
        } else {
            this.productionYear = 2000;
        }
    }
    public int getLifetime() {
        return lifetime;
    }
    public void setLifetime(int lifetime) {
        if (isValidLifetime(lifetime)) {
            this.lifetime = lifetime;
        } else {
            this.lifetime = 5;
        }
    }
    public String getType() {
        return this.type;
//        return this.getClass().toString().substring(41);
    }
    public int getCurrentYear() {
        return CURRENT_YEAR;
    }

    public static final int MIN_YEAR = 1950;
    public static final int CURRENT_YEAR = Year.now().getValue();
}