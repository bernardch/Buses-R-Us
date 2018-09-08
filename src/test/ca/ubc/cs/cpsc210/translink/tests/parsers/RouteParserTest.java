package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.parsers.RouteParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the RouteParser
 */
// TODO: Write more tests

public class RouteParserTest {
    @BeforeEach
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    public void testRouteParserNormal() throws RouteDataMissingException, JSONException, IOException {
        RouteParser p = new RouteParser("allroutes.json");
        p.parse();
        assertEquals(229, RouteManager.getInstance().getNumRoutes());
    }

    @Test
    public void testRouteParserRouteDataException1() throws RouteDataMissingException, JSONException, IOException {
        RouteParser p = new RouteParser("allroutes2.json");
        RouteManager.getInstance().clearRoutes();
        try {
            p.parse();
        } catch (RouteDataMissingException e) {
            assertEquals(228, RouteManager.getInstance().getNumRoutes());
        }
        RouteParser q = new RouteParser("allroutes4.json");
        RouteManager.getInstance().clearRoutes();
        try {
            q.parse();
        } catch(RouteDataMissingException e) {
        }
        assertEquals(1, RouteManager.getInstance().getNumRoutes());
    }
}



