package pl.dominikgaloch.pracalicencjacka.interfaces;

import org.osmdroid.views.overlay.Marker;

import pl.dominikgaloch.pracalicencjacka.data.models.Location;

public interface LocationSavedCallback {
    public void onDialogSuccess(Location locationToInsert);
}
