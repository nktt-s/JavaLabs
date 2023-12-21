package com.example.javafx_project.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lawnmower extends GardeningDevice {
    private static final Logger logger = LogManager.getLogger(Lawnmower.class);
    private boolean isMulchingEnabled; // Система мульчирования - поверхностное
    // покрытие почвы скошенной измельчённой травой для её защиты и улучшения свойств
    private int cuttingHeight; // Высота среза

    public Lawnmower() {
        super();
    }

    public Lawnmower(String manufacturer, String model, String powerSource,
                     int productionYear, int lifetime, int cuttingHeight, boolean isMulchingEnabled,
                     boolean isOn) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.cuttingHeight = cuttingHeight;
        this.isMulchingEnabled = isMulchingEnabled;
        this.isOn = isOn;
        logger.info("Создана новая газонокосилка");
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            logger.error("Попытка включить уже включённую газонокосилку");
        } else {
            setIntensity(75);
            isMulchingEnabled = true;
            setCuttingHeight(40);
            super.isOn = true;
            logger.info("Включена газонокосилка");
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            logger.error("Попытка выключить уже выключенную газонокосилку");
        } else {
            super.isOn = false;
            logger.info("Выключена газонокосилка");
        }
    }

    @Override
    public void performAction() {
        if (super.isOn) {
            cutTheGrass();
        } else {
            logger.error("Попытка выполнить действие выключенной газонокосилки");
        }
    }

    public void cutTheGrass() {
        logger.info("Выполняется кошение травы");
    }

    public void manageMulching() {
        if (isMulchingEnabled) {
            logger.info("Включена система мульчирования газонокосилки");
        } else {
            logger.info("Отключена система мульчирования газонокосилки");
        }
    }

    public boolean isValidCuttingHeight(int height) {
        return 20 <= height && height <= 100;
    }

    public void adjustCuttingHeight(int height) {
        if (20 <= height && height <= 100) {
            setCuttingHeight(height);
            logger.info("Высота среза травы для газонокосилки установлена на " +
                + getCuttingHeight() + "мм");
        } else {
            setCuttingHeight(40);
            logger.error("Неверный ввод. Установлена высота среза травы для газонокосилки " +
                "по умолчанию (40 мм).");
        }
    }

    public int getCuttingHeight() {
        return cuttingHeight;
    }
    public void setCuttingHeight(int cuttingHeight) {
        this.cuttingHeight = cuttingHeight;
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