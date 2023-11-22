package org.example;

import java.util.List;
import java.util.ArrayList;

public class GardeningDeviceManager {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    private List<GardeningDevice> devices;

    public GardeningDeviceManager() {
        this.devices = new ArrayList<>();
    }
    public void addDevice(GardeningDevice device) {
        devices.add(device);
    }
    public GardeningDevice getDeviceByID(int id) {
        for (GardeningDevice device : devices) {
            if (device.getId() == id) {
                return device;
            }
        }
        return null;
    }
    public void removeDeviceByID(int id) {
        GardeningDevice deviceToRemove = getDeviceByID(id);
        if (deviceToRemove != null) {
            devices.remove(deviceToRemove);
            System.out.println(ANSI_GREEN + "\nУстройство успешно удалено!" + ANSI_RESET);
            showObjects();
        } else {
            Main.logger.warn("Попытка ввода значения ID несуществующего устройства.");
            System.out.println(ANSI_RED + "\nУстройство с ID = " + id + " не найдено!\n" + ANSI_RESET);
        }
    }
    public void showObjects() {
        System.out.println("\nID\tТИП\t\t\t\tПРОИЗВОДИТЕЛЬ\t\tМОДЕЛЬ\t\tИСТОЧНИК ПИТАНИЯ\t\tГОД ПРОИЗВОДСТВА\tСРОК СЛУЖБЫ");
        System.out.println("=================================================================================================================");
        if (this.devices.isEmpty()) {
            System.out.println("--------------------------------------------------- NO OBJECTS --------------------------------------------------");
        } else {
            for (GardeningDevice device : devices) {
                System.out.printf("%-2d\t%-15s\t%-16s\t%-10s\t%-20s\t\t%-16s\t%-11s\n", device.getId(), device.getClass().toString().substring(6),
                        device.getManufacturer(), device.getModel(), device.getPowerSource(), device.getProductionYear(), device.getLifetime());
            }
        }
        System.out.println();
    }
    public List<GardeningDevice> getDevices() {
        return devices;
    }

}
