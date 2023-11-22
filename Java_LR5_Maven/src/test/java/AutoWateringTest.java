import org.example.AutoWatering;
import static org.junit.jupiter.api.Assertions.*;

public class AutoWateringTest {
    @org.junit.jupiter.api.Test
    public void turnOnOffTest() {
        AutoWatering autoWatering = new AutoWatering("Karcher", "Model_1", "бензин", 2010, 8);
        autoWatering.switchOn();
        assertTrue(autoWatering.getOnOrOff());

        autoWatering.switchOff();
        assertFalse(autoWatering.getOnOrOff());
    }


}
