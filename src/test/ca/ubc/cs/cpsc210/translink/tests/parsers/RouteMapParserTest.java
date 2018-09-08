package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.RouteMapParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test the parser for route pattern map information
 */

// TODO: Write more tests

public class RouteMapParserTest {
    @BeforeEach
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    private int countNumRoutePatterns() {
        int count = 0;
        for (Route r : RouteManager.getInstance()) {
            for (RoutePattern rp : r.getPatterns()) {
                count ++;
            }
        }
        return count;
    }

    @Test
    public void testRouteParserNormal() {
        RouteMapParser p = new RouteMapParser("allroutemaps.txt");
        p.parse();
        assertEquals(1232, countNumRoutePatterns());
    }


    @Test
    public void testRouteParserSingleLong() {
        RouteMapParser p = new RouteMapParser("routemap1.txt");
        p.parse();
        assertEquals(2, countNumRoutePatterns());
        Route r = new Route("CZZZZZ");
        r.getPattern("BIG-CHICKEN-WING-6789");
        assertEquals(r, RouteManager.getInstance().getRouteWithNumber("CZZZZZ"));
        assertEquals(r, RouteManager.getInstance().getRouteWithNumber("CZZZZZ", "BIG-CHICKEN-WING-6789"));
    }

}
