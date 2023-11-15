public class Lawnmower extends GardeningDevice {
    public Lawnmower(String manufacturer, String model, String powerSource) {
        super(manufacturer, model, powerSource);
    }

    @Override
    public void turnOn() {
        System.out.println("Lawnmower is turned on.");
        super.isOn = true;
    }
    @Override
    public void turnOff() {
        System.out.println("Lawnmower is turned off.");
        super.isOn = false;
    }
    @Override
    public void performAction() {
        System.out.println("Lawnmower is cutting the grass...");
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Lawnmower...");
    }
}