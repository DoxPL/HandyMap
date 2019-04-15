package pl.dominikgaloch.pracalicencjacka.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import pl.dominikgaloch.pracalicencjacka.models.Location;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location")
    List<Location> getAllLocations();

    @Insert
    void insertAllLocations(Location ... locations);

    @Insert
    void insertLocation(Location location);

    @Delete
    void delete(Location location);
}
