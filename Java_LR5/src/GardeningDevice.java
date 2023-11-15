public abstract class GardeningDevice {
    private String manufacturer;
    private String model;
    private String powerSource;
    protected boolean isOn;
    private int intensity;

//    private int id;

    private int noiseLevel;
    private int coverageAreaValue;

    public GardeningDevice(String manufacturer, String model, String powerSource) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.powerSource = powerSource;
        this.isOn = false;
        this.intensity = 0;
    }

    public boolean checkStatus() {
        return isOn;
    }

    public abstract void turnOn();
    public abstract void turnOff();
    public abstract void performAction();

    public abstract void performMaintenance(); // Провести обслуживание

    public String showFeatures() {
        return "";
    }

    public String print() {
        return this.getManufacturer() + "\t  | " + this.getModel() + "  |\t" + this.getPowerSource();
    }

    // Геттеры и сеттеры для свойств

    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getPowerSource() {
        return powerSource;
    }
    public void setPowerSource(String powerSource) {
        this.powerSource = powerSource;
    }
    public int getIntensity() {
        return intensity;
    }
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

//    public int getId() { return id; }
    public int getNoiseLevel() { return noiseLevel; }
    public void setNoiseLevel(int noiseLevel) {
        this.noiseLevel = noiseLevel;
    }
    public int getCoverageAreaValue() { return coverageAreaValue; }
    public void setCoverageAreaValue(int coverageAreaValue) {
        this.coverageAreaValue = coverageAreaValue;
    }
}