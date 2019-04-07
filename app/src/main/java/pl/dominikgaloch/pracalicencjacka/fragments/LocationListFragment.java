package pl.dominikgaloch.pracalicencjacka.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
                getString(R.string.database_name)).allowMainThreadQueries().build();

        list = (ArrayList<Location>) database.locationDao().getAllLocations();

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new LocationAdapter(getContext(), list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchWidget = (SearchView) menu.findItem(R.id.action_search_item);
        searchWidget.setVisibility(View.VISIBLE);

    }
}
