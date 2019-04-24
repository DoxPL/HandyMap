package pl.dominikgaloch.pracalicencjacka.dao;

import org.osmdroid.util.GeoPoint;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import pl.dominikgaloch.pracalicencjacka.models.Location;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location")
    List<Location> getAllLocations();

    @Query("SELECT location_name FROM location")
    List<String> getAllLocationNames();

    @Query("SELECT (sin((latitude - :lat) / 2) * sin((latitude - :lat) / 2) + cos(:lat) * cos(latitude) * " +
            "sin((longitude - :lng) / 2) * sin((longitude - :lng) / 2)) AS distance FROM location")
    List<Integer> getNearbyPlaces(Float lat, Float lng);

    @Query("SELECT * FROM location WHERE location_name LIKE :pattern LIMIT 1")
    Location getLocationByPattern(String pattern);

    @Insert
    void insertAllLocations(Location... locations);

    @Insert
    void insertLocation(Location location);

    @Delete
    void delete(Location location);

    @Query("UPDATE location SET location_name = :name WHERE id = :id")
    void updateName(String name, int id);

    @Query("UPDATE location SET description = :description WHERE id = :id")
    void updateDescription(String description, int id);

    @Query("UPDATE location SET marker_color = :color WHERE id = :id")
    void updateMarkerColor(int color, int id);

}
