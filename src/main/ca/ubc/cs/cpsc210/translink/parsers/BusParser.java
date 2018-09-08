package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Parser for bus data
public class BusParser {

    /**
     * Parse buses from JSON response produced by TransLink query.  All parsed buses are
     * added to the given stop.  Bus location data that is missing any of the required
     * fields (RouteNo, Latitude, Longitude, Destination, RecordedTime) is silently
     * ignored and not added to stop. Bus that is on route that does not pass through
     * this stop is silently ignored and not added to stop.
     *
     * @param stop         stop to which parsed buses are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException when:
     *                       <ul>
     *                       <li>JSON response does not have expected format (JSON syntax problem)</li>
     *                       <li>JSON response is not a JSON array</li>
     *                       </ul>
     */
    public static void parseBuses(Stop stop, String jsonResponse) throws JSONException {
        // TODO: implement this method
        JSONArray busdata = new JSONArray(jsonResponse);
        for (int index = 0; index < busdata.length(); index++) {
            JSONObject bus = busdata.getJSONObject(index);
            try {
                parseBus(stop, bus);
            } catch (JSONException e) {
                throw new JSONException("JSON Exception");
            }
        }
    }

    public static void parseBus(Stop stop, JSONObject bus) throws JSONException {
        String RouteNo = bus.getString("RouteNo");
        Double Latitude = bus.getDouble("Latitude");
        Double Longitude = bus.getDouble("Longitude");
        String Destination = bus.getString("Destination");
        String RecordedTime = bus.getString("RecordedTime");
        Route routeno = RouteManager.getInstance().getRouteWithNumber(RouteNo);
        Bus tempBus = new Bus(routeno, Latitude, Longitude, Destination, RecordedTime);
        if (!(RouteNo.isEmpty() && Latitude.isNaN() && Longitude.isNaN()
                && Destination.isEmpty() && RecordedTime.isEmpty())) {
            try {
                stop.addBus(tempBus);
            } catch (RouteException e) {
            }
        }
    }
}

// WHERE/WHEN THE FUCK DO YOU ADD STOPS TO THE ROUTE? THE BUS' ROUTE DOES NOT CONTAIN THE GIVEN STOP!
// ......... we need to add 2 lines of code to add the stops to the