package pl.dominikgaloch.pracalicencjacka.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.models.LocationIndexName;
import pl.dominikgaloch.pracalicencjacka.data.models.NearbyPlace;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location")
    LiveData<List<Location>> getAllLocations();

    @Query("SELECT * FROM location WHERE category_id = :categoryID")
    LiveData<List<Location>> getAllLocations(int categoryID);

    @Query("SELECT id, location_name FROM location")
    LiveData<List<LocationIndexName>> getAllLocationsIndexName();

    @Query("SELECT * FROM location WHERE location_name LIKE :pattern LIMIT 1")
    LiveData<Location> getLocationByPattern(String pattern);

    @Query("SELECT * FROM location WHERE location_name LIKE :pattern")
    LiveData<List<Location>> getAllLocationsByPattern(String pattern);

    @Insert
    void insertLocation(Location location);

    @Delete
    void deleteLocation(Location location);

    @Query("DELETE FROM location")
    void deleteAllLocations();

    @Query("SELECT COUNT(*) FROM location")
    int getCount();

}
