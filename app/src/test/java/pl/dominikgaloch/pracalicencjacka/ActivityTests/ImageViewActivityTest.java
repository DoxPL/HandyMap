package pl.dominikgaloch.pracalicencjacka.ActivityTests;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import androidx.lifecycle.Lifecycle;
import pl.dominikgaloch.pracalicencjacka.activities.ImageViewActivity;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;
import pl.dominikgaloch.pracalicencjacka.activities.SettingsActivity;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 21)
public class ImageViewActivityTest {
    private ImageViewActivity imageViewActivity;

    @Before
    public void init() {
        imageViewActivity = Robolectric.buildActivity(ImageViewActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void checkIfActivityIsNotNull() {
        Assert.assertNotNull(imageViewActivity);
    }
}