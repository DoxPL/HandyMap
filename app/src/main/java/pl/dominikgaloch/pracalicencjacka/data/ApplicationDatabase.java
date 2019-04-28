package pl.dominikgaloch.pracalicencjacka.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.data.dao.PhotoDao;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.models.Photo;

@Database(entities = {Location.class, Photo.class}, version = 2)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract LocationDao locationDao();
    public abstract PhotoDao photoDao();

    public static synchronized ApplicationDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, ApplicationDatabase.class,
                context.getString(R.string.database_name)).
                fallbackToDestructiveMigration().
                build();
    }
}
