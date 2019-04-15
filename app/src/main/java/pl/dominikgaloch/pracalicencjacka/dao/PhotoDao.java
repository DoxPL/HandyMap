package pl.dominikgaloch.pracalicencjacka.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import pl.dominikgaloch.pracalicencjacka.models.Photo;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photo WHERE placeId=:locationID")
    List<Photo> getPhotosForLocation(int locationID);

    @Insert
    public void insertPhoto(Photo photo);

    @Delete
    public void deletePhoto(Photo photo);
}
