package pl.dominikgaloch.pracalicencjacka.interfaces;

import org.osmdroid.util.GeoPoint;

public interface LocationChangedListener {
    public void onChange(GeoPoint currentPosition);
}
