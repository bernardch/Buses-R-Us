package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Test the StopManager
 */
public class StopManagerTest {

    @BeforeEach
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testBasic() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999);
        assertEquals(s9999, r);
    }

    @Test
    public void testComplex() {
        Stop s99 = new Stop(99, "", new LatLon(-49.0,122.0));
        Stop r = StopManager.getInstance().getStopWithNumber(99, "", new LatLon(-49.0, 122.0));
        assertEquals(s99, r);
        Stop z = StopManager.getInstance().getStopWithNumber(99,"x", new LatLon(0.0,0.0));
        assertEquals(s99,z);
        Stop x = StopManager.getInstance().getStopWithNumber(100, "x", new LatLon(0.0, 0.0));
        assertTrue(!s99.equals(x));
    }

    @Test
    public void testSelection() {
        Stop r = StopManager.getInstance().getStopWithNumber(99);
        Stop z = StopManager.getInstance().getStopWithNumber(101);
        Stop q = new Stop(103, "", new LatLon(-49.0, 122.0));
        assertEquals(null, StopManager.getInstance().getSelected());
        assertEquals(2, StopManager.getInstance().getNumStops());
        assertEquals( r, StopManager.getInstance().getStopWithNumber(99));
        assertEquals(z, StopManager.getInstance().getStopWithNumber(101));
        try {
            StopManager.getInstance().setSelected(z);
            StopManager.getInstance().setSelected(q);
            assertEquals(z, StopManager.getInstance().getSelected());
        } catch (StopException e) {
            //expected
        }
    }

    @Test
    public void testFindNearestTo() {
        Stop a = StopManager.getInstance().getStopWithNumber(1,"test1",new LatLon(1.00,1.00));
        assertEquals(1, StopManager.getInstance().getNumStops());
        assertEquals(a, StopManager.getInstance().findNearestTo(new LatLon(1.01,1.01)));
        assertEquals(null, StopManager.getInstance().findNearestTo(new LatLon(100, 100)));
    }
}
