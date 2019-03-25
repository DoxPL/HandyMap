package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    private ApplicationDatabase database;

    public MapFragment() {
    }

    public MapFragment(LatLng coords)
    {
        this.coordinates = coords;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Mapbox.setAccessToken(getResources().getString(R.string.mapbox_token));
        locationManager = getActivity().getSystemService(Context.LOCATION_SERVICE);
        database = Room.databaseBuilder(getContext(), ApplicationDatabase.class,
                getString(R.string.database_name)).build();
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

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {
                        mapboxMap.addMarker(new MarkerOptions().position(point).title("User marker"));
                        return false;
                    }
                });

                mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public boolean onMapLongClick(@NonNull final LatLng point) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.place_form_dialog, null);
                        final EditText etName = dialogView.findViewById(R.id.tName);
                        final EditText etDescription = dialogView.findViewById(R.id.tDescription);
                        Button btnSave = dialogView.findViewById(R.id.btnSave);

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                database.locationDao().insertAllLocations(new Location(etName.getText(),
                                        etDescription.getText(), point.getLatitude(), point.getLongitude()));
                            }
                        });

                        dialogBuilder.setView(dialogView);
                        AlertDialog dialog = dialogBuilder.create();
                        dialog.show();
                        return false;
                    }
                });

            }
        });

        return view;
    }


}
