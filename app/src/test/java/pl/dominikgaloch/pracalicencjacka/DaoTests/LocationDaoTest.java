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
import pl.dominikgaloch.pracalicencjacka.data.model.Category;
import pl.dominikgaloch.pracalicencjacka.data.model.Location;

@Config(manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class LocationDaoTest {
    private ApplicationDatabase database;
    private LocationDao locationDao;

    @Before
    public void init() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase.class).
                allowMainThreadQueries().
                build();
        locationDao = database.locationDao();
        database.categoryDao().insertCategory(new Category("Test1"));
    }

    @Test
    public void insertLocationTest() {
        locationDao.insertLocation(new Location("TestName1", "TestDescription1",
                55.011206, 18.120005, 1, 1));
        locationDao.insertLocation(new Location("TestName2", "TestDescription2",
                54.999261, 18.078768, 2, 1));
        Assert.assertEquals(2, locationDao.getCount());
    }

    @Test
    public void deleteAllLocations() {
        locationDao.deleteAllLocations();
        Assert.assertEquals(0, locationDao.getCount());
    }

    @After
    public void closeDatabase() {
        database.close();
    }
}

