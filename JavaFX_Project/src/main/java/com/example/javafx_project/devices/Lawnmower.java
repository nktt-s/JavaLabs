package com.example.javafx_project.devices;

public class Lawnmower extends GardeningDevice {
    private boolean isMulchingEnabled; // Система мульчирования - поверхностное
    // покрытие почвы скошенной измельчённой травой для её защиты и улучшения свойств
    private int cuttingHeight; // Высота среза

    public Lawnmower(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.isOn = isOn;
        this.setType();
    }

    public Lawnmower(String manufacturer, String model, String powerSource, int productionYear, int lifetime, int cuttingHeight, boolean isMulchingEnabled, boolean isOn) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.cuttingHeight = cuttingHeight;
        this.isMulchingEnabled = isMulchingEnabled;
        this.isOn = isOn;
        this.setType();
    }
    public Lawnmower(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, int cuttingHeight, boolean isMulchingEnabled, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.cuttingHeight = cuttingHeight;
        this.isMulchingEnabled = isMulchingEnabled;
        this.isOn = isOn;
        this.setType();
    }

    public boolean isValidCuttingHeight(int height) {
        return 20 <= height && height <= 100;
    }

    public int getCuttingHeight() {
        return cuttingHeight;
    }

    public boolean isIsMulchingEnabled() {
        return isMulchingEnabled;
    }

    public boolean isIsOn() {
        return isOn;
    }

    private void setType() {
        this.type = "Lawnmower";
    }
}