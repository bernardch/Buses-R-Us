package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test Bus
 */
public class BusTest {
    private Bus testBus;
    private Route testRoute;
    private LatLon testlatLon;

    @Test
    public void testConstructor() {
        testBus = new Bus(testRoute, 0,0, "testdest", "testtime");
    }
}
