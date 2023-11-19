import java.util.Scanner;

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
    public void turnOn(Scanner scanner) {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Автополив уже включён!" + ANSI_RESET);
        } else {
            System.out.print("Введите продолжительность полива в минутах (5 - 90): ");
            if (scanner.hasNextInt()) {
                int minutes = scanner.nextInt();
                adjustWorkingMinutes(minutes);
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлено значение по умолчанию (10 мин)." + ANSI_RESET);
                setWorkingMinutes(10);
            }
            scanner.nextLine();

            System.out.print("Установить дождеватель? (да/нет): ");
            String sprinklerInput = scanner.nextLine().toLowerCase();
            setSprinklerAttached(sprinklerInput.equals("да"));
            manageSprinkler();

            System.out.print("Введите давление воды в psi (20 - 80): ");
            if (scanner.hasNextInt()) {
                int pressure = scanner.nextInt();
                adjustWaterPressure(pressure);
            } else {
                System.out.println(ANSI_YELLOW + "Неверный ввод. Установлено значение по умолчанию (40 psi)." + ANSI_RESET);
                setWaterPressure(40);
            }
            scanner.nextLine();

            System.out.print("Установить зимний режим? (да/нет): ");
            String winterModeInput = scanner.nextLine().toLowerCase();
            setWinterMode(winterModeInput.equals("да"));
            manageWinterMode();

            System.out.println(ANSI_GREEN + "Автополив включён.\n" + ANSI_RESET);
            super.isOn = true;
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            System.out.println(ANSI_YELLOW + "Автополив уже выключен!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Автополив выключен. Время работы: " + getWorkingMinutes() + " мин.\n" + ANSI_RESET);
            super.isOn = false;
        }
    }
    @Override
    public void performAction(Scanner scanner) {
        if (super.isOn) {
            System.out.println(ANSI_GREEN + "Полив растений...\n" + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Автополив ещё не включён!" + ANSI_RESET);
        }
    }

    public void adjustWorkingMinutes(int minutes) {
        if (5 <= minutes && minutes <= 90) {
            setWorkingMinutes(minutes);
            System.out.println(ANSI_GREEN + "Продолжительность полива установлена на " + getWorkingMinutes() + " мин." + ANSI_RESET);
        } else {
            setWorkingMinutes(10);
            System.out.println(ANSI_YELLOW + "Неверный ввод. Установлена продолжительность полива по умолчанию (10 мин)." + ANSI_RESET);
        }
    }

    public void manageSprinkler() {
       if (isSprinklerAttached) {
            System.out.println(ANSI_GREEN + "Дождеватель успешно установлен!" + ANSI_RESET);
       } else {
            System.out.println(ANSI_RED + "Дождеватель не установлен!" + ANSI_RESET);
       }
    }

    public void adjustWaterPressure(int pressure) {
        if (20 <= pressure && pressure <= 80) {
            setWaterPressure(pressure);
            System.out.println(ANSI_GREEN + "Давление воды установлено на " + getWaterPressure() + " psi." + ANSI_RESET);
        } else {
            setWaterPressure(40);
            System.out.println(ANSI_YELLOW + "Неверный ввод. Установлено значение по умолчанию (40 psi)." + ANSI_RESET);
        }
    }

    public void manageWinterMode() {
       if (isWinterMode) {
            System.out.println(ANSI_GREEN + "Зимний режим успешно включён!" + ANSI_RESET);
       } else {
            System.out.println(ANSI_RED + "Зимний режим отключён!" + ANSI_RESET);
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
}