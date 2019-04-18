package pl.dominikgaloch.pracalicencjacka.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.repository.LocationRepository;
import pl.dominikgaloch.pracalicencjacka.utilities.PhotoAdapter;

public class GalleryFragment extends Fragment {
    private Spinner spPlaces;
    private ArrayAdapter<String> spinnerAdapter;
    private Context context;
    private GridView gvPhotos;
    private FloatingActionButton fabTakePhoto;
    private PhotoAdapter photoAdapter;
    private static final int PHOTO_TAKEN_CODE = 1002;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        context = getContext();
        fabTakePhoto = getActivity().findViewById(R.id.fab);
        spPlaces = view.findViewById(R.id.spPlaces);
        gvPhotos = view.findViewById(R.id.gvPhotos);
        photoAdapter = new PhotoAdapter(context);
        gvPhotos.setAdapter(photoAdapter);
        spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, getPlaceNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPlaces.setAdapter(spinnerAdapter);
        fabTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = makePhotoFile();
                if(file != null) {
                    Uri photoUri = FileProvider.getUriForFile(context, "pl.dominikgaloch.pracalicencjacka.fileprovider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }

                startActivityForResult(intent, PHOTO_TAKEN_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PHOTO_TAKEN_CODE) {
            Toast.makeText(context, "Zdrobiono fotke", Toast.LENGTH_LONG).show();

        }
    }

    public List<String> getPlaceNames() {
        return new LocationRepository(context).getAllLocationNames();
    }

    public File makePhotoFile() {
        String date = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        File destinationDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File temporaryFile = null;
        try {
            temporaryFile = File.createTempFile(date, ".jpg", destinationDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        savePhoto(temporaryFile.getAbsolutePath());
        return temporaryFile;
    }

    public void savePhoto(String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File fileToSave = new File(path);
        Uri uri = Uri.fromFile(fileToSave);
        intent.setData(uri);
        getActivity().sendBroadcast(intent);
    }

}
