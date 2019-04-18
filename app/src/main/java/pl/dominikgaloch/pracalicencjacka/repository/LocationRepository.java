package pl.dominikgaloch.pracalicencjacka.repository;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.List;

import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.models.Location;

public class LocationRepository {
    private static ApplicationDatabase DATABASE_INSTANCE;

    public LocationRepository(Context context) {
        DATABASE_INSTANCE = Room.databaseBuilder(context, ApplicationDatabase.class,
                context.getString(R.string.database_name)).fallbackToDestructiveMigration().
                allowMainThreadQueries().build();
    }

    public List<Location> getAllLocations()
    {
        return DATABASE_INSTANCE.locationDao().getAllLocations();
    }

    public List<String> getAllLocationNames()
    {
        return DATABASE_INSTANCE.locationDao().getAllLocationNames();
    }

    public void insertLocation(Location location)
    {
        DATABASE_INSTANCE.locationDao().insertLocation(location);
    }

    public void deleteLocation(Location location)
    {
        DATABASE_INSTANCE.locationDao().delete(location);
    }
}
