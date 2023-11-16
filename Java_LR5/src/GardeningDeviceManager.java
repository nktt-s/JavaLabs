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
            System.out.println(ANSI_RED + "\nУстройство с ID = " + id + " не найдено!\n" + ANSI_RESET);
        }
    }
    public void showObjects() {
        System.out.println("\nID\t\tType\t\t\tManufacturer\t\tModel\t\tPower Source");
        System.out.println("======================================================================");
        if (this.devices.isEmpty()) {
            System.out.println("----------------------------- NO OBJECTS -----------------------------");
        } else {
            for (int i = 0; i < devices.size(); ++i) {
                System.out.printf("%-6d\t%-15s\t%-16s\t%-10s\t%-15s\n", devices.get(i).getId(), devices.get(i).getClass().toString().substring(6), devices.get(i).getManufacturer(), devices.get(i).getModel(), devices.get(i).getPowerSource());
            }
        }
        System.out.println();
    }
    public List<GardeningDevice> getDevices() {
        return devices;
    }

}
