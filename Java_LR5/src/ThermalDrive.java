public class ThermalDrive extends GardeningDevice {
    private int id;
    public ThermalDrive(String manufacturer, String model, String powerSource) {
        super(manufacturer, model, powerSource);
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            // TODO КРАСИВЫЙ ВЫВОД В ANSI-ЦВЕТАХ (жёлтый - предупреждение, зелёный - ON, красный - OFF
            System.out.println("ThermalDrive is already turned on!");
        } else {
            System.out.println("ThermalDrive is turned on.");
            super.isOn = true;
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            // TODO КРАСИВЫЙ ВЫВОД В ANSI-ЦВЕТАХ (жёлтый - предупреждение, зелёный - ON, красный - OFF
            System.out.println("ThermalDrive is already turned off!");
        } else {
            System.out.println("ThermalDrive is turned off.");
            super.isOn = false;
        }
    }
    @Override
    public void performAction() {
        // TODO ПРОВЕРКА НА ТО, ЧТО УСТРОЙСТВО ВКЛЮЧЕНО
        System.out.println("Greenhouse thermostat is regulating the temperature...");
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Thermal Actuator...");
    }
}