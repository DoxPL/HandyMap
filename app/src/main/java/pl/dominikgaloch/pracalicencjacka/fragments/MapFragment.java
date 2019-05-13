package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.FormDialog;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.CategoryViewModel;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationChangedListener;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationSavedCallback;
import pl.dominikgaloch.pracalicencjacka.utilities.NearbyPlacesFinder;

public class MapFragment extends Fragment implements LocationChangedListener {

    private MapView mvOsmView;
    private MapEventsOverlay mapEventsOverlay;
    private MapEventsReceiver eventsReceiver;
    private Marker userLocationPin;
    private Location receivedLocation;
    private Context context;
    private SharedPreferences preferences;
    private LocationViewModel locationViewModel;
    private CategoryViewModel categoryViewModel;
    private static final int USER_PIN_COLOR = -1;
    private static final float DEFAULT_LATITUDE = 0;
    private static final float DEFAULT_LONGITUDE = 0;
    private static final float DEFAULT_ZOOM_LEVEL = 10f;

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
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        mvOsmView = view.findViewById(R.id.mvOsmDroid);
        mapInit();

        eventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                FormDialog formDialog = new FormDialog(getActivity(), context, p);
                formDialog.create(new LocationSavedCallback() {
                    @Override
                    public void onDialogSuccess(Location locationToInsert) {
                        locationViewModel.insert(locationToInsert);
                        putMarker(locationToInsert);
                    }
                });
                return true;
            }
        };

        mapEventsOverlay = new MapEventsOverlay(eventsReceiver);
        mvOsmView.getOverlays().add(mapEventsOverlay);

        addLocationsFromDatabase();

        ((MainActivity) getActivity()).setLocationListener(this);

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
                    locationViewModel.getLocationByPattern(newText).observe(MapFragment.this, new Observer<Location>() {
                        @Override
                        public void onChanged(Location location) {
                            if (location != null) {
                                mvOsmView.getController().animateTo(new GeoPoint(location.getLatitude(), location.getLongitude()));
                            }
                        }
                    });
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

    private void putMarker(Location location) {
        Marker pin = new Marker(mvOsmView);
        pin.setPosition(location.getGeoPoint());
        pin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        pin.setIcon(getPinDrawable(location.getMarkerColor()));
        pin.setTitle(location.getName());
        mvOsmView.getOverlays().add(pin);
    }

    public Drawable getPinDrawable(int type) {
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

    private void addLocationsFromDatabase() {
        locationViewModel.getAllLocation().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                for (Location location : locations) {
                    putMarker(location.getGeoPoint(), location.getName(), location.getMarkerColor(), false);
                }
            }
        });
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
        double latitude = preferences.getFloat("lastLocationLatitude", DEFAULT_LATITUDE);
        double longitude = preferences.getFloat("lastLocationLongitude", DEFAULT_LONGITUDE);
        return new GeoPoint(latitude, longitude);
    }

    private double getLastZoomLevel() {
        return preferences.getFloat("lastZoomLevel", DEFAULT_ZOOM_LEVEL);
    }

    //Todo remove later
    private String getNearbyPlacesStr(GeoPoint point) {
        return null;
    }


    @Override
    public void onChange(final GeoPoint currentPosition) {
        putMarker(currentPosition, getString(R.string.current_user_position), USER_PIN_COLOR,
                true);
        mvOsmView.invalidate();
    }

    @Override
    public void onProviderStatusChanged(boolean status) {

    }
}
