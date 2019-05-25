package pl.dominikgaloch.pracalicencjacka.interfaces;

import java.util.TreeMap;

public interface NearbyPlacesListener {
    void onCheckedNearbyLocations(TreeMap<String, Double> nearbyPlacesMap);
}
