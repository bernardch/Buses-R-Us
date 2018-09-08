package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;
    private boolean throwable;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
        throwable = false;
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)
     *     <li>JSON data is not an array
     * </ul>
     * If a JSONException is thrown, no routes should be added to the route manager
     *
     * @throws RouteDataMissingException when
     * <ul>
     *  <li>JSON data is missing RouteNo, Name, or Patterns element for any route</li>
     *  <li>The value of the Patterns element is not an array for any route</li>
     *  <li>JSON data is missing PatternNo, Destination, or Direction element for any route pattern</li>
     * </ul>
     * If a RouteDataMissingException is thrown, all correct routes are first added to the route manager.
     */

    public void parseRoutes(String jsonResponse) throws JSONException, RouteDataMissingException {
        JSONArray routedata = new JSONArray(jsonResponse);
        for (int index = 0; index < routedata.length(); index++) {
            JSONObject route = routedata.getJSONObject(index);
            try {
                parseRoute(route);
            } catch (JSONException e) {
                throw new JSONException("JSON Exception");
            }
        }
        if (throwable)
            throw new RouteDataMissingException();
    }
    public void parseRoute(JSONObject route) throws JSONException, RouteDataMissingException{
        String Name = route.getString("Name");
        String RouteNo = route.getString("RouteNo");
        if (!Name.isEmpty() && !RouteNo.isEmpty()) {
            JSONArray patterndata = route.getJSONArray("Patterns");
            for (int index = 0; index < patterndata.length(); index++) {
                JSONObject pattern = patterndata.getJSONObject(index);
                try {
                    parsePattern(pattern, RouteNo, Name);
                } catch (RouteDataMissingException e) {
                    throw new RouteDataMissingException("Route Data Missing Exception");
                }
            }
        }
        else throwable = true;
    }

    public void parsePattern(JSONObject pattern, String RouteNo, String Name) throws JSONException, RouteDataMissingException {
        String Destination = pattern.getString("Destination");
        String Direction = pattern.getString("Direction");
        String PatternNo = pattern.getString("PatternNo");
        if (!PatternNo.isEmpty() && !Destination.isEmpty() && !Direction.isEmpty()) {//Patterns element is not an array for any route? /* add HERE */
            Route tempr = RouteManager.getInstance().getRouteWithNumber(RouteNo, Name);
            tempr.getPattern("",Destination, Direction);
        }
        else
            throwable = true;
    }
}

