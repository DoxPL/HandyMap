package pl.dominikgaloch.pracalicencjacka.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import pl.dominikgaloch.pracalicencjacka.R;

public class ImageViewActivity extends AppCompatActivity {
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ivPhoto = findViewById(R.id.ivFullSizePhoto);
        if (getIntent().hasExtra("photo_path")) {
            String path = getIntent().getStringExtra("photo_path");
            Picasso.get().load(new File(path)).into(ivPhoto);
        }
    }
}
