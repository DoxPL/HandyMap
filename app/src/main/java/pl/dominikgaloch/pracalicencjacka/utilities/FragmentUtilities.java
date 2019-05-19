package pl.dominikgaloch.pracalicencjacka.utilities;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import pl.dominikgaloch.pracalicencjacka.R;

public class FragmentUtilities {
    private Context context;
    private FragmentActivity activity;

    public FragmentUtilities(FragmentActivity activity) {
        this.activity = activity;
    }

    public void switchFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void setToolbarTitle(String text) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(text);
    }
}
