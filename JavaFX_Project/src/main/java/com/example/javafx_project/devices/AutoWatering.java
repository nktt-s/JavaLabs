package com.example.javafx_project.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoWatering extends GardeningDevice {
    private static final Logger logger = LogManager.getLogger(AutoWatering.class);
    private boolean isSprinklerAttached;
    private int waterPressure;
    private boolean isWinterMode;

    public AutoWatering(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
//        this.waterPressure = 40;
//        this.isSprinklerAttached = false;
//        this.isWinterMode = false;
        this.isOn = isOn;
    }

    public AutoWatering(String manufacturer, String model, String powerSource, int productionYear, int lifetime, int waterPressure, boolean isSprinklerAttached, boolean isWinterMode, boolean isOn) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.waterPressure = waterPressure;
        this.isSprinklerAttached = isSprinklerAttached;
        this.isWinterMode = isWinterMode;
        this.isOn = isOn;
        logger.info("Создан новый автополив");
    }

    public AutoWatering(int id, String manufacturer, String model, String powerSource, int productionYear, int lifetime, int waterPressure, boolean isSprinklerAttached, boolean isWinterMode, boolean isOn) {
        super(id, manufacturer, model, powerSource, productionYear, lifetime);
        this.waterPressure = waterPressure;
        this.isSprinklerAttached = isSprinklerAttached;
        this.isWinterMode = isWinterMode;
        this.isOn = isOn;
        logger.info("Создан новый автополив");
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            logger.error("Попытка включить уже включённый автополив");
        } else {
            setIntensity(75);
            setSprinklerAttached(true);
            setWaterPressure(40);
            setWinterMode(false);
            super.isOn = true;
            logger.info("Включён автополив");
        }
    }

    @Override
    public void turnOff() {
        if (!super.isOn) {
            logger.error("Попытка выключить уже выключенный автополив");
        }
        super.isOn = false;
        logger.info("Выключен автополив");
    }

    @Override
    public void performAction() {
        if (super.isOn) {
            logger.info("Выполняется полив растений");
            turnOff();
            logger.info("Выключен автополив");
        } else {
            logger.error("Попытка выполнить действие выключенного автополива");
        }
    }

    public void manageSprinkler() {
        if (isSprinklerAttached) {
            logger.info("Установлен дождеватель");
        } else {
            logger.info("Не установлен дождеватель");
        }
    }

    public boolean isValidWaterPressure(int pressure) {
        return 20 <= pressure && pressure <= 80;
    }

    public void adjustWaterPressure(int pressure) {
        if (20 <= pressure && pressure <= 80) {
            setWaterPressure(pressure);
            logger.info("Установлено давление воды для автополива");
        } else {
            setWaterPressure(40);
            logger.error("Неверный ввод. Установлено значение по умолчанию (40 psi)");
        }
    }

    public void manageWinterMode() {
        if (isWinterMode) {
            logger.info("Включён зимний режим для автополива");
        } else {
            logger.info("Выключен зимний режим для автополива");
        }
    }

    public boolean isIsSprinklerAttached() {
        return isSprinklerAttached;
    }

    public boolean isIsWinterMode() {
        return isWinterMode;
    }

    public void setSprinklerAttached(boolean sprinklerAttached) {
        isSprinklerAttached = sprinklerAttached;
    }

    public void setWaterPressure(int pressure) {
        this.waterPressure = pressure;
    }

    public int getWaterPressure() {
        return waterPressure;
    }

    public void setWinterMode(boolean winterMode) {
        isWinterMode = winterMode;
    }

    public void switchOn() {
        isOn = true;
    }

    public void switchOff() {
        isOn = false;
    }
}