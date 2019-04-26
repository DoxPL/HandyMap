package pl.dominikgaloch.pracalicencjacka.data.repository;

import android.content.Context;

import org.osmdroid.util.GeoPoint;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.models.NearbyPlace;

public class LocationRepository {
    private static ApplicationDatabase DATABASE_INSTANCE;

    public LocationRepository(Context context) {
        DATABASE_INSTANCE = Room.databaseBuilder(context, ApplicationDatabase.class,
                context.getString(R.string.database_name)).fallbackToDestructiveMigration().
                allowMainThreadQueries().build();
    }

    public LiveData<List<Location>> getAllLocations() {
        return DATABASE_INSTANCE.locationDao().getAllLocations();
    }

    public List<NearbyPlace> getNearbyPlaces(GeoPoint point) {
        return DATABASE_INSTANCE.locationDao().getNearbyPlaces(point.getLatitude(), point.getLongitude());
    }

    public List<String> getAllLocationNames() {
        return DATABASE_INSTANCE.locationDao().getAllLocationNames();
    }

    public Location getLocationByPattern(String pattern) {
        pattern += "%";
        return DATABASE_INSTANCE.locationDao().getLocationByPattern(pattern);
    }

    public void insertLocation(Location location) {
        DATABASE_INSTANCE.locationDao().insertLocation(location);
    }

    public void deleteLocation(Location location) {
        DATABASE_INSTANCE.locationDao().delete(location);
    }
}
