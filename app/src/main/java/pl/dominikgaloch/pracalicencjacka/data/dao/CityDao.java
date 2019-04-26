package pl.dominikgaloch.pracalicencjacka.data.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import pl.dominikgaloch.pracalicencjacka.data.models.City;

@Dao
public interface CityDao {
    @Query("SELECT * FROM city")
    List<City> getAllCities();
}
