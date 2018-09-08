package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoutePatternTest {
    private RoutePattern testRoutePattern;
    private Route testRoute;
    private List<LatLon> testPath;

    @Test
    public void testConstructor() {
        testRoute = new Route("testrnumber");
        testRoutePattern = new RoutePattern("testrpname", "testrpdestination",
                "testrpdirection", testRoute);
        assertEquals("testrpname", testRoutePattern.getName());
        assertEquals("testrpdestination", testRoutePattern.getDestination());
        assertEquals("testrpdirection", testRoutePattern.getDirection());
        assertTrue(testRoutePattern.getPath().isEmpty());
    }
}
