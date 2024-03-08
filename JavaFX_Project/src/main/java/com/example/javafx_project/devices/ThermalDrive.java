package com.example.javafx_project.devices;

public class ThermalDrive extends GardeningDevice {
    private int temperature;
    private boolean isProtectiveFunctionOn;

    public ThermalDrive(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.isOn = isOn;
        this.setType();
    }

    public ThermalDrive(String manufacturer, String model, String powerSource, int productionYear, int lifetime, int temperature, boolean isProtectiveFunctionOn, boolean isOn) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.temperature = temperature;
        this.isProtectiveFunctionOn = isProtectiveFunctionOn;
        this.isOn = isOn;
        this.setType();
    }

    public ThermalDrive(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, int temperature, boolean isProtectiveFunctionOn, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.temperature = temperature;
        this.isProtectiveFunctionOn = isProtectiveFunctionOn;
        this.isOn = isOn;
        this.setType();
    }

    public boolean isValidTemperature(int temperature) {
        return 5 <= temperature && temperature <= 30;
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean isIsProtectiveFunctionOn() {
        return isProtectiveFunctionOn;
    }

    public boolean isIsOn() {
        return isOn;
    }

    private void setType() {
        this.type = "ThermalDrive";
    }
}