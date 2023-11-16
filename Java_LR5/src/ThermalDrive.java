public class ThermalDrive extends GardeningDevice {
    private int id;
    public ThermalDrive(String manufacturer, String model, String powerSource, int productionYear, int expectedLifetime) {
        super(manufacturer, model, powerSource, productionYear, expectedLifetime);
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Термопривод уже включён!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "Термопривод включён." + ANSI_RESET);
            super.isOn = true;
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            System.out.println(ANSI_YELLOW + "Термопривод уже выключен!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Термопривод выключен." + ANSI_RESET);
            super.isOn = false;
        }
    }
    @Override
    public void performAction() {
        if (super.isOn) {
            System.out.println(ANSI_GREEN + "Регуляция температуры в теплице..." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Термопривод ещё не включён!" + ANSI_RESET);
        }
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Thermal Actuator...");
    }
}