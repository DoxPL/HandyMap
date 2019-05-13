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
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    public void insertCategory(Category category) {
        categoryRepository.insertCategory(category);
    }

    public void deleteCategoryByName(String name) {
        categoryRepository.deleteCategoryByName(name);
    }

}
