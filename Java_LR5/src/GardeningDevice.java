import java.util.Scanner;

public abstract class GardeningDevice {
    private String manufacturer;
    private String model;
    private String powerSource;
    protected boolean isOn;
    private int intensity;
    private int lifetime;
    private int productionYear;

    private final int id;
    private static int nextId = 1;

    public GardeningDevice(String manufacturer, String model, String powerSource, int productionYear, int lifetime) {
        this.id = nextId++;
        this.manufacturer = manufacturer;
        this.model = model;
        this.powerSource = powerSource;
        if (isValidYear(productionYear)) {
            this.productionYear = productionYear;
        } else {
            System.out.println(ANSI_RED + "Введён неверный год производства. Установлено значение по умолчанию (2000 год)." + ANSI_RESET);
            this.productionYear = 2000;
        }
        if (isValidLifetime(lifetime)) {
            this.lifetime = lifetime;
        } else {
            System.out.println(ANSI_RED + "Введён неверный срок службы. Установлено значение по умолчанию (5 лет)." + ANSI_RESET);
            this.lifetime = 5;
        }
        this.isOn = false;
        this.intensity = 0;
    }

    public boolean checkStatus() {
        return isOn;
    }

    public abstract void turnOn(Scanner scanner);
    public abstract void turnOff();
    public abstract void performAction(Scanner scanner);


    public boolean isExpired(int productionYear, int expectedLifetime) {
        int age = CURRENT_YEAR - productionYear;
        return age >= expectedLifetime;
    }
    public boolean isValidYear(int productionYear) {
        return 2000 < productionYear && productionYear < CURRENT_YEAR;
    }
    public boolean isValidLifetime(int expectedLifetime) {
        return 3 <= expectedLifetime && expectedLifetime <= 20;
    }



//    public abstract void performMaintenance(); // Провести обслуживание
    public String showFeatures() {
        return "";
    }



    // Геттеры и сеттеры для свойств
    public int getId() {
        return id;
    }
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
    public int getProductionYear() {
        return productionYear;
    }
    public void setProductionYear(int productionYear) {
        if (isValidYear(productionYear)) {
            this.productionYear = productionYear;
        } else {
            System.out.println(ANSI_RED + "Введён неверный год производства. Установлено значение по умолчанию (2000 год)." + ANSI_RESET);
            this.productionYear = 2000;
        }
    }
    public int getLifetime() {
        return lifetime;
    }
    public void setLifetime(int lifetime) {
        if (isValidLifetime(lifetime)) {
            this.lifetime = lifetime;
        } else {
            System.out.println(ANSI_RED + "Введён неверный срок службы. Установлено значение по умолчанию (5 лет)." + ANSI_RESET);
            this.lifetime = 5;
        }
    }
    public int getIntensity() {
        return intensity;
    }
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final int CURRENT_YEAR = 2023;
}