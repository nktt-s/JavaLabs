public class AutoWatering extends GardeningDevice {
    public AutoWatering(String manufacturer, String model, String powerSupply) {
        super(manufacturer, model, powerSupply);
    }

    @Override
    public void turnOn() {
        System.out.println("AutoWatering is turned on.");
    }
    @Override
    public void turnOff() {
        System.out.println("AutoWatering is turned off.");
    }
    @Override
    public void performAnAction() {
        System.out.println("Automatic watering...");
    }
    @Override
    public void setIntensity() {
        System.out.println("Setting the intensity of the AutoWatering.");
    }
    @Override
    public void checkStatus() {
        System.out.println("Checking the status of the AutoWatering.");
    }
}