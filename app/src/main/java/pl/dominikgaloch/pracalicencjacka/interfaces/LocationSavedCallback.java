package pl.dominikgaloch.pracalicencjacka.interfaces;

import pl.dominikgaloch.pracalicencjacka.data.model.Location;

public interface LocationSavedCallback {
    void onDialogSuccess(Location locationToInsert);
}
