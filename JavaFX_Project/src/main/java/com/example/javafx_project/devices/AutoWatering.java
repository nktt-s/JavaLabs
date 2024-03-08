package com.example.javafx_project.devices;

public class AutoWatering extends GardeningDevice {
    private boolean isSprinklerAttached;
    private int waterPressure;
    private boolean isWinterMode;


    public AutoWatering(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.isOn = isOn;
        this.setType();
    }

    public AutoWatering(String manufacturer, String model, String powerSource, int productionYear, int lifetime, int waterPressure, boolean isSprinklerAttached, boolean isWinterMode, boolean isOn) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.waterPressure = waterPressure;
        this.isSprinklerAttached = isSprinklerAttached;
        this.isWinterMode = isWinterMode;
        this.isOn = isOn;
        this.setType();
    }

    public AutoWatering(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, int waterPressure, boolean isSprinklerAttached, boolean isWinterMode, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.waterPressure = waterPressure;
        this.isSprinklerAttached = isSprinklerAttached;
        this.isWinterMode = isWinterMode;
        this.isOn = isOn;
        this.setType();
    }

    public boolean isValidWaterPressure(int pressure) {
        return 20 <= pressure && pressure <= 80;
    }

    public boolean isIsSprinklerAttached() {
        return isSprinklerAttached;
    }

    public boolean isIsWinterMode() {
        return isWinterMode;
    }

    public boolean isIsOn() {
        return isOn;
    }

    public int getWaterPressure() {
        return waterPressure;
    }

    private void setType() {
        this.type = "AutoWatering";
    }
}