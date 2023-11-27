package org.example;

public class AutoWatering extends GardeningDevice {
    private int workingMinutes;
    private boolean isSprinklerAttached;
    private int waterPressure;
    private boolean isWinterMode;

    public AutoWatering(String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        this.workingMinutes = 0;
        isSprinklerAttached = false;
        this.waterPressure = 0;
        this.isWinterMode = false;
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Автополив уже включён!\n" + ANSI_RESET);
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

            Main.sc.nextLine();
            System.out.print("Введите продолжительность полива в минутах (5 - 90): ");
            if (Main.sc.hasNextInt()) {
                int minutes = Main.sc.nextInt();
                adjustWorkingMinutes(minutes);
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлено значение по умолчанию (10 мин)." + ANSI_RESET);
                setWorkingMinutes(10);
            }
            Main.sc.nextLine();

            System.out.print("Установить дождеватель? (y/n): ");
            String sprinklerInput = Main.sc.nextLine().toLowerCase();
            setSprinklerAttached(sprinklerInput.equals("y"));
            manageSprinkler();

            System.out.print("Введите давление воды в psi (20 - 80): ");
            if (Main.sc.hasNextInt()) {
                int pressure = Main.sc.nextInt();
                adjustWaterPressure(pressure);
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлено значение по умолчанию (40 psi)." + ANSI_RESET);
                setWaterPressure(40);
            }
            Main.sc.nextLine();

            System.out.print("Установить зимний режим? (y/n): ");
            String winterModeInput = Main.sc.nextLine().toLowerCase();
            setWinterMode(winterModeInput.equals("y"));
            manageWinterMode();

            System.out.println(ANSI_GREEN + "Система автополива готова к работе!\n" + ANSI_RESET);
            super.isOn = true;
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            System.out.println(ANSI_YELLOW + "Автополив уже выключен!\n" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "Автополив выключен. Время работы: " + getWorkingMinutes() + " мин.\n" + ANSI_RESET);
            super.isOn = false;
        }
    }

    @Override
    public void performAction() throws InterruptedException {
        if (super.isOn) {
            System.out.print(ANSI_GREEN + "Полив растений" + ANSI_RESET);
            Thread.sleep(500);
            System.out.print(ANSI_GREEN + "." + ANSI_RESET);
            Thread.sleep(500);
            System.out.print(ANSI_GREEN + "." + ANSI_RESET);
            Thread.sleep(500);
            System.out.print(ANSI_GREEN + "." + ANSI_RESET);
            Thread.sleep(500);
            System.out.println();
            turnOff();

        } else {
            System.out.println(ANSI_YELLOW + "Автополив ещё не включён!\n" + ANSI_RESET);
        }
    }

    public void adjustWorkingMinutes(int minutes) {
        if (5 <= minutes && minutes <= 90) {
            setWorkingMinutes(minutes);
            System.out.println(ANSI_GREEN + "Продолжительность полива установлена на " + getWorkingMinutes() + " мин." + ANSI_RESET);
        } else {
            setWorkingMinutes(10);
            System.out.println(ANSI_RED + "Неверный ввод. Установлена продолжительность полива по умолчанию (10 мин)." + ANSI_RESET);
        }
    }

    public void manageSprinkler() {
       if (isSprinklerAttached) {
            System.out.println(ANSI_GREEN + "Дождеватель установлен." + ANSI_RESET);
       } else {
            System.out.println(ANSI_YELLOW + "Дождеватель не установлен." + ANSI_RESET);
       }
    }

    public void adjustWaterPressure(int pressure) {
        if (20 <= pressure && pressure <= 80) {
            setWaterPressure(pressure);
            System.out.println(ANSI_GREEN + "Давление воды установлено на " + getWaterPressure() + " psi." + ANSI_RESET);
        } else {
            setWaterPressure(40);
            System.out.println(ANSI_RED + "Неверный ввод. Установлено значение по умолчанию (40 psi)." + ANSI_RESET);
        }
    }

    public void manageWinterMode() {
       if (isWinterMode) {
            System.out.println(ANSI_GREEN + "Зимний режим успешно включён!" + ANSI_RESET);
       } else {
            System.out.println(ANSI_YELLOW + "Зимний режим отключён!" + ANSI_RESET);
       }
    }

    public void setWorkingMinutes(int minutes) {
        this.workingMinutes = minutes;
    }
    public int getWorkingMinutes() {
        return workingMinutes;
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
    public boolean getStatus() {
        return isOn;
    }
}