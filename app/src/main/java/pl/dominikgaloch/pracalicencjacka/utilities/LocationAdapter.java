package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.fragments.MapFragment;
import pl.dominikgaloch.pracalicencjacka.interfaces.ListItemClickListener;
import pl.dominikgaloch.pracalicencjacka.models.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {

    private Context context;
    private List<Location> list;

    public LocationAdapter(Context context, List<Location> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.location_item, null);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder locationHolder, int i) {
        final Location location = list.get(i);

        locationHolder.tvName.setText(location.getName());
        locationHolder.tvDescription.setText(location.getDescription());
        locationHolder.setOnClickListener(new ListItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean longClick) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().
                        replace(R.id.content, new MapFragment(location.getCoordinates())).
                        addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
