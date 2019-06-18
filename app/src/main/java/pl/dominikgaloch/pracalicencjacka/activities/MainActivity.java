package pl.dominikgaloch.pracalicencjacka.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.osmdroid.util.GeoPoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.fragments.GalleryFragment;
import pl.dominikgaloch.pracalicencjacka.fragments.LocationListFragment;
import pl.dominikgaloch.pracalicencjacka.fragments.MapFragment;
import pl.dominikgaloch.pracalicencjacka.fragments.NearbyPlacesFragment;
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
    private SharedPreferences preferences;
    private boolean prefCenterStatus;
    public static long GPS_UPDATE_INTERVAL = 2;
    public static int MIN_DISTANCE_TO_GPS_UPDATE = 1;
    public static final int REQUEST_PERMISSIONS = 1001;

    @SuppressLint("MissingPermission")
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

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!checkPermissions()) {
            allowPermissions();
        }

        loadPreferences();

        fragmentUtilities.switchFragment(new MapFragment());

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                if (locationChangedCallback != null) {
                    GeoPoint currentPosition = new GeoPoint(location.getLatitude(), location.getLongitude());
                    locationChangedCallback.onChange(currentPosition, prefCenterStatus);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                if (locationChangedCallback != null)
                    locationChangedCallback.onProviderStatusChanged(true);
            }

            @Override
            public void onProviderDisabled(String provider) {
                if (locationChangedCallback != null)
                    locationChangedCallback.onProviderStatusChanged(false);
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                GPS_UPDATE_INTERVAL * 1000,
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
                    if (grantResults[3] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, getString(R.string.perm_camera), Toast.LENGTH_LONG).show();
                    }
                }
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

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void allowPermissions() {
        final String[] permissionsArray = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        requestPermissions(permissionsArray, REQUEST_PERMISSIONS);
    }

    public void loadPreferences() {
        String defaultIntervalStr = String.valueOf(GPS_UPDATE_INTERVAL);
        String defaultDistanceStr = String.valueOf(MIN_DISTANCE_TO_GPS_UPDATE);
        try {
            GPS_UPDATE_INTERVAL = Long.parseLong(preferences.getString("location_refresh_time", defaultIntervalStr));
            MIN_DISTANCE_TO_GPS_UPDATE = Integer.parseInt(preferences.getString("location_refresh_distance", defaultDistanceStr));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        prefCenterStatus = preferences.getBoolean("center_user_position", false);
    }

    public void setLocationListener(LocationChangedListener listener) {
        this.locationChangedCallback = listener;
    }
}
