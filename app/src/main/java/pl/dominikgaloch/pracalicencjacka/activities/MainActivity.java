package pl.dominikgaloch.pracalicencjacka.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import androidx.fragment.app.FragmentTransaction;
import pl.dominikgaloch.pracalicencjacka.fragments.NearbyPlacesFragment;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.fragments.GalleryFragment;
import pl.dominikgaloch.pracalicencjacka.fragments.LocationListFragment;
import pl.dominikgaloch.pracalicencjacka.fragments.MapFragment;
import pl.dominikgaloch.pracalicencjacka.fragments.QRScannerFragment;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationChangedListener;
import pl.dominikgaloch.pracalicencjacka.utilities.FragmentUtilities;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private LocationChangedListener locationChangedCallback;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Toolbar toolbar;
    private FragmentUtilities fragmentUtilities;
    public static final long GPS_UPDATE_INTERVAL = 2000;
    public static final int MIN_DISTANCE_TO_GPS_UPDATE = 1;
    public static final int REQUEST_PERMISSIONS = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentUtilities = new FragmentUtilities(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (!checkPermissions()) {
            allowPermissions();
        }

        fragmentUtilities.switchFragment(new MapFragment());

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                GeoPoint currentPosition = new GeoPoint(location.getLatitude(), location.getLongitude());
                locationChangedCallback.onChange(currentPosition);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                locationChangedCallback.onProviderStatusChanged(true);
            }

            @Override
            public void onProviderDisabled(String provider) {
                locationChangedCallback.onProviderStatusChanged(false);
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                GPS_UPDATE_INTERVAL,
                MIN_DISTANCE_TO_GPS_UPDATE,
                locationListener
        );

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment currentFragment = null;
        switch (id) {
            case R.id.nav_map:
                currentFragment = new MapFragment();
                break;
            case R.id.nav_list:
                currentFragment = new LocationListFragment();
                break;
            case R.id.nav_gallery:
                currentFragment = new GalleryFragment();
                break;
            case R.id.nav_nearby_places:
                currentFragment = new NearbyPlacesFragment();
                break;
            case R.id.nav_qrscanner:
                currentFragment = new QRScannerFragment();
                break;
        }
        fragmentUtilities.switchFragment(currentFragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(getApplicationContext(), "NFC found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                            && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, getString(R.string.perm_gps), Toast.LENGTH_LONG).show();
                    }
                    if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, getString(R.string.perm_storage), Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

    public void switchFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void allowPermissions() {
        final String[] permissionsArray = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissionsArray, REQUEST_PERMISSIONS);
    }

    public void setLocationListener(LocationChangedListener listener) {
        this.locationChangedCallback = listener;
    }
}
