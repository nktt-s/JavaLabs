package org.example;
import java.util.Scanner;

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
    public void turnOn(Scanner scanner) throws InterruptedException {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Термопривод уже включён!" + ANSI_RESET);
        } else {

            System.out.print("Установите интенсивность работы устройства (10 - 100%): ");
            if (scanner.hasNextInt()) {
                int intensityInput = scanner.nextInt();
                adjustIntensity(intensityInput);
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлена интенсивность работы устройства по умолчанию (75%)." + ANSI_RESET);
                setIntensity(75);
                scanner.nextLine();
            }

            System.out.print("Включить систему авторегуляции температуры в необходимом диапазоне? (да/нет): ");
            scanner.nextLine();
            String autoregulationInput = scanner.nextLine().toLowerCase();
            setAutoregulation(autoregulationInput.equals("да"));
            manageAutoregulation(scanner);

            if (!isAutoregulationOn) {
                adjustTemperature(scanner);
            }

            System.out.print("Включить систему защиты от перегрева и переохлаждения? (да/нет): ");
            String protectiveFunctionInput = scanner.nextLine().toLowerCase();
            setProtectiveFunction(protectiveFunctionInput.equals("да"));
            manageProtectiveFunction();

            System.out.print("Выполнить калибровку температурных датчиков? (да/нет): ");
            String calibrateSensorsInput = scanner.nextLine().toLowerCase();
            setCalibrateTemperatureSensors(calibrateSensorsInput.equals("да"));
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
    public void performAction(Scanner scanner) {
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

    public void manageAutoregulation(Scanner scanner) {
        if (isAutoregulationOn) {
            System.out.print("Введите минимальное значение температуры (5 - 30°C): ");
            if (scanner.hasNextInt()) {
                lowTemp = scanner.nextInt();
                if (!(5 <= lowTemp && lowTemp <= 30)) {
                    System.out.println(ANSI_RED + "Введённое значение не соответствует диапазону. Установлено значение минимальной температуры по умолчанию (5°C)." + ANSI_RESET);
                    setLowTemp(5);
                }
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлено значение минимальной температуры по умолчанию (5°C)." + ANSI_RESET);
                setLowTemp(5);
            }
            System.out.print("Введите максимальное значение температуры (5 - 30°C): ");
            if (scanner.hasNextInt()) {
                highTemp = scanner.nextInt();
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
            scanner.nextLine();
        } else {
            System.out.println(ANSI_YELLOW + "Система авторегуляции отключена." + ANSI_RESET);
        }
    }

    public void adjustTemperature(Scanner scanner) {
        int temperature;
        System.out.print("Установите температуру (5 - 30°C): ");
        if (scanner.hasNextInt()) {
            temperature = scanner.nextInt();
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
        scanner.nextLine();
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