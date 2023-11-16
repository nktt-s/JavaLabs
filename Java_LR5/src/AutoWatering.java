public class AutoWatering extends GardeningDevice {
    public AutoWatering(String manufacturer, String model, String powerSource, int productionYear, int expectedLifetime) {
        super(manufacturer, model, powerSource, productionYear, expectedLifetime);
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Автополив уже включён!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "Автополив включён." + ANSI_RESET);
            super.isOn = true;
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            System.out.println(ANSI_YELLOW + "Автополив уже выключен!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Автополив выключен." + ANSI_RESET);
            super.isOn = false;
        }
    }
    @Override
    public void performAction() {
        if (super.isOn) {
            System.out.println(ANSI_GREEN + "Полив растений..." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Автополив ещё не включён!" + ANSI_RESET);
        }
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Autowatering system...");
    }
}