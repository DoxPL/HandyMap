package pl.dominikgaloch.pracalicencjacka.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.data.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.data.dao.PhotoDao;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.models.Photo;

public class PhotoRepository {
    private static ApplicationDatabase databaseInstance;
    private PhotoDao photoDao;

    public PhotoRepository(Context context) {
        databaseInstance = ApplicationDatabase.getInstance(context);
        photoDao = databaseInstance.photoDao();
    }

    public LiveData<List<Photo>> getAllPhotos(int locationID) {
        return photoDao.getPhotosForLocation(locationID);
    }

    public void insertPhoto(Photo photo) {
        new InsertPhotoAsyncTask(photoDao).execute(photo);
    }

    public void deletePhoto(Photo photo) {
        new DeletePhotoAsyncTask(photoDao).execute(photo);
    }

    public class InsertPhotoAsyncTask extends AsyncTask<Photo, Void, Void> {

        private PhotoDao photoDao;

        public InsertPhotoAsyncTask(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.insertPhoto(photos[0]);
            return null;
        }
    }

    public class DeletePhotoAsyncTask extends AsyncTask<Photo, Void, Void> {

        private PhotoDao photoDao;

        public DeletePhotoAsyncTask(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.deletePhoto(photos[0]);
            return null;
        }
    }
}
