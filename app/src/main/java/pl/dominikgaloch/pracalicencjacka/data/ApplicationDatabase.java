package pl.dominikgaloch.pracalicencjacka.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.dao.CategoryDao;
import pl.dominikgaloch.pracalicencjacka.data.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.data.dao.PhotoDao;
import pl.dominikgaloch.pracalicencjacka.data.model.Category;
import pl.dominikgaloch.pracalicencjacka.data.model.Location;
import pl.dominikgaloch.pracalicencjacka.data.model.Photo;
import pl.dominikgaloch.pracalicencjacka.data.repository.CategoryRepository;

@Database(entities = {Location.class, Photo.class, Category.class}, version = 7)
public abstract class ApplicationDatabase extends RoomDatabase {
    private static ApplicationDatabase DATABASE_INSTANCE;
    public abstract LocationDao locationDao();
    public abstract PhotoDao photoDao();
    public abstract CategoryDao categoryDao();
    public static final String DATABASE_NAME = "user_database";

    public static ApplicationDatabase getInstance(final Context context) {
        if (DATABASE_INSTANCE == null) {
            synchronized (ApplicationDatabase.class) {
                DATABASE_INSTANCE = Room.databaseBuilder(context, ApplicationDatabase.class, DATABASE_NAME).
                        fallbackToDestructiveMigration().
                        addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Category defaultCategory = new Category(context.getString(R.string.tab_default));
                                new CategoryRepository(context).insertCategory(defaultCategory);
                            }
                        }).
                        build();
            }
        }
        return DATABASE_INSTANCE;
    }
}
