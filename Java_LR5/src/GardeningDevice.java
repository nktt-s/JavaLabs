public abstract class GardeningDevice {
    private String manufacturer;
    private String model;
    private String powerSupply;

    public GardeningDevice(String manufacturer, String model, String powerSupply) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.powerSupply = powerSupply;
    }

    public String getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public String getPowerSupply() { return powerSupply; }

    public abstract void turnOn();
    public abstract void turnOff();
    public abstract void performAnAction();
    public abstract void setIntensity();
    public abstract void checkStatus();
    public String print() {
        return this.getManufacturer() + "\t  | " + this.getModel() + "  |\t" + this.getPowerSupply();
    }
}