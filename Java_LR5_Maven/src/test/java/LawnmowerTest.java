import org.example.Lawnmower;
import static org.junit.jupiter.api.Assertions.*;

public class LawnmowerTest {
    @org.junit.jupiter.api.Test
    public void turnOnOffTest() {
        Lawnmower lawnmower = new Lawnmower("Karcher", "Model_1", "бензин", 2010, 8);
        lawnmower.switchOn();
        assertTrue(lawnmower.getOnOrOff());

        lawnmower.switchOff();
        assertFalse(lawnmower.getOnOrOff());
    }

    @org.junit.jupiter.api.Test
    public void adjustCuttingHeightTest() {
        Lawnmower lawnmower = new Lawnmower("Karcher", "Model_1", "бензин", 2010, 8);
        assertEquals(40, lawnmower.getCuttingHeight());

        lawnmower.adjustCuttingHeight(20);
        assertEquals(20, lawnmower.getCuttingHeight());

        lawnmower.adjustCuttingHeight(80);
        assertEquals(80, lawnmower.getCuttingHeight());

        lawnmower.adjustCuttingHeight(120);
        assertEquals(40, lawnmower.getCuttingHeight());

        lawnmower.adjustCuttingHeight(0);
        assertEquals(40, lawnmower.getCuttingHeight());
    }

    @org.junit.jupiter.api.Test
    public void checkBatteryLevelTest() {
        Lawnmower lawnmower = new Lawnmower("Karcher", "Model_1", "аккумулятор", 2010, 8);
        assertEquals(100, lawnmower.getBatteryLevel());

        lawnmower.batteryDischarge();
        assertEquals(80, lawnmower.getBatteryLevel());

        lawnmower.batteryDischarge();
        assertEquals(60, lawnmower.getBatteryLevel());

        lawnmower.batteryDischarge();
        assertEquals(40, lawnmower.getBatteryLevel());

        lawnmower.batteryDischarge();
        assertEquals(20, lawnmower.getBatteryLevel());

        lawnmower.batteryDischarge();
        assertEquals(0, lawnmower.getBatteryLevel());

        Lawnmower lawnmower2 = new Lawnmower("Karcher", "Model_1", "бензин", 2010, 8);
        assertEquals(0, lawnmower2.getBatteryLevel());
    }
}
