public class Lawnmower extends GardeningDevice {
    public Lawnmower(String manufacturer, String model, String powerSource) {
        super(manufacturer, model, powerSource);
    }

    @Override
    public void turnOn() {
        if (super.isOn) {
            // TODO КРАСИВЫЙ ВЫВОД В ANSI-ЦВЕТАХ (жёлтый - предупреждение, зелёный - ON, красный - OFF
            System.out.println("Lawnmower is already turned on!");
        } else {
            System.out.println("Lawnmower is turned on.");
            super.isOn = true;
        }
    }
    @Override
    public void turnOff() {
        if (!super.isOn) {
            // TODO КРАСИВЫЙ ВЫВОД В ANSI-ЦВЕТАХ (жёлтый - предупреждение, зелёный - ON, красный - OFF
            System.out.println("Lawnmower is already turned off!");
        } else {
            System.out.println("Lawnmower is turned off.");
            super.isOn = false;
        }
    }
    @Override
    public void performAction() {
        // TODO ПРОВЕРКА НА ТО, ЧТО УСТРОЙСТВО ВКЛЮЧЕНО
        System.out.println("Lawnmower is cutting the grass...");
    }
    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance of Lawnmower...");
    }
}