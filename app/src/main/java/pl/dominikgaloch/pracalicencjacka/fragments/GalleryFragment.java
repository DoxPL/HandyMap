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
import android.widget.Toast;

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
import pl.dominikgaloch.pracalicencjacka.activities.ImageViewActivity;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.models.Photo;
import pl.dominikgaloch.pracalicencjacka.repository.LocationRepository;
import pl.dominikgaloch.pracalicencjacka.repository.PhotoRepository;
import pl.dominikgaloch.pracalicencjacka.utilities.PhotoAdapter;

public class GalleryFragment extends Fragment {
    private Spinner spPlaces;
    private ArrayAdapter<String> spinnerAdapter;
    private Context context;
    private GridView gvPhotos;
    private FloatingActionButton fabTakePhoto;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;
    private static final int PHOTO_TAKEN_CODE = 1002;
    private static final String FILE_EXTENSION = ".jpg";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        context = getContext();
        fabTakePhoto = getActivity().findViewById(R.id.fab);
        spPlaces = view.findViewById(R.id.spPlaces);
        gvPhotos = view.findViewById(R.id.gvPhotos);
        photoList = new PhotoRepository(context).getAllPhotos(1);
        //getPhotoPathList();
        photoAdapter = new PhotoAdapter(context, photoList);
        gvPhotos.setAdapter(photoAdapter);
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, getPlaceNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPlaces.setAdapter(spinnerAdapter);
        fabTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = makePhotoFile();
                if(file != null) {
                    Uri photoUri = FileProvider.getUriForFile(context, "pl.dominikgaloch.pracalicencjacka.fileprovider", file);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }

                startActivityForResult(intent, PHOTO_TAKEN_CODE);
            }
        });
        gvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ImageViewActivity.class);
                intent.putExtra("photo_path", getPhotoPath(position));
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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PHOTO_TAKEN_CODE) {
            Toast.makeText(context, "Zrobiono fotke", Toast.LENGTH_LONG).show();
            photoAdapter.notifyDataSetChanged();
        }
    }

    public List<String> getPlaceNames() {
        return new LocationRepository(context).getAllLocationNames();
    }

    public String getPhotoPath(int position) {
        return photoList.get(position).getPhotoLocation();
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
        savePhoto(temporaryFile.getAbsolutePath());
        return temporaryFile;
    }

    public void savePhoto(String path) {
        //
        Photo photo = new Photo();
        photo.setPhotoLocation(path);
        photo.setPlaceID(1);
        new PhotoRepository(context).insertPhoto(photo);
        photoList.add(photo);
        //
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File fileToSave = new File(path);
        Uri uri = Uri.fromFile(fileToSave);
        intent.setData(uri);
        getActivity().sendBroadcast(intent);
    }

    public boolean deletePhoto(String path) {
        File fileToDelete = new File(path);
        return fileToDelete.delete();
    }


    public void createDialog(final View view, final int currentItemPosition) {
        DialogInterface.OnClickListener dialogCallback = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        String photoPath = photoList.get(currentItemPosition).getPhotoLocation();
                        photoAdapter.removePhoto(currentItemPosition);
                        photoAdapter.notifyDataSetChanged();
                        deletePhoto(photoPath);
                        Snackbar.make(view, context.getString(R.string.text_item_removed), Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(context.getResources().getString(R.string.dialog_question))
                .setPositiveButton(context.getResources().getString(R.string.dialog_pos_text), dialogCallback)
                .setNegativeButton(context.getResources().getString(R.string.dialog_neg_text), dialogCallback).show();
    }
}