package pl.dominikgaloch.pracalicencjacka.interfaces;

import org.osmdroid.util.GeoPoint;

public interface LocationChangedListener {
    void onChange(GeoPoint currentPosition, boolean setAsCenter);
    void onProviderStatusChanged(boolean status);
}
