package com.example.javafx_project.devices;

import javafx.beans.property.IntegerProperty;

public abstract class GardeningDevice {

    private String manufacturer;
    private String model;
    private String powerSource;
    private int intensity;
    public boolean isOn;
    private int lifetime;
    private int productionYear;
    private String type;

    private int id;
    private static int nextId = 1;

    public GardeningDevice() {
        this.id = nextId++;
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
        this.id = nextId++;
        this.manufacturer = manufacturer;
        this.model = model;
        this.powerSource = powerSource;
        this.productionYear = productionYear;
        this.lifetime = lifetime;
        //        if (isValidYear(productionYear)) {
//            this.productionYear = productionYear;
//        } else {
//            System.out.println(ANSI_RED + "Введённое значение не соответствует допустимому диапазону. Установлен год производства по умолчанию (2000 год)." + ANSI_RESET);
//            this.productionYear = 2000;
//        }
//        if (isValidLifetime(lifetime)) {
//            this.lifetime = lifetime;
//        } else {
//            System.out.println(ANSI_RED + "Введённое значение не соответствует допустимому диапазону. Установлен срок службы по умолчанию (5 лет)." + ANSI_RESET);
//            this.lifetime = 5;
//        }
        this.isOn = false;
        this.intensity = 0;
    }

    public boolean checkStatus() {
        return isOn;
//        if (isOn) {
//            System.out.println(ANSI_GREEN + "Устройство готово к работе!\n" + ANSI_RESET);
//        } else {
//            System.out.println(ANSI_RED + "Устройство выключено!\n" + ANSI_RESET);
//        }
    }

    public abstract void turnOn();
    public abstract void turnOff();

    public abstract void performAction();


    public void isExpired() {
//        int age = CURRENT_YEAR - this.productionYear;
//        if (age >= this.lifetime) {
//            System.out.println(ANSI_RED + "Срок службы устройства истёк!\n" + ANSI_RESET);
//        } else {
//            System.out.println(ANSI_GREEN + "Срок службы устройства ещё не истёк!\n" + ANSI_RESET);
//        }
    }
    public boolean isValidYear(int productionYear) {
        return 2000 <= productionYear && productionYear < CURRENT_YEAR;
    }
    public boolean isValidLifetime(int expectedLifetime) {
        return 3 <= expectedLifetime && expectedLifetime <= 20;
    }


    // Геттеры и сеттеры для свойств
    public int getId() {
        return id;
    }
    public void setId() {
        this.id = nextId++;
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
    public int getIntensity() {
        return intensity;
    }
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void adjustIntensity(int intensity) {
        if (10 <= intensity && intensity <= 100) {
            setIntensity(intensity);
        } else {
            setIntensity(75);
        }
    }
    public String getType() { return this.getClass().toString().substring(41);}

    public static final int CURRENT_YEAR = 2023;
}