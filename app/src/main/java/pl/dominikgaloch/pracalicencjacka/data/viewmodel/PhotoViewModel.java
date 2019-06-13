package pl.dominikgaloch.pracalicencjacka.data.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import pl.dominikgaloch.pracalicencjacka.data.model.Photo;
import pl.dominikgaloch.pracalicencjacka.data.repository.PhotoRepository;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepository photoRepository;
    private MutableLiveData<Integer> locationIdInput = new MutableLiveData<>();

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

    public LiveData<List<Photo>> getPhotosByLocationId() {
        return photosByLocationId;
    }

    private LiveData<List<Photo>> photosByLocationId = Transformations.switchMap(locationIdInput, new Function<Integer, LiveData<List<Photo>>>() {
        @Override
        public LiveData<List<Photo>> apply(Integer locationId) {
            return photoRepository.getAllPhotos(locationId);
        }
    });

    public void setLocationId(int locationId) {
        locationIdInput.setValue(locationId);
    }

}
