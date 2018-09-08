package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteTest {
    private Route testRoute;
    private RoutePattern testRoutePattern;
    private RoutePattern testRoutePattern2;
    private RoutePattern testRoutePattern3;
    private List<RoutePattern> routePatterns;

    @Test
    public void testConstructor() {
        testRoute = new Route("99");
        testRoutePattern = new RoutePattern("t", "x", "x", testRoute);
        testRoutePattern2 = new RoutePattern("t", "t", "t", testRoute);
        testRoutePattern3 = new RoutePattern("t", "","", testRoute);
        assertEquals(testRoutePattern, testRoute.getPattern("t", "x", "x"));
        testRoute.addPattern(testRoutePattern2);
        assertEquals(testRoutePattern2, testRoute.getPattern("t","t","t"));
        assertEquals(testRoutePattern3, testRoute.getPattern("t", "", ""));
        assertEquals(testRoutePattern, testRoutePattern2);
    }

    @Test
    public void testgetPatternOverloaded() {
        testRoute = new Route("99");
        testRoutePattern = new RoutePattern("t", "t", "t", testRoute);
        assertEquals(testRoutePattern, testRoute.getPattern("t"));
        testRoutePattern2 = new RoutePattern("x","","",testRoute);
        assertEquals(testRoutePattern2, testRoute.getPattern("x"));
        routePatterns = new LinkedList<>();
        routePatterns.add(testRoutePattern);
        routePatterns.add(testRoutePattern2);
        assertEquals(routePatterns, testRoute.getPatterns());
    }
}
