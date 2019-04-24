package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import pl.dominikgaloch.pracalicencjacka.models.Location;
import pl.dominikgaloch.pracalicencjacka.repository.LocationRepository;

public class NearbyPlacesFinder {
    private ArrayList<Location> locationList;
    private Context context;
    private GeoPoint currentLocation;

    public NearbyPlacesFinder(ArrayList<Location> locationsList)
    {
        this.locationList = locationsList;
    }

    public boolean findNearbyPlaces(GeoPoint point)
    {
        for(Location location : locationList)
        {
            if(point.distanceToAsDouble(new GeoPoint(location.getLatitude(), location.getLongitude())) < 100)
            {

            }
        }
        return true;
    }

    public ArrayList<Location> getLocationList() {
        return locationList;
    }
}
