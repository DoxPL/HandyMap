package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
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
                if(longClick) {
                    createDialog(view, position);
                }
                else {
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.content, new MapFragment(location.getGeoPoint())).
                            addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void createDialog(final View view, final int currentItemPosition) {
        DialogInterface.OnClickListener dialogCallback = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Snackbar.make(view, context.getString(R.string.text_item_removed), Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                        //Todo delete after create repository
                        ApplicationDatabase database = Room.databaseBuilder(context, ApplicationDatabase.class,
                                context.getString(R.string.database_name)).allowMainThreadQueries().build();
                        database.locationDao().delete(list.get(currentItemPosition));
                        list.remove(currentItemPosition);
                        notifyDataSetChanged();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(context.getResources().getString(R.string.dialog_question))
                .setPositiveButton(context.getResources().getString(R.string.dialog_pos_text), dialogCallback)
                .setNegativeButton(context.getResources().getString(R.string.dialog_neg_text), dialogCallback).show();
    }
}
