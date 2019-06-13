package pl.dominikgaloch.pracalicencjacka.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.data.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.data.model.Location;
import pl.dominikgaloch.pracalicencjacka.data.model.LocationIndexName;

public class LocationRepository {
    private static ApplicationDatabase databaseInstance;
    private LocationDao locationDao;

    public LocationRepository(Context context) {
        databaseInstance = ApplicationDatabase.getInstance(context);
        locationDao = databaseInstance.locationDao();
    }

    public LiveData<List<Location>> getAllLocations() {
        return locationDao.getAllLocations();
    }

    public LiveData<List<Location>> getAllLocations(int categoryID) {
        return locationDao.getAllLocations(categoryID);
    }

    public LiveData<List<LocationIndexName>> getAllLocationsIndexName() {
        return locationDao.getAllLocationsIndexName();
    }

    public LiveData<Location> getLocationByPattern(String pattern) {
        pattern += "%";
        return locationDao.getLocationByPattern(pattern);
    }

    public LiveData<List<Location>> getAllLocationsByPattern(String pattern) {
        pattern += "%";
        return locationDao.getAllLocationsByPattern(pattern);
    }

    public void insertLocation(Location location) {
        new InsertLocationAsyncTask(locationDao).execute(location);
    }

    public void deleteLocation(Location location) {
        new DeleteLocationAsyncTask(locationDao).execute(location);
    }

    public class InsertLocationAsyncTask extends AsyncTask<Location, Void, Void> {

        private LocationDao locationDao;

        public InsertLocationAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDao.insertLocation(locations[0]);
            return null;
        }
    }

    public class DeleteLocationAsyncTask extends AsyncTask<Location, Void, Void> {

        private LocationDao locationDao;

        public DeleteLocationAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDao.deleteLocation(locations[0]);
            return null;
        }
    }
}
