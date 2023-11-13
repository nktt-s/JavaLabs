public class Lawnmower extends GardeningDevice {
    public Lawnmower(String manufacturer, String model, String powerSupply) {
        super(manufacturer, model, powerSupply);
    }

    @Override
    public void turnOn() {
        System.out.println("Lawnmower is turned on.");
    }
    @Override
    public void turnOff() {
        System.out.println("Lawnmower is turned off.");
    }
    @Override
    public void performAnAction() {
        System.out.println("Lawn mowing...");
    }
    @Override
    public void setIntensity() {
        System.out.println("Setting the intensity of the Lawnmower.");
    }
    @Override
    public void checkStatus() {
        System.out.println("Checking the status of the Lawnmower.");
    }
}