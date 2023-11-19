import java.util.Scanner;

public class AutoWatering extends GardeningDevice {
    public AutoWatering(String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
        super(manufacturer, model, powerSource, productionYear, lifetime);
    }

    @Override
    public void turnOn(Scanner scanner) {
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
    public void performAction(Scanner scanner) {
        if (super.isOn) {
            System.out.println(ANSI_GREEN + "Полив растений..." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Автополив ещё не включён!" + ANSI_RESET);
        }
    }
//    @Override
//    public void performMaintenance() {
//        System.out.println("Performing maintenance of Autowatering system...");
//    }
}