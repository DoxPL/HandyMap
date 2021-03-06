package pl.dominikgaloch.pracalicencjacka.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.dominikgaloch.pracalicencjacka.data.ApplicationDatabase;
import pl.dominikgaloch.pracalicencjacka.data.dao.CategoryDao;
import pl.dominikgaloch.pracalicencjacka.data.model.Category;

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

    public void deleteCategoryById(int categoryId) {
        new DeleteCategoryByIdAsyncTask(categoryDao).execute(categoryId);
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

    public class DeleteCategoryByIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private CategoryDao categoryDao;

        public DeleteCategoryByIdAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            categoryDao.deleteCategoryById(integers[0]);
            return null;
        }
    }
}
