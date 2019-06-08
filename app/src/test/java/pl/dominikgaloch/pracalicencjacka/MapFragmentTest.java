package pl.dominikgaloch.pracalicencjacka;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;


import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;
import pl.dominikgaloch.pracalicencjacka.fragments.GalleryFragment;
import pl.dominikgaloch.pracalicencjacka.fragments.MapFragment;

@Config(manifest = "AndroidManifest.xml", sdk=21)
@RunWith(RobolectricTestRunner.class)
public class MapFragmentTest {
    @Test
    public void checkFragmentNotNull() {
        FragmentScenario scenario = FragmentScenario.launchInContainer(MapFragment.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }
}
