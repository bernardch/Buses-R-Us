package ca.ubc.cs.cpsc210.translink.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import ca.ubc.cs.cpsc210.translink.BusesAreUs;
import ca.ubc.cs.cpsc210.translink.R;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.util.Geometry;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// A plotter for bus stop locations
public class BusStopPlotter extends MapViewOverlay {
    /** clusterer */
    private RadiusMarkerClusterer stopClusterer;
    /** maps each stop to corresponding marker on map */
    private Map<Stop, Marker> stopMarkerMap = new HashMap<>();
    /** marker for stop that is nearest to user (null if no such stop) */
    private Marker nearestStnMarker;
    private Activity activity;
    private StopInfoWindow stopInfoWindow;
    /**
     * Constructor
     * @param activity  the application context
     * @param mapView  the map view on which buses are to be plotted
     */
    public BusStopPlotter(Activity activity, MapView mapView) {
        super(activity.getApplicationContext(), mapView);
        this.activity = activity;
        nearestStnMarker = null;
        stopInfoWindow = new StopInfoWindow((StopSelectionListener) activity, mapView);
        newStopClusterer();
    }

    public RadiusMarkerClusterer getStopClusterer() {
        return stopClusterer;
    }

    /**
     * Mark all visible stops in stop manager onto map.
     */
    public void markStops(Location currentLocation) {
        Drawable stopIconDrawable = activity.getResources().getDrawable(R.drawable.stop_icon);
        updateVisibleArea();
        newStopClusterer();
        for (Stop tempStop : StopManager.getInstance()) {
            if (Geometry.rectangleContainsPoint(northWest, southEast, tempStop.getLocn())) {
                if (getMarker(tempStop) == null) {
                    LatLon tempLocn = new LatLon(tempStop.getLocn().getLatitude(), tempStop.getLocn().getLongitude());
                    String markerTitle = tempStop.getNumber() + " " + tempStop.getName();
                    for (Route route : tempStop.getRoutes()) {
                        markerTitle = markerTitle + "\n" + route.getNumber();
                    }
                    Marker tempMarker = new Marker(mapView);
                    tempMarker.setPosition(Geometry.gpFromLL(tempLocn));
                    tempMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                    tempMarker.setTitle(markerTitle);
                    setMarker(tempStop, tempMarker);
                    tempMarker.setRelatedObject(tempStop);
                    tempMarker.setInfoWindow(stopInfoWindow);
                    tempMarker.setIcon(stopIconDrawable);
                    stopClusterer.add(tempMarker);
                } else {
                    setMarker(tempStop, getMarker(tempStop));
                    stopClusterer.add(getMarker(tempStop));
                }
            }
        }
        stopClusterer.invalidate();
    }

    /**
     * Create a new stop cluster object used to group stops that are close by to reduce screen clutter
     */
    private void newStopClusterer() {
        stopClusterer = new RadiusMarkerClusterer(activity);
        stopClusterer.getTextPaint().setTextSize(20.0F * BusesAreUs.dpiFactor());
        int zoom =  mapView == null ? 16 : mapView.getZoomLevel();
        if (zoom == 0) zoom = MapDisplayFragment.DEFAULT_ZOOM;
        int radius = 1000 / zoom;
        stopClusterer.setRadius(radius);
        Drawable clusterIconD = activity.getResources().getDrawable(R.drawable.stop_cluster);
        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
        stopClusterer.setIcon(clusterIcon);
    }

    /**
     * Update marker of nearest stop (called when user's location has changed).  If nearest is null,
     * no stop is marked as the nearest stop.
     *
     * @param nearest   stop nearest to user's location (null if no stop within StopManager.RADIUS metres)
     */
    public void updateMarkerOfNearest(Stop nearest) {
        Drawable stopIconDrawable = activity.getResources().getDrawable(R.drawable.stop_icon);
        Drawable closestStopIconDrawable = activity.getResources().getDrawable(R.drawable.closest_stop_icon);
        if (nearest == null) {
            if (nearestStnMarker != null)
                nearestStnMarker.setIcon(stopIconDrawable);
        } else {
            if (nearest != null) {
                if (getMarker(nearest) != null) {
                    if (nearestStnMarker == null) {
                        nearestStnMarker = getMarker(nearest);
                        nearestStnMarker.setIcon(closestStopIconDrawable);
                    } else {
                        nearestStnMarker.setIcon(stopIconDrawable);
                        nearestStnMarker = getMarker(nearest);
                        nearestStnMarker.setIcon(closestStopIconDrawable);
                    }
                } else if (getMarker(nearest) == null) {
                    if (nearestStnMarker == null) {
                        LatLon tempLocn = new LatLon(nearest.getLocn().getLatitude(), nearest.getLocn().getLongitude());
                        String markerTitle = nearest.getNumber() + " " + nearest.getName();
                        for (Route route : nearest.getRoutes()) {
                            markerTitle = markerTitle + "\n" + route.getNumber();
                        }
                        Marker tempMarker = new Marker(mapView);
                        tempMarker.setPosition(Geometry.gpFromLL(tempLocn));
                        tempMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                        tempMarker.setTitle(markerTitle);
                        setMarker(nearest, tempMarker);
                        tempMarker.setRelatedObject(nearest);
                        tempMarker.setInfoWindow(stopInfoWindow);
                        tempMarker.setIcon(closestStopIconDrawable);
                        stopClusterer.add(tempMarker);
                    } else {
                        nearestStnMarker.setIcon(stopIconDrawable);
                        LatLon tempLocn = new LatLon(nearest.getLocn().getLatitude(), nearest.getLocn().getLongitude());
                        String markerTitle = nearest.getNumber() + " " + nearest.getName();
                        Marker tempMarker = new Marker(mapView);
                        tempMarker.setPosition(Geometry.gpFromLL(tempLocn));
                        tempMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                        for (Route route : nearest.getRoutes()) {
                            markerTitle = markerTitle + "\n" + route.getNumber();
                        }
                        tempMarker.setTitle(markerTitle);
                        setMarker(nearest, tempMarker);
                        tempMarker.setRelatedObject(nearest);
                        tempMarker.setInfoWindow(stopInfoWindow);
                        tempMarker.setIcon(closestStopIconDrawable);
                        stopClusterer.add(tempMarker);
                    }
                }
            }
        }
        stopClusterer.invalidate();
    }

    /**
     * Manage mapping from stops to markers using a map from stops to markers.
     * The mapping in the other direction is done using the Marker.setRelatedObject() and
     * Marker.getRelatedObject() methods.
     */
    private Marker getMarker(Stop stop) { return stopMarkerMap.get(stop); } // Returns a Marker
    private void setMarker(Stop stop, Marker marker) { stopMarkerMap.put(stop, marker); } // Sets the Marker
    private void clearMarker(Stop stop) { stopMarkerMap.remove(stop); }
    private void clearMarkers() { stopMarkerMap.clear(); }
}
