package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationChangedListener;
import pl.dominikgaloch.pracalicencjacka.interfaces.NearbyPlacesListener;
import pl.dominikgaloch.pracalicencjacka.utilities.FragmentUtilities;
import pl.dominikgaloch.pracalicencjacka.utilities.NearbyPlacesFinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.style.layers.Property;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NearbyPlacesFragment extends Fragment implements LocationChangedListener {

    private Context context;
    private ListView listView;
    private TextView tvStatus;
    private FloatingActionButton fab;
    private ArrayAdapter<String> listAdapter;
    private LocationViewModel locationViewModel;
    private FragmentUtilities fragmentUtilities;
    private NearbyPlacesFinder nearbyPlacesFinder;
    private boolean gpsStatus;
    private SharedPreferences preferences;
    private static final int DEFAULT_RADIUS_STR = 100;

    public NearbyPlacesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_places, container, false);
        context = getContext();
        listView = view.findViewById(R.id.listView);
        tvStatus = view.findViewById(R.id.tvStatus);
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        fragmentUtilities = new FragmentUtilities(getActivity());
        fragmentUtilities.setToolbarTitle(getString(R.string.nearbyPlacesView));
        gpsStatus = false;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            nearbyPlacesFinder = new NearbyPlacesFinder(preferences.getString("radius", String.valueOf(DEFAULT_RADIUS_STR)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        locationViewModel.getAllLocation().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                nearbyPlacesFinder.setList((ArrayList<Location>) locations);
            }
        });

        listView.setAdapter(listAdapter);
        setStatusLabel();

        ((MainActivity) getActivity()).setLocationListener(this);
        nearbyPlacesFinder.setListener(new NearbyPlacesListener() {
            @Override
            public void onCheckedNearbyLocations(TreeMap<String, Double> nearbyPlacesMap) {
                listAdapter.clear();
                MapComparator mapComparator = new MapComparator(nearbyPlacesMap);
                TreeMap<String, Double> sortedMap = new TreeMap(mapComparator);
                sortedMap.putAll(nearbyPlacesMap);
                for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
                    long roundedDistance = Math.round(entry.getValue());
                    listAdapter.add(entry.getKey() + " - " + roundedDistance + "m");
                }
                listAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void setStatusLabel() {
        if (!gpsStatus) {
            listAdapter.clear();
            tvStatus.setText(getString(R.string.status_provider_disabled));
        } else {
            tvStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setLocationListener(null);
    }

    @Override
    public void onChange(GeoPoint currentPosition, boolean setAsCenter) {
        nearbyPlacesFinder.findNearbyPlaces(currentPosition);
    }

    @Override
    public void onProviderStatusChanged(boolean status) {
        this.gpsStatus = status;
        setStatusLabel();
    }

    class MapComparator implements Comparator<String> {

        private Map<String, Double> map;
        public MapComparator(Map<String, Double> map) {
            this.map = map;
        }


        @Override
        public int compare(String first, String second) {
            if(map.get(first) < map.get(second)) {
                return -1;
            } else {
                return 1;
            }
        }

        public TreeMap<String, Double> getSortedMap() {
            return new TreeMap<>(map);
        }
    }
}

