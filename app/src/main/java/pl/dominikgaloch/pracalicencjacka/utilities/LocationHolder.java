package pl.dominikgaloch.pracalicencjacka.utilities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.interfaces.ListItemClickListener;

public class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ListItemClickListener listener;
    protected TextView tvName;
    protected TextView tvDescription;

    public LocationHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvlocationName);
        tvDescription = itemView.findViewById(R.id.tvLocationDescription);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setOnClickListener(ListItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        listener.onClick(v, getAdapterPosition(), true);
        return false;
    }
}
