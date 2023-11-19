import java.util.Scanner;

public class Lawnmower extends GardeningDevice {
    private boolean isGrassCollectionFull; // Заполненность травосборника
    private boolean isMulchingEnabled; // Система мульчирования - поверхностное покрытие почвы скошенной измельчённой травой для её защиты и улучшения свойств
    private int cuttingHeight; // Высота среза
    private int batteryLevel; // Уровень заряда аккумулятора (в случае если установлен аккумулятор)

    public Lawnmower(String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
        if (super.getPowerSource().equalsIgnoreCase("Аккумулятор")) {
            this.batteryLevel = 100;
        }
        this.isGrassCollectionFull = false;
        this.isMulchingEnabled = false;
        setCuttingHeight(40);
    }

    @Override
    public void turnOn(Scanner scanner) {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Газонокосилка уже включена!" + ANSI_RESET);
        } else {
            System.out.print("Включить мульчирование? (да/нет): ");
            scanner.nextLine();
            String mulchingInput = scanner.nextLine().toLowerCase();
            if (mulchingInput.equals("да")) {
                isMulchingEnabled = true;
                manageMulching();
            } else if (mulchingInput.equals("нет")) {
                isMulchingEnabled = false;
                manageMulching();
            } else {
                System.out.println(ANSI_YELLOW + "Неверный ввод. Мульчирование отключено!" + ANSI_RESET);
            }

            System.out.print("Введите желаемую высоту среза (20 - 100 мм): ");
            if (scanner.hasNextInt()) {
                int height = scanner.nextInt();
                adjustCuttingHeight(height);
            } else {
                System.out.println(ANSI_RED + "Неверный ввод. Установлена высота среза по умолчанию (40 мм)." + ANSI_RESET);
                adjustCuttingHeight(40);
            }

            checkBatteryLevel();
            if (super.getPowerSource().equalsIgnoreCase("Аккумулятор")) {
                if (batteryLevel < 10) {
                    System.out.println(ANSI_RED + "Низкий уровень заряда аккумулятора! Газонокосилка не может быть включена." + ANSI_RESET);
                    System.out.println("Зарядить аккумулятор? (да/нет): ");
                    String chargeInput = scanner.nextLine().toLowerCase();
                    if (chargeInput.equals("да")) {
                        batteryCharge();
                    }
                } else {
                    System.out.println(ANSI_GREEN + "Газонокосилка включена." + ANSI_RESET);
                    super.isOn = true;
                }
            } else {
                System.out.println(ANSI_GREEN + "Газонокосилка включена." + ANSI_RESET);
                super.isOn = true;
            }
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            System.out.println(ANSI_YELLOW + "Газонокосилка уже выключена!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Газонокосилка выключена." + ANSI_RESET);
            super.isOn = false;
        }
    }
    @Override
    public void performAction(Scanner scanner) {
        if (super.isOn) {
            if (super.getPowerSource().equalsIgnoreCase("Аккумулятор")) {
                if (batteryLevel < 10) {
                    System.out.println(ANSI_RED + "Низкий уровень заряда аккумулятора! Газонокосилка не может работать." + ANSI_RESET);
                    System.out.println("Зарядить аккумулятор? (да/нет): ");
                    String chargeInput = scanner.nextLine().toLowerCase();
                    scanner.nextLine();
                    if (chargeInput.equals("да")) {
                        batteryCharge();
                        System.out.println(ANSI_GREEN + "Газонокосилка косит траву..." + ANSI_RESET);
                    } else {
                        System.out.println(ANSI_RED + "Газонокосилка выключена." + ANSI_RESET);
                        super.isOn = false;
                    }
                } else {
                    System.out.println(ANSI_GREEN + "Газонокосилка косит траву..." + ANSI_RESET);
                    batteryDischarge();
                    checkBatteryLevel();
                }
            } else {
                System.out.println(ANSI_GREEN + "Газонокосилка косит траву..." + ANSI_RESET);
                isGrassCollectionFull = true;
                emptyGrassCollector();
            }
        } else {
            System.out.println(ANSI_YELLOW + "Газонокосилка ещё не включена!" + ANSI_RESET);
        }
    }

    public void manageMulching() {
        if (isMulchingEnabled) {
            System.out.println(ANSI_GREEN + "Система мульчирования включена." + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Система мульчирования отключена." + ANSI_RESET);
        }
    }

    public void adjustCuttingHeight(int height) {
        if (20 <= height && height <= 100) {
            setCuttingHeight(height);
            System.out.println(ANSI_GREEN + "Высота среза установлена на " + getCuttingHeight() + " мм." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Неверный ввод. Установлена высота среза по умолчанию (40 мм)." + ANSI_RESET);
            setCuttingHeight(40);
        }
    }

    public void emptyGrassCollector() {
        if (isGrassCollectionFull) {
            System.out.println(ANSI_YELLOW + "Травосборник полон. Выполняется очистка..." + ANSI_RESET);
            isGrassCollectionFull = false;
            System.out.println(ANSI_GREEN + "Травосборник очищен!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "Травосборник пуст. Очистка не требуется!" + ANSI_RESET);
        }
    }

    public void batteryDischarge() {
        if (super.getPowerSource().equalsIgnoreCase("Аккумулятор")) {
            batteryLevel -= 20;
        }
    }

    public void batteryCharge() {
        if (super.getPowerSource().equalsIgnoreCase("Аккумулятор")) {
            System.out.println(ANSI_GREEN + "Аккумулятор полностью заряжен." + ANSI_RESET);
            batteryLevel = 100;
        }
    }

    public void checkBatteryLevel() {
        if (super.getPowerSource().equalsIgnoreCase("Аккумулятор")) {
            if (70 <= batteryLevel && batteryLevel <= 100) {
                System.out.println(ANSI_GREEN + "Уровень заряда аккумулятора: " + batteryLevel + "%." + ANSI_RESET);
            } else if (40 <= batteryLevel && batteryLevel < 70) {
                System.out.println(ANSI_YELLOW + "Уровень заряда аккумулятора: " + batteryLevel + "%." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Внимание! Низкий уровень заряда аккумулятора: " + batteryLevel + "%." + ANSI_RESET);
            }
        }
    }
    public int getCuttingHeight() {
        return cuttingHeight;
    }
    public void setCuttingHeight(int cuttingHeight) {
        this.cuttingHeight = cuttingHeight;
    }
}