package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationChangedListener;
import pl.dominikgaloch.pracalicencjacka.interfaces.NearbyPlacesListener;
import pl.dominikgaloch.pracalicencjacka.utilities.NearbyPlacesFinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.style.layers.Property;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NearbyPlacesFragment extends Fragment implements LocationChangedListener {

    private Context context;
    private ListView listView;
    private TextView tvStatus;
    private ArrayAdapter<String> listAdapter;
    private LocationViewModel locationViewModel;
    private NearbyPlacesFinder nearbyPlacesFinder;
    private boolean gpsStatus;

    public NearbyPlacesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_places, container, false);
        context = getContext();
        listView = view.findViewById(R.id.listView);
        tvStatus = view.findViewById(R.id.tvStatus);
        listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        nearbyPlacesFinder = new NearbyPlacesFinder();
        gpsStatus = false;

        locationViewModel.getAllLocation().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                nearbyPlacesFinder.setList((ArrayList<Location>) locations);
            }
        });

        listView.setAdapter(listAdapter);

        ((MainActivity) getActivity()).setLocationListener(this);
        nearbyPlacesFinder.setListener(new NearbyPlacesListener() {
            @Override
            public void onCheckedNearbyLocations(LinkedHashMap<String, Double> nearbyPlaces) {
                listAdapter.clear();
                for(Map.Entry<String, Double> entry : nearbyPlaces.entrySet()) {
                    listAdapter.add(entry.getKey() + " - " + entry.getValue());
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    private void setStatusLabel() {
        if(!gpsStatus) {
            listAdapter.clear();
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(getString(R.string.status_provider_disabled));
        } else {
            tvStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChange(GeoPoint currentPosition) {
        nearbyPlacesFinder.findNearbyPlaces(currentPosition);
    }

    @Override
    public void onProviderStatusChanged(boolean status) {
        this.gpsStatus = status;
        setStatusLabel();
    }

}
