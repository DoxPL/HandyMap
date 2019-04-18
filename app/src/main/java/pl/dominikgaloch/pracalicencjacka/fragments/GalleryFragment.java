package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.repository.LocationRepository;
import pl.dominikgaloch.pracalicencjacka.utilities.PhotoAdapter;

public class GalleryFragment extends Fragment {
    Spinner spPlaces;
    ArrayAdapter<String> spinnerAdapter;
    Context context;
    GridView gvPhotos;
    PhotoAdapter photoAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        context = getContext();
        spPlaces = view.findViewById(R.id.spPlaces);
        gvPhotos = view.findViewById(R.id.gvPhotos);
        photoAdapter = new PhotoAdapter(context);
        gvPhotos.setAdapter(photoAdapter);
        spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, getPlaceNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPlaces.setAdapter(spinnerAdapter);
        return view;
    }

    public List<String> getPlaceNames()
    {
        return new LocationRepository(context).getAllLocationNames();
    }
}
