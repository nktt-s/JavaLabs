public class ThermalActuator extends GardeningDevice {
    public ThermalActuator(String manufacturer, String model, String powerSupply) {
        super(manufacturer, model, powerSupply);
    }

    @Override
    public void turnOn() {
        System.out.println("ThermalActuator is turned on.");
    }
    @Override
    public void turnOff() {
        System.out.println("ThermalActuator is turned off.");
    }
    @Override
    public void performAnAction() {
        System.out.println("Performing greenhouse temperature control...");
    }
    @Override
    public void setIntensity() {
        System.out.println("Setting the intensity of the ThermalActuator.");
    }
    @Override
    public void checkStatus() {
        System.out.println("Checking the status of the ThermalActuator.");
    }
}