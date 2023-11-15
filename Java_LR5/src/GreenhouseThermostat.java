public class GreenhouseThermostat extends GardeningDevice {
    private int id;
    public GreenhouseThermostat(String manufacturer, String model, String powerSource) {
        super(manufacturer, model, powerSource);
    }

    @Override
    public void turnOn() {
        System.out.println("ThermalActuator is turned on.");
        super.isOn = true;
    }
    @Override
    public void turnOff() {
        System.out.println("ThermalActuator is turned off.");
        super.isOn = false;
    }
    @Override
    public void performAction() {
        System.out.println("Greenhouse thermostat is regulating the temperature...");
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Thermal Actuator...");
    }
}