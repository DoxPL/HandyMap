package pl.dominikgaloch.pracalicencjacka.data.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.models.LocationIndexName;
import pl.dominikgaloch.pracalicencjacka.data.repository.LocationRepository;

public class LocationViewModel extends AndroidViewModel {
    private LocationRepository locationRepository;
    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository(application);
    }

    public void insert(Location location) {
        locationRepository.insertLocation(location);
    }

    public void delete(Location location) {
        locationRepository.deleteLocation(location);
    }


    public LiveData<List<Location>> getAllLocation() {
        return locationRepository.getAllLocations();
    }

    public LiveData<List<Location>> getAllLocation(int categoryID) {
        return locationRepository.getAllLocations(categoryID);
    }

    public LiveData<List<Location>> getAllLocationsByCategoryName(String categoryName) {
        return locationRepository.getAllLocationsByCategoryName(categoryName);
    }

    public LiveData<List<LocationIndexName>> getAllLocationIndexNames() {
        return locationRepository.getAllLocationsIndexName();
    }

    public LiveData<Location> getLocationByPattern(String pattern) {
        return locationRepository.getLocationByPattern(pattern);
    }
}
