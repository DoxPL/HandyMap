package pl.dominikgaloch.pracalicencjacka.activities;

import androidx.appcompat.app.AppCompatActivity;
import pl.dominikgaloch.pracalicencjacka.R;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ivPhoto = findViewById(R.id.ivFullSizePhoto);
        if(getIntent().hasExtra("photo_path")) {
            String path = getIntent().getStringExtra("photo_path");
            Picasso.get().load(new File(path)).into(ivPhoto);
        }
    }
}
