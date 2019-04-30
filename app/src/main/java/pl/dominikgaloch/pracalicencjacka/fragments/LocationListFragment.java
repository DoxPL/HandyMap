package pl.dominikgaloch.pracalicencjacka.fragments;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import pl.dominikgaloch.pracalicencjacka.R;
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
    private FloatingActionButton fabAddCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);
        ArrayList<Location> list = new ArrayList<Location>();

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        fabAddCategory = getActivity().findViewById(R.id.fab);
        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createForm();
            }
        });

        adapter = new LocationAdapter(getContext(), list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        addCategoriesFromDatabase();

        locationViewModel.getAllLocation().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.setList(locations);
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search_item);
        searchItem.setVisible(true);
        //SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchWidget = (SearchView) searchItem.getActionView();
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

    public void createForm() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.category_form_dialog, null);
        final EditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        Button btnSave = dialogView.findViewById(R.id.btnSaveCategory);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category categoryToInsert = new Category(etCategoryName.getText().toString());
                tabLayout.addTab(tabLayout.newTab().setText(categoryToInsert.getName()));
                categoryViewModel.insertCategory(categoryToInsert);
            }
        });
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void addCategoriesFromDatabase() {
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                for(Category category : categories) {
                    tabLayout.addTab(tabLayout.newTab().setText(category.getName()));
                }
            }
        });
    }

}
