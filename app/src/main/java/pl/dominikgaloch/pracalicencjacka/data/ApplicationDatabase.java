package pl.dominikgaloch.pracalicencjacka.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import pl.dominikgaloch.pracalicencjacka.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.dao.PhotoDao;
import pl.dominikgaloch.pracalicencjacka.models.Location;
import pl.dominikgaloch.pracalicencjacka.models.Photo;

@Database(entities = {Location.class, Photo.class}, version = 2)
public abstract class ApplicationDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract PhotoDao photoDao();
}
