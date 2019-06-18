package pl.dominikgaloch.pracalicencjacka.ActivityTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import pl.dominikgaloch.pracalicencjacka.activities.ImageViewActivity;

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