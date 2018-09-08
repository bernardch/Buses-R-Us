package ca.ubc.cs.cpsc210.translink.ui;

import android.content.Context;
import ca.ubc.cs.cpsc210.translink.BusesAreUs;
import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.util.Geometry;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// A bus route drawer
public class BusRouteDrawer extends MapViewOverlay {
    /** overlay used to display bus route legend text on a layer above the map */
    private BusRouteLegendOverlay busRouteLegendOverlay;
    /** overlays used to plot bus routes */
    private List<Polyline> busRouteOverlays;

    /**
     * Constructor
     * @param context   the application context
     * @param mapView   the map view
     */
    public BusRouteDrawer(Context context, MapView mapView) {
        super(context, mapView);
        busRouteLegendOverlay = createBusRouteLegendOverlay();
        busRouteOverlays = new ArrayList<>();
    }

    /**
     * Plot each visible segment of each route pattern of each route going through the selected stop.
     */
    public void plotRoutes(int zoomLevel) {
        updateVisibleArea();
        busRouteOverlays.clear();
        busRouteLegendOverlay.clear();
        Stop selectedStop = StopManager.getInstance().getSelected();
        if (selectedStop != null) {
            for (Route tempRoute : selectedStop.getRoutes()) {
                busRouteLegendOverlay.add(tempRoute.getNumber());
                for (RoutePattern selectedRoutePattern : tempRoute.getPatterns()) {
                    List<LatLon> selectedRoutePath = selectedRoutePattern.getPath();
                    for (int x = 0; x < selectedRoutePath.size() - 1; x++) {
                        Polyline tempLine = new Polyline(context);
                        List<GeoPoint> validPath = new ArrayList<>();
                        LatLon a = selectedRoutePath.get(x);
                        LatLon b = selectedRoutePath.get(x+1);
                        GeoPoint A = Geometry.gpFromLL(a);
                        GeoPoint B = Geometry.gpFromLL(b);
                        if (Geometry.rectangleIntersectsLine(northWest, southEast, a, b)) {
                            validPath.add(A);
                            validPath.add(B);
                            tempLine.setPoints(validPath);
                            tempLine.setColor(getBusRouteLegendOverlay().getColor(tempRoute.getNumber()));
                            tempLine.setWidth(getLineWidth(zoomLevel));
                            busRouteOverlays.add(tempLine);
                        }
                    }
                }
            }
        } else {
            busRouteOverlays.clear();
            busRouteLegendOverlay.clear();
        }
    }

    public List<Polyline> getBusRouteOverlays() {
        return Collections.unmodifiableList(busRouteOverlays);
    }

    public BusRouteLegendOverlay getBusRouteLegendOverlay() {
        return busRouteLegendOverlay;
    }


    /**
     * Create text overlay to display bus route colours
     */
    private BusRouteLegendOverlay createBusRouteLegendOverlay() {
        ResourceProxy rp = new DefaultResourceProxyImpl(context);
        return new BusRouteLegendOverlay(rp, BusesAreUs.dpiFactor());
    }

    /**
     * Get width of line used to plot bus route based on zoom level
     * @param zoomLevel   the zoom level of the map
     * @return            width of line used to plot bus route
     */
    private float getLineWidth(int zoomLevel) {
        if(zoomLevel > 14)
            return 7.0f * BusesAreUs.dpiFactor();
        else if(zoomLevel > 10)
            return 5.0f * BusesAreUs.dpiFactor();
        else
            return 2.0f * BusesAreUs.dpiFactor();
    }
}
