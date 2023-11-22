import org.example.ThermalDrive;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ThermalDriveTest {
    @org.junit.jupiter.api.Test
    public void turnOnOffTest() {
        ThermalDrive thermalDrive = new ThermalDrive("Thermovent", "MODEL_77", "сетевое питание", 2018, 6);
        assertFalse(thermalDrive.getStatus());

        thermalDrive.switchOn();
        assertTrue(thermalDrive.getStatus());

        thermalDrive.switchOff();
        assertFalse(thermalDrive.getStatus());
    }

    @org.junit.jupiter.api.Test
    public void manageProtectiveFunctionTest() {
        ThermalDrive thermalDrive = new ThermalDrive("Thermovent", "MODEL_77", "сетевое питание", 2018, 6);
        assertFalse(thermalDrive.getProtectiveFunction());

        thermalDrive.setProtectiveFunction(true);
        assertTrue(thermalDrive.getProtectiveFunction());

        thermalDrive.setProtectiveFunction(false);
        assertFalse(thermalDrive.getProtectiveFunction());
    }

    @org.junit.jupiter.api.Test
    public void CalibrateTemperatureSensorsTest() throws InterruptedException {
        ThermalDrive thermalDrive = new ThermalDrive("Thermovent", "MODEL_77", "сетевое питание", 2018, 6);
        assertFalse(thermalDrive.isCalibrated());

        thermalDrive.setCalibrateTemperatureSensors(true);
        thermalDrive.manageCalibrationTemperatureSensors();
        assertTrue(thermalDrive.isCalibrated());
    }
}
