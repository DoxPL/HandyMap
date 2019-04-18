package pl.dominikgaloch.pracalicencjacka.utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import pl.dominikgaloch.pracalicencjacka.R;

public class PhotoAdapter extends BaseAdapter {

    private Context context;
    List<Integer> photos;

    public PhotoAdapter(Context context) {
        this.context = context;
        photos = new ArrayList<>();
        photos.add(R.drawable.bonuspack_bubble);
        photos.add(R.drawable.marker_default);
        photos.add(R.drawable.osm_ic_follow_me);
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
        ivPhotoItem.setImageResource(photos.get(position));
        ivPhotoItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivPhotoItem.setLayoutParams(new GridView.LayoutParams(200, 180));
        return ivPhotoItem;
    }
}
