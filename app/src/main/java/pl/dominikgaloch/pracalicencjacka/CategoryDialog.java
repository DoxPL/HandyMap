package pl.dominikgaloch.pracalicencjacka;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.CategoryViewModel;

public class CategoryDialog {
    private View dialogView;
    private EditText etCategoryName;
    private Button btnSave;
    private Activity activity;
    private Context context;

    public CategoryDialog(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        initComponents();
    }


    private void initComponents() {
        dialogView = activity.getLayoutInflater().inflate(R.layout.category_form_dialog, null);
        etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        btnSave = dialogView.findViewById(R.id.btnSaveCategory);
    }

    public void create(final TabLayout categoryTabLayout) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoryTabLayout != null)
                    categoryTabLayout.removeAllTabs();
                String tabName = etCategoryName.getText().toString();
                Category categoryToInsert = new Category(tabName);
                insertCategoryIntoDatabase(categoryToInsert);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void insertCategoryIntoDatabase(Category category) {
        CategoryViewModel categoryViewModel = ViewModelProviders.of((FragmentActivity) context).get(CategoryViewModel.class);
        categoryViewModel.insertCategory(category);
    }

}
