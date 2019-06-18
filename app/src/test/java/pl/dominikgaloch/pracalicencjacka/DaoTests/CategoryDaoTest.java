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
import pl.dominikgaloch.pracalicencjacka.data.dao.CategoryDao;
import pl.dominikgaloch.pracalicencjacka.data.model.Category;

@Config(manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CategoryDaoTest {
    private ApplicationDatabase database;
    private CategoryDao categoryDao;

    @Before
    public void init() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase.class).
                allowMainThreadQueries().
                build();
        categoryDao = database.categoryDao();
    }

    @Test
    public void insertCategoryTest() {
        categoryDao.insertCategory(new Category("Test1"));
        categoryDao.insertCategory(new Category("Test2"));
        Assert.assertEquals(2, categoryDao.getCount());
    }

    @Test
    public void deleteAllCategoriesTest() {
        categoryDao.deleteAllCategories();
        Assert.assertEquals(0, categoryDao.getCount());
    }

    @After
    public void closeDatabase() {
        database.close();
    }
}
