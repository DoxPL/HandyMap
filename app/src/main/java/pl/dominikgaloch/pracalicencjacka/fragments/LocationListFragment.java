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

import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
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

        ApplicationDatabase database = Room.databaseBuilder(getContext(), ApplicationDatabase.class,
                getString(R.string.database_name)).build();

        list = database.locationDao().getAllLocations();

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new LocationAdapter(getContext(), list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;
    }
}
