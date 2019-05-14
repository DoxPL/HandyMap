package pl.dominikgaloch.pracalicencjacka.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();

    @Insert
    void insertCategory(Category category);

    @Query("DELETE FROM category WHERE category_name = :categoryName")
    void deleteCategoryByName(String categoryName);

}