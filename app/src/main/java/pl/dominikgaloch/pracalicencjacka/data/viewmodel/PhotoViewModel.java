package pl.dominikgaloch.pracalicencjacka.data.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.dominikgaloch.pracalicencjacka.data.models.Photo;
import pl.dominikgaloch.pracalicencjacka.data.repository.PhotoRepository;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepository photoRepository;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
    }

    public void insert(Photo photo) {
        photoRepository.insertPhoto(photo);
    }

    public void delete(Photo photo) {
        photoRepository.deletePhoto(photo);
    }

    public LiveData<List<Photo>> getAllPhotos(int locationID) {
        return photoRepository.getAllPhotos(locationID);
    }
}
