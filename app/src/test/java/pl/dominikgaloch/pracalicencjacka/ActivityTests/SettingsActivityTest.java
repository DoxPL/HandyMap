package pl.dominikgaloch.pracalicencjacka.ActivityTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import pl.dominikgaloch.pracalicencjacka.activities.ImageViewActivity;
import pl.dominikgaloch.pracalicencjacka.activities.SettingsActivity;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 21)
public class SettingsActivityTest {
    private SettingsActivity settingsActivity;

    @Before
    public void init() {
        settingsActivity = Robolectric.buildActivity(SettingsActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void checkIfActivityIsNotNull() {
        Assert.assertNotNull(settingsActivity);
    }
}