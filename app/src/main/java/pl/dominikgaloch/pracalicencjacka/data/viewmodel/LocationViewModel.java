package pl.dominikgaloch.pracalicencjacka.data.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.models.LocationIndexName;
import pl.dominikgaloch.pracalicencjacka.data.repository.LocationRepository;

public class LocationViewModel extends AndroidViewModel {
    private LocationRepository locationRepository;
    private MutableLiveData<Integer> categoryIdInput = new MutableLiveData<>();
    private MutableLiveData<String> patternInput = new MutableLiveData<>();

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository(application);
    }

    private LiveData<List<Location>> locationsByCategoryId = Transformations.switchMap(categoryIdInput, new Function<Integer, LiveData<List<Location>>>() {
        @Override
        public LiveData<List<Location>> apply(Integer categoryId) {
            return locationRepository.getAllLocations(categoryId);
        }
    });

    private LiveData<List<Location>> locationsByPattern = Transformations.switchMap(patternInput, new Function<String, LiveData<List<Location>>>() {
        @Override
        public LiveData<List<Location>> apply(String input) {
            return locationRepository.getAllLocationsByPattern(input);
        }
    });

    public void insert(Location location) {
        locationRepository.insertLocation(location);
    }

    public void delete(Location location) {
        locationRepository.deleteLocation(location);
    }


    public LiveData<List<Location>> getAllLocation() {
        return locationRepository.getAllLocations();
    }

    public void setCategoryId(int categoryId) {
        categoryIdInput.setValue(categoryId);
    }

    public LiveData<List<Location>> getLocationsByCategoryId() {
        return locationsByCategoryId;
    }

    public void setPattern(String pattern) {
        patternInput.setValue(pattern);
    }

    public LiveData<List<Location>> getAllLocationsByPattern() {
        return locationsByPattern;
    }

    public LiveData<List<Location>> getAllLocations(int categoryID) {
        return locationRepository.getAllLocations(categoryID);
    }

    public LiveData<List<LocationIndexName>> getAllLocationIndexNames() {
        return locationRepository.getAllLocationsIndexName();
    }

    public LiveData<Location> getLocationByPattern(String pattern) {
        return locationRepository.getLocationByPattern(pattern);
    }


}
