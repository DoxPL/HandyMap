package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
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

    public boolean findNearbyPlaces(GeoPoint point)
    {
        LinkedHashMap<String, Double> nearbyPlacesList = new LinkedHashMap<>();
        for(Location location : locationList)
        {
            double distance = point.distanceToAsDouble(location.getGeoPoint());
            if(distance <= radius)
            {
                nearbyPlacesList.put(location.getName(), distance);
            }
        }
        listener.onCheckedNearbyLocations(nearbyPlacesList);
        return true;
    }

    public void setList(ArrayList<Location> list) {
        this.locationList = list;
    }

    public void setListener(NearbyPlacesListener listener) {
        this.listener = listener;
    }

    public double getRadius() {
        return radius;
    }

}
