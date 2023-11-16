public class Lawnmower extends GardeningDevice {
    public Lawnmower(String manufacturer, String model, String powerSource, int productionYear, int expectedLifetime) {
        super(manufacturer, model, powerSource, productionYear, expectedLifetime);
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            System.out.println(ANSI_YELLOW + "Газонокосилка уже включена!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "Газонокосилка включена." + ANSI_RESET);
            super.isOn = true;
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
    public void performAction() {
        if (super.isOn) {
            System.out.println(ANSI_GREEN + "Газонокосилка косит траву..." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Газонокосилка ещё не включена!" + ANSI_RESET);
        }
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Lawnmower...");
    }
}