package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.interfaces.NearbyPlacesListener;

public class NearbyPlacesFinder {
    private ArrayList<Location> locationList;
    private Context context;
    private GeoPoint currentLocation;
    private NearbyPlacesListener listener;
    private double radius;

    public NearbyPlacesFinder(String radius) {
        this.radius = Float.parseFloat(radius);
        System.out.println(radius);
    }

    public void findNearbyPlaces(GeoPoint point) {
        Map<String, Double> nearbyPlacesMap = new HashMap<>();
        for (Location location : locationList) {
            double distance = point.distanceToAsDouble(location.getGeoPoint());
            if (distance <= radius) {
                nearbyPlacesMap.put(location.getName(), distance);
            }
        }
        listener.onCheckedNearbyLocations(new TreeMap<>(nearbyPlacesMap));
    }

    public void setList(ArrayList<Location> list) {
        this.locationList = list;
    }

    public void setListener(NearbyPlacesListener listener) {
        this.listener = listener;
    }

}
