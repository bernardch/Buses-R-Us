package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class StopTest {
    private Stop testStop;
    private LatLon testLatLon;
    private Route testRoute;
    private Route testRoute2;
    private Bus testBus;
    private Bus testBus2;

    @BeforeEach
    public void runBefore() {
        testLatLon = new LatLon(0,0);
        testRoute = new Route("99");
        testRoute2 = new Route("14");
        testBus = new Bus(testRoute, 0,0,"testdest","1500");
        testBus2 = new Bus(testRoute2, 0, 0, "testdest", "1500");
    }
    @Test
    public void testConstructor() {
        testStop = new Stop(0, "name", testLatLon);
        assertTrue(!testStop.onRoute(testRoute));
        testRoute.addStop(testStop);
        testStop.addRoute(testRoute);
        assertTrue(testStop.onRoute(testRoute));
    }

    @Test
    public void testaddBus() throws RouteException {
            testStop = new Stop(0, "name", testLatLon);
            testStop.addRoute(testRoute); //? redundant?
            testRoute.addStop(testStop);
            testStop.addBus(testBus);
            try {
            testStop.addBus(testBus2);
            fail("RouteException should have been thrown");
        } catch (RouteException e) {
            // expected
        }
    }
}
