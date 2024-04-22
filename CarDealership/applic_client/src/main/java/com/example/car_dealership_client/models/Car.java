package com.example.car_dealership_client.models;

import java.io.Serializable;
import java.time.Year;

public class Car implements Serializable {
    private String manufacturer;
    private String model;
    private String color;
    private String seller;
    private String buyer = null;
    private int productionYear;

    private int id;
    private static int nextId = 1;

    public Car() {
        this.id = nextId++;
    }

    public Car(int id, String seller, String buyer, String manufacturer, String model, String color, int productionYear) {
        this.id = id;
        this.seller = seller;
        this.buyer = buyer;
        this.manufacturer = manufacturer;
        this.model = model;
        this.color = color;
        this.productionYear = productionYear;
    }

    public int getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getSeller() {
        return seller;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public String getColor() {
        return color;
    }

    public String getBuyer() {
        return buyer;
    }

    public boolean isValidYear(int productionYear) {
        return MIN_YEAR <= productionYear && productionYear <= CURRENT_YEAR;
    }

    public int getCurrentYear() {
        return CURRENT_YEAR;
    }

//    public void setUniqueId() {
//        ArrayList<Car> devicesFromDB = DatabaseManager.getAllDevices();
//        if (devicesFromDB != null) {
//            this.id = devicesFromDB.getLast().getId() + 1;
//        } else {
//            this.id = 1;
//        }

    //    }
    public static final int MIN_YEAR = 1950;

    public static final int CURRENT_YEAR = Year.now().getValue();
}
