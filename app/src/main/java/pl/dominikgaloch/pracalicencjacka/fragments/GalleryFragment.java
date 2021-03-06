package pl.dominikgaloch.pracalicencjacka.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.activities.ImageViewActivity;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.model.LocationIndexName;
import pl.dominikgaloch.pracalicencjacka.data.model.Photo;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.PhotoViewModel;
import pl.dominikgaloch.pracalicencjacka.utilities.FragmentUtilities;
import pl.dominikgaloch.pracalicencjacka.utilities.PhotoAdapter;

public class GalleryFragment extends Fragment {
    private Spinner spLocations;
    private ArrayAdapter<LocationIndexName> spinnerAdapter;
    private Context context;
    private GridView gvPhotos;
    private FloatingActionButton fabTakePhoto;
    private PhotoAdapter photoAdapter;
    private LocationViewModel locationViewModel;
    private PhotoViewModel photoViewModel;
    private FragmentUtilities fragmentUtilities;
    private String currentPhotoPath;
    private static final int PHOTO_TAKEN_CODE = 1002;
    private static final String FILE_EXTENSION = ".jpg";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        context = getContext();
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        fragmentUtilities = new FragmentUtilities(getActivity());
        fragmentUtilities.setToolbarTitle(getString(R.string.galleryView));
        fabTakePhoto = getActivity().findViewById(R.id.fab);
        fabTakePhoto.setVisibility(View.VISIBLE);
        spLocations = view.findViewById(R.id.spPlaces);
        gvPhotos = view.findViewById(R.id.gvPhotos);
        photoAdapter = new PhotoAdapter(context, photoViewModel);
        spinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_style);
        gvPhotos.setAdapter(photoAdapter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spLocations.setAdapter(spinnerAdapter);

        populateSpinnerItems();
        populatePhotoItems();

        fabTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = makePhotoFile();
                if (file != null) {
                    Uri photoUri = FileProvider.getUriForFile(context,
                            "pl.dominikgaloch.pracalicencjacka.fileprovider", file);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, PHOTO_TAKEN_CODE);
                }
            }
        });

        gvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ImageViewActivity.class);
                intent.putExtra("photo_path", photoAdapter.getPhotoPath(position));
                startActivity(intent);
            }
        });

        gvPhotos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(view, position);
                return true;
            }
        });

        spLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int locationId = ((LocationIndexName) parent.getItemAtPosition(position)).getId();
                photoViewModel.setLocationId(locationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PHOTO_TAKEN_CODE) {
            savePhoto(currentPhotoPath, ((LocationIndexName) spLocations.getSelectedItem()).getId());
            Snackbar.make(getView(), getString(R.string.text_photo_saved), Snackbar.LENGTH_LONG).show();
        }
    }

    public void populateSpinnerItems() {
        locationViewModel.getAllLocationIndexNames().observe(this, new Observer<List<LocationIndexName>>() {
            @Override
            public void onChanged(List<LocationIndexName> locationIndexNames) {
                spinnerAdapter.addAll(locationIndexNames);
            }
        });
    }

    public void populatePhotoItems() {
        photoViewModel.getPhotosByLocationId().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                photoAdapter.setList(photos);
            }
        });
    }

    public File makePhotoFile() {
        String date = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        File destinationDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File temporaryFile = null;
        try {
            temporaryFile = File.createTempFile(date, FILE_EXTENSION, destinationDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPhotoPath = temporaryFile.getAbsolutePath();
        return temporaryFile;
    }

    public void savePhoto(String path, int locationId) {
        Photo photo = new Photo();
        photo.setPhotoLocation(path);
        photo.setLocationID(locationId);
        photoAdapter.insertPhoto(photo);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File fileToSave = new File(path);
        Uri uri = Uri.fromFile(fileToSave);
        intent.setData(uri);
        getActivity().sendBroadcast(intent);
    }


    public void createDialog(final View view, final int currentItemPosition) {
        DialogInterface.OnClickListener dialogCallback = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        photoAdapter.removePhoto(currentItemPosition);
                        Snackbar.make(view, context.getString(R.string.text_item_removed), Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(context.getResources().getString(R.string.dialog_remove_photo_question))
                .setPositiveButton(context.getResources().getString(R.string.dialog_pos_text), dialogCallback)
                .setNegativeButton(context.getResources().getString(R.string.dialog_neg_text), dialogCallback).show();
    }

    @Override
    public void onResume() {
        fabTakePhoto.setImageResource(R.drawable.camera_icon);
        super.onResume();
    }
}
