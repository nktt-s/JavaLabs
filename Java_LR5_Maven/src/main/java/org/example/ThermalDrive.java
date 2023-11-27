package org.example;

public class ThermalDrive extends GardeningDevice {
    private int temperature;
    private boolean isAutoregulationOn;
    private int lowTemp;
    private int highTemp;
    private boolean isProtectiveFunctionOn;
    private boolean calibrateTemperatureSensors;
    private boolean isCalibrated;

    public ThermalDrive(String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.temperature = 0;
    }

    @Override
    public void turnOn() throws InterruptedException {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Термопривод уже включён!" + ANSI_RESET);
        } else {

            System.out.print("Установите интенсивность работы устройства (10 - 100%): ");
            if (Main.sc.hasNextInt()) {
                int intensityInput = Main.sc.nextInt();
                adjustIntensity(intensityInput);
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлена интенсивность работы устройства по умолчанию (75%)." + ANSI_RESET);
                setIntensity(75);
                Main.sc.nextLine();
            }

            System.out.print("Включить систему авторегуляции температуры в необходимом диапазоне? (y/n): ");
            Main.sc.nextLine();
            String autoregulationInput = Main.sc.nextLine().toLowerCase();
            setAutoregulation(autoregulationInput.equals("y"));
            manageAutoregulation();

            if (!isAutoregulationOn) {
                adjustTemperature();
            }

            System.out.print("Включить систему защиты от перегрева и переохлаждения? (y/n): ");
            String protectiveFunctionInput = Main.sc.nextLine().toLowerCase();
            setProtectiveFunction(protectiveFunctionInput.equals("y"));
            manageProtectiveFunction();

            System.out.print("Выполнить калибровку температурных датчиков? (y/n): ");
            String calibrateSensorsInput = Main.sc.nextLine().toLowerCase();
            setCalibrateTemperatureSensors(calibrateSensorsInput.equals("y"));
            manageCalibrationTemperatureSensors();

            System.out.println(ANSI_GREEN + "Термопривод готов к работе.\n" + ANSI_RESET);
            super.isOn = true;
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            System.out.println(ANSI_YELLOW + "Термопривод уже выключен!\n" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Термопривод выключен.\n" + ANSI_RESET);
            super.isOn = false;
        }
    }
    @Override
    public void performAction() {
        if (super.isOn) {
            if (!isAutoregulationOn) {
                System.out.println(ANSI_GREEN + "Термопривод работает! Установленная температура: " + getTemperature() + "°C.\n" + ANSI_RESET);
            } else {
                System.out.println(ANSI_GREEN + "Термопривод работает! Температура поддерживается в диапазоне от " + getLowTemp() + "°C до " + getHighTemp() + "°C.\n" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_YELLOW + "Термопривод ещё не включён!\n" + ANSI_RESET);
        }
    }

    public void manageAutoregulation() {
        if (isAutoregulationOn) {
            System.out.print("Введите минимальное значение температуры (5 - 30°C): ");
            if (Main.sc.hasNextInt()) {
                lowTemp = Main.sc.nextInt();
                if (!(5 <= lowTemp && lowTemp <= 30)) {
                    System.out.println(ANSI_RED + "Введённое значение не соответствует диапазону. Установлено значение минимальной температуры по умолчанию (5°C)." + ANSI_RESET);
                    setLowTemp(5);
                }
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлено значение минимальной температуры по умолчанию (5°C)." + ANSI_RESET);
                setLowTemp(5);
            }
            System.out.print("Введите максимальное значение температуры (5 - 30°C): ");
            if (Main.sc.hasNextInt()) {
                highTemp = Main.sc.nextInt();
                if (!(5 <= highTemp && highTemp <= 30)) {
                    System.out.println(ANSI_RED + "Введённое значение не соответствует диапазону. Установлено значение максимальной температуры по умолчанию (30°C)." + ANSI_RESET);
                    setHighTemp(30);
                }
                if (getLowTemp() >= getHighTemp()) {
                    System.out.println(ANSI_RED + "Введённое значение максимальной температуры не может быть меньше установленной минимальной температуры.");
                    System.out.println("Установлено значение максимальной температуры по умолчанию (30°C)." + ANSI_RESET);
                    setHighTemp(30);
                }
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлено значение максимальной температуры по умолчанию (30°C)." + ANSI_RESET);
                setHighTemp(30);
            }
            System.out.println(ANSI_GREEN + "Система авторегуляции включена и настроена. Температура будет поддерживаться в диапазоне от " + getLowTemp() + "°C до " + getHighTemp() + "°C." + ANSI_RESET);
            Main.sc.nextLine();
        } else {
            System.out.println(ANSI_YELLOW + "Система авторегуляции отключена." + ANSI_RESET);
        }
    }

    public void adjustTemperature() {
        int temperature;
        System.out.print("Установите температуру (5 - 30°C): ");
        if (Main.sc.hasNextInt()) {
            temperature = Main.sc.nextInt();
            if (5 <= temperature && temperature <= 30) {
                setTemperature(temperature);
                System.out.println(ANSI_GREEN + "Значение температуры успешно установлено на " + getTemperature() + "°C." + ANSI_RESET);
            } else {
                setTemperature(20);
                System.out.println(ANSI_RED + "Значение температуры не соответствует допустимому диапазону. Установлено значение по умолчанию (20°C).");
            }
        } else {
            setTemperature(20);
            System.out.println(ANSI_RED + "Неверный ввод. Установлено значение температуры по умолчанию (20°C)." + ANSI_RESET);
        }
        Main.sc.nextLine();
    }

    public void manageProtectiveFunction() {
        if (isProtectiveFunctionOn) {
            System.out.println(ANSI_GREEN + "Система защиты от перегрева и переохлаждения включена." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Система защиты от перегрева и переохлаждения отключена." + ANSI_RESET);
        }
    }

    public void manageCalibrationTemperatureSensors() throws InterruptedException {
        if (calibrateTemperatureSensors) {
            System.out.print("Выполняется калибровка датчиков температуры");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.println();
            System.out.println(ANSI_GREEN + "Калибровка успешно завершена!" + ANSI_RESET);
            isCalibrated = true;
        } else {
            System.out.println(ANSI_YELLOW + "Калибровка датчиков температуры пропущена." + ANSI_RESET);
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