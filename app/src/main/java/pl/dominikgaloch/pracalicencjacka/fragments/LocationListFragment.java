package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.activities.SettingsActivity;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.repository.LocationRepository;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.CategoryViewModel;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.utilities.LocationAdapter;

public class LocationListFragment extends Fragment {
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private LocationViewModel locationViewModel;
    private CategoryViewModel categoryViewModel;
    private Context context;
    private SearchView searchWidget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);
        ArrayList<Location> list = new ArrayList<Location>();
        context = getContext();

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new LocationAdapter(getContext(), list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        addCategoriesFromDatabase();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String name = tab.getText().toString();
                addLocationsFromDatabase(name);
                hideSearchWidget();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search_item);
        MenuItem addCategoryItem = menu.findItem(R.id.action_add_category);
        MenuItem removeCategoryItem = menu.findItem(R.id.action_remove_category);
        searchItem.setVisible(true);
        addCategoryItem.setVisible(true);
        removeCategoryItem.setVisible(true);
        searchWidget = (SearchView) searchItem.getActionView();
        searchWidget.setQueryHint(getString(R.string.place_name));
        searchWidget.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_category:
                createFormDialog();
                return true;
            case R.id.action_remove_category:
                createDeleteConfirmDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createFormDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.category_form_dialog, null);
        final EditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        Button btnSave = dialogView.findViewById(R.id.btnSaveCategory);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.removeAllTabs();
                String tabName = etCategoryName.getText().toString();
                Category categoryToInsert = new Category(tabName);
                categoryViewModel.insertCategory(categoryToInsert);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createDeleteConfirmDialog() {
        DialogInterface.OnClickListener dialogCallback = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        int tabPosition = tabLayout.getSelectedTabPosition();
                        TabLayout.Tab tabToRemove = tabLayout.getTabAt(tabPosition);
                        String categoryName = tabToRemove.getText().toString();
                        categoryViewModel.deleteCategoryByName(categoryName);
                        locationViewModel.deleteAllLocationsByCategoryName(categoryName);
                        tabLayout.removeAllTabs();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(getString(R.string.dialog_remove_question))
                .setPositiveButton(getString(R.string.dialog_pos_text), dialogCallback)
                .setNegativeButton(getString(R.string.dialog_neg_text), dialogCallback).show();
    }

    private void addCategoriesFromDatabase() {
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                for (Category category : categories) {
                    addTab(category.getName());
                }
            }
        });
    }

    private void addLocationsFromDatabase(String categoryName) {
        locationViewModel.getAllLocationsByCategoryName(categoryName).observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.setList(locations);
            }
        });
    }

    private void addTab(String name) {
        tabLayout.addTab(tabLayout.newTab().setText(name));
    }

    private void hideSearchWidget() {
        if(!searchWidget.isIconified()) {
            searchWidget.setQuery(null, false);
            searchWidget.setIconified(true);
        }
    }

}
