package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import pl.dominikgaloch.pracalicencjacka.data.models.Location;

public class NearbyPlacesFinder {
    private ArrayList<Location> locationList;
    private Context context;
    private GeoPoint currentLocation;

    public NearbyPlacesFinder(Context context, ArrayList<Location> locationsList)
    {
        this.context = context;
        this.locationList = locationsList;
    }

    public boolean findNearbyPlaces(GeoPoint point)
    {
        for(Location location : locationList)
        {
            double distance = point.distanceToAsDouble(location.getGeoPoint());
            if(distance < 100)
            {
                Toast.makeText(context, location.getName() + " - " + distance + " m", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

}
