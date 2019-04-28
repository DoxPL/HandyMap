package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.dominikgaloch.pracalicencjacka.data.models.Photo;
import pl.dominikgaloch.pracalicencjacka.data.repository.PhotoRepository;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.PhotoViewModel;

public class PhotoAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> photoList;
    private PhotoViewModel photoViewModel;
    public static final int PHOTO_WIDTH = 200;
    public static final int PHOTO_HEIGHT = 180;

    public PhotoAdapter(Context context, PhotoViewModel photoViewModel) {
        this.context = context;
        this.photoViewModel = photoViewModel;
        this.photoList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return photoList.size();
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
        return ivPhotoItem;
    }

    public void loadPhoto(String path, ImageView ivPhoto) {
        Picasso.get().load(new File(path)).resize(PHOTO_WIDTH, PHOTO_HEIGHT).into(ivPhoto);
    }

    public String getPhotoPath(int position) {
        return photoList.get(position).getPhotoLocation();
    }

    public void removePhoto(int position) {
        photoViewModel.delete(photoList.get(position));
        photoList.remove(position);
    }

    public void setList(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    public void insertPhoto(Photo photo) {
        photoViewModel.insert(photo);
        photoList.add(photo);
        notifyDataSetChanged();
    }

}
