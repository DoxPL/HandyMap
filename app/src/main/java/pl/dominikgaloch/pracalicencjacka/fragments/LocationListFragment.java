package pl.dominikgaloch.pracalicencjacka.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.models.Location;
import pl.dominikgaloch.pracalicencjacka.utilities.LocationAdapter;

public class LocationListFragment extends Fragment {
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ArrayList<Location> list = new ArrayList<Location>();

        //TODO delete sample object
        Location sample = new Location();
        sample.setName("Przykladowe");
        sample.setDescription("Miejsce");
        sample.setCoordinates(new LatLng(51.974130, 18.793245));

        Location s = new Location();
        s.setName("Nazwa");
        s.setDescription("Przykladowy opis");

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new LocationAdapter(getContext(), list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        list.add(sample);
        list.add(s);
        adapter.notifyDataSetChanged();

        return view;
    }
}
