import java.util.List;
import java.util.ArrayList;

public class GardeningDeviceManager {
    private List<GardeningDevice> devices;

    public GardeningDeviceManager() {
        this.devices = new ArrayList<>();
    }
    public void addDevice(GardeningDevice device) {
        devices.add(device);
    }
    public void removeDevice(int index) {
        devices.remove(index);
    }
    public void showObjects() {
        System.out.println("\nID | Manufacturer | Model | Power Supply");
        System.out.println("=================================================");
        if (this.devices.isEmpty()) {
            System.out.println("------------------ NO OBJECTS -------------------");
        } else {
            for (int i = 0; i < devices.size(); ++i) {
                System.out.println(" " + i + " | " + devices.get(i).print());
            }
        }
    }
    public List<GardeningDevice> getDevices() {
        return devices;
    }

}
