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

    @Query("SELECT * FROM location INNER JOIN category ON location.category_id = category.id WHERE category.category_name = :categoryName")
    LiveData<List<Location>> getAllLocationsByCategoryName(String categoryName);

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


    @Query("UPDATE location SET location_name = :name WHERE id = :id")
    void updateName(String name, int id);

}
