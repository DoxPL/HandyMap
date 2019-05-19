package pl.dominikgaloch.pracalicencjacka.utilities;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.fragments.MapFragment;
import pl.dominikgaloch.pracalicencjacka.interfaces.ListItemClickListener;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationHolder> implements Filterable {

    private Context context;
    private FragmentActivity activity;
    private List<Location> locationList;
    private List<Location> listCopy;
    private LocationViewModel locationViewModel;
    private FragmentUtilities fragmentUtilities;

    public LocationAdapter(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
        this.locationList = new ArrayList<>();
        locationViewModel = ViewModelProviders.of((FragmentActivity) context).get(LocationViewModel.class);
        fragmentUtilities = new FragmentUtilities(activity);
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
        final Location location = locationList.get(i);
        locationHolder.tvName.setText(location.getName());
        locationHolder.tvDescription.setText(location.getDescription());
        locationHolder.setOnClickListener(new ListItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean longClick) {
                if (longClick) {
                    createDialog(view, position);
                } else {
                    fragmentUtilities.switchFragment(new MapFragment(location));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void createDialog(final View view, final int currentItemPosition) {
        DialogInterface.OnClickListener dialogCallback = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        removeLocationFromDatabase(locationList.get(currentItemPosition));
                        Snackbar.make(view, context.getString(R.string.text_item_removed), Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(context.getString(R.string.dialog_question))
                .setPositiveButton(context.getString(R.string.dialog_pos_text), dialogCallback)
                .setNegativeButton(context.getString(R.string.dialog_neg_text), dialogCallback).show();
    }


    public Filter getFilter() {
        final List<Location> filteredList = new ArrayList<>();
        if (listCopy != null) {
            locationList = listCopy;
        }
        Filter listFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    for (Location location : locationList) {
                        if (location.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(location);
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.values = filteredList;
                    results.count = filteredList.size();
                    return results;
                } else {
                    return new FilterResults();
                }
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCopy = locationList;
                locationList = (List<Location>) results.values;
                notifyDataSetChanged();
            }
        };
        return listFilter;
    }

    public void setList(List<Location> locationList) {
        listCopy = null;
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    private void removeLocationFromDatabase(Location location) {
        locationViewModel.delete(location);
    }

}
