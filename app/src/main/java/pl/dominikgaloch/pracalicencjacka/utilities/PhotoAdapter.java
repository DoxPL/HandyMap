package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.models.Photo;
import pl.dominikgaloch.pracalicencjacka.repository.PhotoRepository;

public class PhotoAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> photos;
    public static final int PHOTO_WIDTH = 200;
    public static final int PHOTO_HEIGHT = 180;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView ivPhotoItem = new ImageView(context);
        loadPhoto(getPhotoPath(position), ivPhotoItem);
        ivPhotoItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //ivPhotoItem.setLayoutParams(new GridView.LayoutParams(PHOTO_WIDTH, PHOTO_HEIGHT));
        return ivPhotoItem;
    }

    public void loadPhoto(String path, ImageView ivPhoto) {
        Picasso.get().load(new File(path)).resize(PHOTO_WIDTH, PHOTO_HEIGHT).into(ivPhoto);
    }

    public String getPhotoPath(int position) {
        return photos.get(position).getPhotoLocation();
    }

    public void removePhoto(int position) {
        new PhotoRepository(context).deletePhoto(photos.get(position));
        photos.remove(position);
    }
}
