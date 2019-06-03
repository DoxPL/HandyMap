package pl.dominikgaloch.pracalicencjacka.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import pl.dominikgaloch.pracalicencjacka.data.models.Photo;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photo WHERE placeId=:locationID")
    LiveData<List<Photo>> getPhotosForLocation(int locationID);

    @Insert
    public void insertPhoto(Photo photo);

    @Delete
    public void deletePhoto(Photo photo);

    @Query("DELETE FROM photo")
    void deleteAllPhotos();

    @Query("SELECT COUNT(*) FROM photo")
    int getCount();

}
