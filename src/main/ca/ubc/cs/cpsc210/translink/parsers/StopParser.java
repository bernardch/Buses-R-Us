package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    private boolean throwable;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)</li>
     *     <li>JSON data is not an array</li>
     * </ul>
     * If a JSONException is thrown, no stops should be added to the stop manager
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     * If a StopDataMissingException is thrown, all correct stops are first added to the stop manager.
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray stops = new JSONArray(jsonResponse);

        for (int index = 0; index < stops.length(); index++) {
            JSONObject stop = stops.getJSONObject(index);
            try {
                parseStop(stop);
            } catch (JSONException e) {
                throw new JSONException("JSON Exception");
            }
        }
        if (throwable)
            throw new StopDataMissingException();
    }

    public void parseStop(JSONObject stop) throws JSONException, StopDataMissingException {
        String Name = stop.getString("Name");
        Integer Number = stop.getInt("StopNo");
        Double Latitude = stop.getDouble("Latitude");
        Double Longitude = stop.getDouble("Longitude");
        LatLon tempLatLon = new LatLon(Latitude, Longitude);
        String Routes = stop.getString("Routes");
        String[] SetRoutes = Routes.split(",\\s?+");
        if (Name.isEmpty() || Number.toString().isEmpty() || Latitude.isNaN() || Longitude.isNaN() || Routes.isEmpty())
            throwable = true;
        else {
            Stop tempStop = StopManager.getInstance().getStopWithNumber(Number, Name, tempLatLon);
            for (String s : SetRoutes) {
                Route tempRoute = new Route(s);
                tempStop.addRoute(tempRoute);
                RouteManager.getInstance().getRouteWithNumber(s);
            }
        }
    }
}
