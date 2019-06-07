package pl.dominikgaloch.pracalicencjacka.DaoTests;

import android.content.Context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.data.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.data.dao.PhotoDao;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.models.Photo;

@Config(manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class PhotoDaoTest {
    private ApplicationDatabase database;
    private PhotoDao photoDao;

    @Before
    public void init() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase.class).
                allowMainThreadQueries().
                build();
        photoDao = database.photoDao();
        database.categoryDao().insertCategory(new Category("Test1"));
        database.locationDao().insertLocation(new Location("TestName1", "TestDescription1",
                55.011206, 18.120005, 1, 1));
    }

    @Test
    public void insertPhotoTest() {
        photoDao.insertPhoto(new Photo("root/sdcard/test1", 1));
        photoDao.insertPhoto(new Photo("root/sdcard/test2", 1));
        Assert.assertEquals(2, photoDao.getCount());
    }

    @Test
    public void deleteAllCategoriesTest() {
        photoDao.deleteAllPhotos();
        Assert.assertEquals(0, photoDao.getCount());
    }

    @After
    public void closeDatabase() {
        database.close();
    }
}
