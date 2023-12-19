package com.example.javafx_project.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThermalDrive extends GardeningDevice {
    private static final Logger logger = LogManager.getLogger(AutoWatering.class);
    private int temperature;
    private boolean isAutoregulationOn;
    private int lowTemp;
    private int highTemp;
    private boolean isProtectiveFunctionOn;
    private boolean calibrateTemperatureSensors;
    private boolean isCalibrated;

    public ThermalDrive() {
        super();
    };

    public ThermalDrive(String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.temperature = 0;
        logger.info("Создан новый термопривод");
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            logger.error("Попытка включить уже включённый термопривод");
        } else {
            setIntensity(75);
            setAutoregulation(false);
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
            if (!isAutoregulationOn) {
                logger.info("Термопривод работает. Установленная температура: " + getTemperature() + "°C.");
            } else {
                logger.info("Термопривод работает! Температура поддерживается в диапазоне от " + getLowTemp() + "°C до " + getHighTemp() + "°C.");
            }
        } else {
            logger.error("Попытка выполнить действие выключенного термопривода");
        }
    }

    public void manageAutoregulation(int lowTemp, int highTemp) {
        if (isAutoregulationOn) {
            if (!(5 <= lowTemp && lowTemp <= 30)) {
                logger.error("Неверный ввод. Значение минимальной температуры не соответствует диапазону");
                logger.info("Установлено значение минимальной температуры по умолчанию (5°C)");
                setLowTemp(5);
            }
            if (!(5 <= highTemp && highTemp <= 30)) {
                logger.error("Неверный ввод. Значение максимальной температуры не соответствует диапазону");
                logger.info("Установлено значение максимальной температуры по умолчанию (30°C)");
                setHighTemp(30);
            }
            if (getLowTemp() >= getHighTemp()) {
                logger.error("Неверный ввод. Значение минимальной температуры больше значения максимальной");
                logger.info("Установлено значение максимальной температуры по умолчанию (30°C)");
                setHighTemp(30);
            }
            logger.info("Система авторегуляции термопривода включена и настроена");
        } else {
            logger.info("Система авторегуляции термопривода отключена");
        }
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
    public void setAutoregulation(boolean isAutoregulationOn) {
        this.isAutoregulationOn = isAutoregulationOn;
    }
    public void setProtectiveFunction(boolean isProtectiveFunctionOn) {
        this.isProtectiveFunctionOn = isProtectiveFunctionOn;
    }
    public boolean getProtectiveFunction() {
        return isProtectiveFunctionOn;
    }
    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }
    public int getLowTemp() {
        return lowTemp;
    }
    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }
    public int getHighTemp() {
        return highTemp;
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
    public boolean getStatus() {
        return isOn;
    }
}