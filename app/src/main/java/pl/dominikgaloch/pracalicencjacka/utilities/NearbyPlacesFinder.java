package pl.dominikgaloch.pracalicencjacka.utilities;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import pl.dominikgaloch.pracalicencjacka.models.Location;

public class NearbyPlacesFinder {
    private ArrayList<Location> locationList;
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
