package pl.dominikgaloch.pracalicencjacka.ActivityTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import pl.dominikgaloch.pracalicencjacka.activities.ImageViewActivity;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 21)
public class MainActivityTest {
    private MainActivity mainActivity;

    @Before
    public void init() {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void checkIfActivityIsNotNull() {
        Assert.assertNotNull(mainActivity);
    }
}