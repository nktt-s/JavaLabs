public class AutoWatering extends GardeningDevice {
    public AutoWatering(String manufacturer, String model, String powerSource) {
        super(manufacturer, model, powerSource);
    }

    @Override
    public void turnOn() {
        System.out.println("AutoWatering is turned on.");
        super.isOn = true;
    }
    @Override
    public void turnOff() {
        System.out.println("AutoWatering is turned off.");
        super.isOn = false;
    }
    @Override
    public void performAction() {
        System.out.println("Automatic watering is watering the plants...");
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Autowatering system...");
    }
}