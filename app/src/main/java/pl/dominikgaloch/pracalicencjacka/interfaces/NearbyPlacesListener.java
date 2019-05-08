package pl.dominikgaloch.pracalicencjacka.interfaces;

import java.util.LinkedHashMap;

public interface NearbyPlacesListener {
    void onCheckedNearbyLocations(LinkedHashMap<String, Double> nearbyPlaces);
}
