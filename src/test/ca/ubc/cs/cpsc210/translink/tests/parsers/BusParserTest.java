package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.BusParser;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusParserTest {
    @Test
    public void testBusLocationsParserNormal() throws JSONException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearBuses();
        s.addRoute(RouteManager.getInstance().getRouteWithNumber("004"));
        s.addRoute(RouteManager.getInstance().getRouteWithNumber("014"));
        String data = "";

        try {
            data = new FileDataProvider("buslocations.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the bus locations data");
        }
        BusParser.parseBuses(s, data);

        assertEquals(4, s.getBuses().size());
    }


    @Test
    public void testBusLocationsJSONFewer() throws JSONException {
        Stop s = StopManager.getInstance().getStopWithNumber(0000);
        Stop r = StopManager.getInstance().getStopWithNumber(50216);
        r.addRoute(RouteManager.getInstance().getRouteWithNumber("014"));
        r.addRoute(RouteManager.getInstance().getRouteWithNumber("004"));
        s.clearBuses();
        String data = "";

        try {
            data = new FileDataProvider("buslocationstest.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the bus locations data");
        }
        BusParser.parseBuses(s, data);
        assertEquals(0, s.getBuses().size());
        BusParser.parseBuses(r, data);
        assertEquals(3, r.getBuses().size());

    }

    @Test
    public void badfile() throws JSONException {
        Stop s = StopManager.getInstance().getStopWithNumber(0000);
        String data = "";
        try {
            data = new FileDataProvider("badfile.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the bus locations data");
        }

        try {
            BusParser.parseBuses(s, data);
        } catch (JSONException e) {
            //expected
        }

        try {
            data = new FileDataProvider("unreadable.xml").dataSourceToString();
        } catch (IOException e) {
            // expected
        }
    }
    @Test
    public void BROKENFILE() throws JSONException {
        Stop s = new Stop(0, "name", new LatLon(0,0));
        String data = "";
        try {
            data = new FileDataProvider("BROKENBUS.json").dataSourceToString();
        } catch(IOException e) {
        }
        try {BusParser.parseBuses(s, data);}
        catch (JSONException e) {
            //expected
        }
    }

    @Test
    public void testBusLocationsParserNormalOnlyOneRoute() throws JSONException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearBuses();
        String data = "";
        s.addRoute(RouteManager.getInstance().getRouteWithNumber("004"));

        try {
            data = new FileDataProvider("buslocations.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the bus locations data");
        }

        BusParser.parseBuses(s, data);
        assertEquals(2, s.getBuses().size());
    }
}
