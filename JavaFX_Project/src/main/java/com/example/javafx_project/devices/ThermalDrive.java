package com.example.javafx_project.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThermalDrive extends GardeningDevice {
    private static final Logger logger = LogManager.getLogger(AutoWatering.class);
    private int temperature;
    private boolean isProtectiveFunctionOn;
    private boolean calibrateTemperatureSensors;
    private boolean isCalibrated;

    public ThermalDrive(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
//        this.temperature = 20;
//        this.isProtectiveFunctionOn = true;
        this.isOn = isOn;
    }

    public ThermalDrive(String manufacturer, String model, String powerSource, int productionYear, int lifetime, int temperature, boolean isProtectiveFunctionOn, boolean isOn) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.temperature = temperature;
        this.isProtectiveFunctionOn = isProtectiveFunctionOn;
        this.isOn = isOn;
        logger.info("Создан новый термопривод");
    }

    public ThermalDrive(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, int temperature, boolean isProtectiveFunctionOn, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.temperature = temperature;
        this.isProtectiveFunctionOn = isProtectiveFunctionOn;
        this.isOn = isOn;
        logger.info("Создан новый термопривод");
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            logger.error("Попытка включить уже включённый термопривод");
        } else {
            setTemperature(20);
            setProtectiveFunction(true);
            setCalibrateTemperatureSensors(true);
            super.isOn = true;
            logger.info("Включён термопривод");
        }
    }

    @Override
    public void turnOff() {
        if (!super.isOn) {
            logger.error("Попытка выключить уже выключенный термопривод");
        } else {
            super.isOn = false;
            logger.info("Выключен термопривод");
        }
    }

    @Override
    public void performAction() {
        if (super.isOn) {
            logger.info("Термопривод работает. Установленная температура: " + getTemperature() + "°C.");
        } else {
            logger.error("Попытка выполнить действие выключенного термопривода");
        }
    }

    public boolean isValidTemperature(int temperature) {
        return 5 <= temperature && temperature <= 30;
    }

    public void adjustTemperature(int temperature) {
        if (5 <= temperature && temperature <= 30) {
            setTemperature(temperature);
            logger.info("Установлено значение температуры " + getTemperature() + "°C для термопривода");
        } else {
            setTemperature(20);
            logger.error("Неверный ввод. Установлено значение по умолчанию (20°C) для термопривода.");
        }
    }

    public void manageProtectiveFunction() {
        if (isProtectiveFunctionOn) {
            logger.info("Система защиты от перегрева и переохлаждения для термопривода включена.");
        } else {
            logger.info("Система защиты от перегрева и переохлаждения для термопривода отключена.");
        }
    }

    public void manageCalibrationTemperatureSensors() {
        if (calibrateTemperatureSensors) {
            logger.info("Выполнена калибровка температурных датчиков для термопривода");
            isCalibrated = true;
        } else {
            logger.info("Пропущена калибровка температурных датчиков для термопривода");
            isCalibrated = false;
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setProtectiveFunction(boolean isProtectiveFunctionOn) {
        this.isProtectiveFunctionOn = isProtectiveFunctionOn;
    }

    public boolean isIsProtectiveFunctionOn() {
        return isProtectiveFunctionOn;
    }

    public void setCalibrateTemperatureSensors(boolean calibrateTemperatureSensors) {
        this.calibrateTemperatureSensors = calibrateTemperatureSensors;
    }

    public boolean isCalibrated() {
        return isCalibrated;
    }

    public void switchOn() {
        isOn = true;
    }

    public void switchOff() {
        isOn = false;
    }
}