package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when:
     *                                      <ul>
     *                                      <li>JSON response does not have expected format (JSON syntax problem)</li>
     *                                      <li>JSON response is not an array</li>
     *                                      </ul>
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        JSONArray arrivaldata = new JSONArray(jsonResponse);
        int x = 0;
        for (int index = 0; index < arrivaldata.length(); index++) {
            try {
                JSONObject arrival = arrivaldata.getJSONObject(index);
                String RouteNo = arrival.getString("RouteNo");
                Route tempr = RouteManager.getInstance().getRouteWithNumber(RouteNo);
                JSONArray schedules = arrival.getJSONArray("Schedules");
                for (int index0 = 0; index0 < schedules.length(); index0++) {
                    JSONObject schedule = schedules.getJSONObject(index0);
                    Integer ExpectedCountdown = schedule.getInt("ExpectedCountdown");
                    String ScheduleStatus = schedule.getString("ScheduleStatus");
                    String Destination = schedule.getString("Destination");
                    Arrival tempa = new Arrival(ExpectedCountdown, Destination, tempr);
                    tempa.setStatus(ScheduleStatus);
                    stop.addArrival(tempa);
                    x++;
                }
            } catch (JSONException e) {
                throw new JSONException("JSON Exception");
            }
            if (x == 0)
                throw new ArrivalsDataMissingException("Arrivals Data Missing Exception");
        }
    }
}