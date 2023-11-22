import org.example.AutoWatering;
import static org.junit.jupiter.api.Assertions.*;

public class AutoWateringTest {
    @org.junit.jupiter.api.Test
    public void turnOnOffTest() {
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "MODEL_81", "солнечные панели", 2014, 10);
        assertFalse(autoWatering.getStatus());

        autoWatering.switchOn();
        assertTrue(autoWatering.getStatus());

        autoWatering.switchOff();
        assertFalse(autoWatering.getStatus());
    }

    @org.junit.jupiter.api.Test
    public void adjustWorkingMinutesTest() {
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "MODEL_81", "солнечные панели", 2014, 10);

        autoWatering.adjustWorkingMinutes(5);
        assertEquals(5, autoWatering.getWorkingMinutes());

        autoWatering.adjustWorkingMinutes(30);
        assertEquals(30, autoWatering.getWorkingMinutes());

        autoWatering.adjustWorkingMinutes(90);
        assertEquals(90, autoWatering.getWorkingMinutes());

        autoWatering.adjustWorkingMinutes(0);
        assertEquals(10, autoWatering.getWorkingMinutes());

        autoWatering.adjustWorkingMinutes(100);
        assertEquals(10, autoWatering.getWorkingMinutes());
    }

    @org.junit.jupiter.api.Test
    public void adjustWaterPressureTest() {
        AutoWatering autoWatering = new AutoWatering("Rain Bird", "MODEL_81", "солнечные панели", 2014, 10);

        autoWatering.adjustWaterPressure(20);
        assertEquals(20, autoWatering.getWaterPressure());

        autoWatering.adjustWaterPressure(60);
        assertEquals(60, autoWatering.getWaterPressure());

        autoWatering.adjustWaterPressure(80);
        assertEquals(80, autoWatering.getWaterPressure());

        autoWatering.adjustWaterPressure(0);
        assertEquals(40, autoWatering.getWaterPressure());

        autoWatering.adjustWaterPressure(100);
        assertEquals(40, autoWatering.getWaterPressure());
    }
}
