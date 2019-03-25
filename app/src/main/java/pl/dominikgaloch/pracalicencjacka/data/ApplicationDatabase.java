package pl.dominikgaloch.pracalicencjacka.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import pl.dominikgaloch.pracalicencjacka.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.models.Location;

@Database(entities = {Location.class}, version = 1)
public abstract class ApplicationDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}
