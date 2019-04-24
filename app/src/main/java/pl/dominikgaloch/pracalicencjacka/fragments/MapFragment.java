package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.FormDialog;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.models.Location;
import pl.dominikgaloch.pracalicencjacka.repository.LocationRepository;

public class MapFragment extends Fragment {

    private MapView mvOsmView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapEventsOverlay mapEventsOverlay;
    private MapEventsReceiver eventsReceiver;
    private Marker userLocationPin;
    private Location receivedLocation;
    private Context context;
    private SharedPreferences preferences;
    private static final long GPS_UPDATE_INTERVAL = 2000;
    private static final int MIN_DISTANCE_TO_GPS_UPDATE = 1;
    private static final int USER_PIN_COLOR = 0;

    public MapFragment() {

    }

    public MapFragment(Location receivedLocation) {
        this.receivedLocation = receivedLocation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);
        preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        context = getContext();
        mvOsmView = view.findViewById(R.id.mvOsmDroid);
        mapInit();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        eventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                createForm(p);
                return true;
            }
        };

        mapEventsOverlay = new MapEventsOverlay(eventsReceiver);
        mvOsmView.getOverlays().add(mapEventsOverlay);

        addLocationsFromDatabase();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {

                putMarker(new GeoPoint(location.getLatitude(), location.getLongitude()),
                        getString(R.string.current_user_position), USER_PIN_COLOR,
                        true);
                mvOsmView.invalidate();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                GPS_UPDATE_INTERVAL,
                MIN_DISTANCE_TO_GPS_UPDATE,
                locationListener
        );

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveLastChanges();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search_item);
        searchItem.setVisible(true);
        SearchView searchWidget = (SearchView) searchItem.getActionView();
        searchWidget.setQueryHint(getString(R.string.place_name));
        searchWidget.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    Location location = new LocationRepository(context).getLocationByPattern(newText);
                    if (location != null) {
                        mvOsmView.getController().animateTo(new GeoPoint(location.getLatitude(), location.getLongitude()));
                    }
                }
                return true;
            }
        });
    }

    private void putMarker(GeoPoint position, String name, int color, boolean changeUserPosition) {
        Marker pin = new Marker(mvOsmView);
        pin.setPosition(position);
        pin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        pin.setIcon(getPinDrawable(color));
        pin.setTitle(name);
        if (changeUserPosition) {
            if (userLocationPin != null)
                mvOsmView.getOverlays().remove(userLocationPin);
            userLocationPin = pin;
        }
        mvOsmView.getOverlays().add(pin);
    }

    private Drawable getPinDrawable(int type) {
        int pin = 0;
        switch (type) {
            case 0:
                pin = R.drawable.marker_blue;
                break;
            case 1:
                pin = R.drawable.marker_green;
                break;
            case 2:
                pin = R.drawable.marker_orange;
                break;
            case 3:
                pin = R.drawable.marker_yellow;
                break;
            case 4:
                pin = R.drawable.marker_brown;
                break;
            case 5:
                pin = R.drawable.marker_purple;
                break;
            case 6:
                pin = R.drawable.marker_red;
                break;
            default:
                pin = R.drawable.person;
                break;
        }
        return ContextCompat.getDrawable(context, pin);
    }

    private void createForm(final GeoPoint point) {
        //Todo move
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.place_form_dialog, null);
        final EditText etName = dialogView.findViewById(R.id.tName);
        final EditText etDescription = dialogView.findViewById(R.id.tDescription);
        final Spinner spPinColor = dialogView.findViewById(R.id.spPinColor);
        final LinearLayout manualCoordinatesForm = dialogView.findViewById(R.id.manualCoordsForm);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Switch swInputType = dialogView.findViewById(R.id.swAutoManual);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();


        swInputType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    manualCoordinatesForm.setVisibility(View.VISIBLE);
                } else {
                    manualCoordinatesForm.setVisibility(View.GONE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                int color = spPinColor.getSelectedItemPosition();
                Location locationToInsert = new Location(name,
                        description, point.getLatitude(), point.getLongitude(), color);
                putMarker(point, locationToInsert.getName(), locationToInsert.getMarkerColor(), false);
                new LocationRepository(context).insertLocation(locationToInsert);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void addLocationsFromDatabase() {
        LocationRepository locationRepository = new LocationRepository(context);
        for (Location location : locationRepository.getAllLocations()) {
            putMarker(location.getGeoPoint(), location.getName(), location.getMarkerColor(), false);
        }
    }

    private void mapInit() {
        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mvOsmView.setTileSource(TileSourceFactory.MAPNIK);
        mvOsmView.setMultiTouchControls(true);
        mvOsmView.getController().setZoom(getLastZoomLevel());
        if (receivedLocation != null) {
            putMarker(receivedLocation.getGeoPoint(), receivedLocation.getName(), receivedLocation.getMarkerColor(), false);
            mvOsmView.invalidate();
            mvOsmView.getController().setCenter(receivedLocation.getGeoPoint());
        } else {
            mvOsmView.getController().setCenter(getLastLocation());
        }
    }

    private void saveLastChanges() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("lastLocationLatitude", (float) mvOsmView.getMapCenter().getLatitude());
        editor.putFloat("lastLocationLongitude", (float) mvOsmView.getMapCenter().getLongitude());
        editor.putFloat("lastZoomLevel", (float) mvOsmView.getZoomLevelDouble());
        editor.commit();
    }

    private GeoPoint getLastLocation() {
        double latitude = preferences.getFloat("lastLocationLatitude", 0);
        double longitude = preferences.getFloat("lastLocationLongitude", 0);
        return new GeoPoint(latitude, longitude);
    }

    private double getLastZoomLevel() {
        return preferences.getFloat("lastZoomLevel", (float) 10d);
    }

}
