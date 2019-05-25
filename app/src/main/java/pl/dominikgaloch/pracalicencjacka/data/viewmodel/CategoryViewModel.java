package pl.dominikgaloch.pracalicencjacka.data.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;
import pl.dominikgaloch.pracalicencjacka.data.repository.CategoryRepository;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }
    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
    public void insertCategory(Category category) {
        categoryRepository.insertCategory(category);
    }

   // public void deleteCategoryById(int categoryId) {
   //     categoryRepository.deleteCategoryById(categoryId);
    //}

    public void deleteCategoryByName(String name) {
        categoryRepository.deleteCategoryByName(name);
    }

}
