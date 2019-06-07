package pl.dominikgaloch.pracalicencjacka;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;
import pl.dominikgaloch.pracalicencjacka.fragments.MapFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("pl.dominikgaloch.pracalicencjacka", appContext.getPackageName());
    }
}
