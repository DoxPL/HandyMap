package pl.dominikgaloch.pracalicencjacka.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.data.dao.CategoryDao;
import pl.dominikgaloch.pracalicencjacka.data.dao.LocationDao;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;

public class CategoryRepository {
    private static ApplicationDatabase databaseInstance;
    private CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        databaseInstance = ApplicationDatabase.getInstance(context);
        categoryDao = databaseInstance.categoryDao();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public void insertCategory(Category category) {
        new InsertCategoryAsyncTask(categoryDao).execute(category);
    }

    public void deleteCategory(Category category) {
        new DeleteCategoryAsyncTask(categoryDao).execute(category);
    }

    public class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao categoryDao;

        public InsertCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.insertCategory(categories[0]);
            return null;
        }
    }

    public class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao categoryDao;

        public DeleteCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.deleteCategory(categories[0]);
            return null;
        }
    }
}
