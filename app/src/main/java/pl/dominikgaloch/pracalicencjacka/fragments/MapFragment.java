package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
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

public class MapFragment extends Fragment {

    private MapView mvOsmView;
    private ApplicationDatabase database;
    private ArrayList<OverlayItem> locationList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapEventsOverlay mapEventsOverlay;
    private MapEventsReceiver eventsReceiver;
    private ItemizedOverlayWithFocus<OverlayItem> mapOverlay;
    private Marker userLocationPin;
    private static final long GPS_UPDATE_INTERVAL = 2000;
    private static final int MIN_DISTANCE_TO_GPS_UPDATE = 1;


    public MapFragment() {
    }

    public MapFragment(double latitude, double longitude)
    {
        //this.coordinates = new LatLng(latitude, longitude);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mvOsmView = view.findViewById(R.id.mvOsmDroid);
        mvOsmView.setTileSource(TileSourceFactory.MAPNIK);
        mvOsmView.setMultiTouchControls(true);

        database = Room.databaseBuilder(getContext(), ApplicationDatabase.class,
                getString(R.string.database_name)).allowMainThreadQueries().build();

        locationList = new ArrayList<>();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        eventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(final GeoPoint p) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.place_form_dialog, null);
                final EditText etName = dialogView.findViewById(R.id.tName);
                final EditText etDescription = dialogView.findViewById(R.id.tDescription);
                final EditText etLatitude = dialogView.findViewById(R.id.etLat);
                final EditText etLongitude = dialogView.findViewById(R.id.etLng);
                Button btnSave = dialogView.findViewById(R.id.btnSave);
                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString();
                        String description = etDescription.getText().toString();
                        database.locationDao().insertAllLocations(new Location(name,
                                description, p.getLatitude(), p.getLongitude()));
                        putMarker(mvOsmView, p, false);
                        dialog.dismiss();

                    }
                });

                dialog.show();
                return false;
            }
        };

        mapEventsOverlay = new MapEventsOverlay(eventsReceiver);
        mvOsmView.getOverlays().add(mapEventsOverlay);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                putMarker(mvOsmView, new GeoPoint(location.getLatitude(), location.getLongitude()),
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

    private void putMarker(MapView map, GeoPoint position, boolean removeLastUserLocation)
    {
        if(removeLastUserLocation && userLocationPin != null)
            mvOsmView.getOverlays().remove(userLocationPin);
        Marker pin = new Marker(map);
        pin.setPosition(position);
        pin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(pin);
    }

}
