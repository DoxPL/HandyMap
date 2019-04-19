package pl.dominikgaloch.pracalicencjacka.repository;

import android.content.Context;

import java.util.List;

import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.models.Photo;

public class PhotoRepository {
    private static ApplicationDatabase DATABASE_INSTANCE;

    public PhotoRepository(Context context) {
        DATABASE_INSTANCE = Room.databaseBuilder(context, ApplicationDatabase.class,
                context.getString(R.string.database_name)).allowMainThreadQueries().build();
    }

    public List<Photo> getAllPhotos(int locationID) {
        return DATABASE_INSTANCE.photoDao().getPhotosForLocation(locationID);
    }

    public void deletePhoto(Photo photo)
    {
        DATABASE_INSTANCE.photoDao().deletePhoto(photo);
    }

    public void insertPhoto(Photo photo) {
        DATABASE_INSTANCE.photoDao().insertPhoto(photo);
    }
}
