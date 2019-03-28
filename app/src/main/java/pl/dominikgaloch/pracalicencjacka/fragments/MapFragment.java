package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.models.Location;

public class MapFragment extends Fragment {
    private MapView mapView;
    private LatLng coordinates;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ApplicationDatabase database;
    private static final long GPS_UPDATE_INTERVAL = 2000;
    private static final int MIN_DISTANCE_TO_GPS_UPDATE = 1;

    public MapFragment() {
    }

    public MapFragment(double latitude, double longitude)
    {
        this.coordinates = new LatLng(latitude, longitude);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //Mapbox.setAccessToken(getResources().getString(R.string.mapbox_token));
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                Toast.makeText(getContext(), location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG)
                        .show();
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
        database = Room.databaseBuilder(getContext(), ApplicationDatabase.class,
                getString(R.string.database_name)).allowMainThreadQueries().build();
        mapView = view.findViewById(R.id.mapBoxView);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                    }
                });

                //TODO remove after tests
                if(coordinates != null)
                {
                    mapboxMap.addMarker(new MarkerOptions().position(coordinates).title("Uniejow"));
                }

                mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public boolean onMapLongClick(@NonNull final LatLng point) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.place_form_dialog, null);
                        final EditText etName = dialogView.findViewById(R.id.tName);
                        final EditText etDescription = dialogView.findViewById(R.id.tDescription);
                        Button btnSave = dialogView.findViewById(R.id.btnSave);

                        dialogBuilder.setView(dialogView);
                        final AlertDialog dialog = dialogBuilder.create();

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = etName.getText().toString();
                                String description = etDescription.getText().toString();
                                database.locationDao().insertAllLocations(new Location(name,
                                        description, point.getLatitude(), point.getLongitude()));
                                mapboxMap.addMarker(new MarkerOptions().position(point).title(name));
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        return false;
                    }
                });

            }
        });

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                GPS_UPDATE_INTERVAL,
                MIN_DISTANCE_TO_GPS_UPDATE,
                locationListener
        );

        return view;
    }


}
