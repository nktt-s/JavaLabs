import java.util.Scanner;

public abstract class GardeningDevice {
    private String manufacturer;
    private String model;
    private String powerSource;
    private int intensity;
    protected boolean isOn;
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
            System.out.println(ANSI_RED + "Введённое значение не соответствует допустимому диапазону. Установлен год производства по умолчанию (2000 год)." + ANSI_RESET);
            this.productionYear = 2000;
        }
        if (isValidLifetime(lifetime)) {
            this.lifetime = lifetime;
        } else {
            System.out.println(ANSI_RED + "Введённое значение не соответствует допустимому диапазону. Установлен срок службы по умолчанию (5 лет)." + ANSI_RESET);
            this.lifetime = 5;
        }
        this.isOn = false;
        this.intensity = 0;
    }

    public void checkStatus() {
        if (isOn) {
            System.out.println(ANSI_GREEN + "Устройство готово к работе!\n" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Устройство выключено!\n" + ANSI_RESET);
        }
    }

    public abstract void turnOn(Scanner scanner) throws InterruptedException;
    public abstract void turnOff();
    public abstract void performAction(Scanner scanner) throws InterruptedException;


    public void isExpired() {
        int age = CURRENT_YEAR - this.productionYear;
        if (age >= this.lifetime) {
            System.out.println(ANSI_RED + "Срок службы устройства истёк!\n" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "Срок службы устройства ещё не истёк!\n" + ANSI_RESET);
        }
    }
    public boolean isValidYear(int productionYear) {
        return 2000 < productionYear && productionYear < CURRENT_YEAR;
    }
    public boolean isValidLifetime(int expectedLifetime) {
        return 3 <= expectedLifetime && expectedLifetime <= 20;
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

    public void adjustIntensity(int intensity) {
        if (10 <= intensity && intensity <= 100) {
            setIntensity(intensity);
            System.out.println(ANSI_GREEN + "Интенсивность работы устройства установлена на " + getIntensity() + "%." + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Неверный ввод. Установлена интенсивность работы устройства по умолчанию (75%)." + ANSI_RESET);
            setIntensity(75);
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final int CURRENT_YEAR = 2023;
}