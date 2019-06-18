package pl.dominikgaloch.pracalicencjacka.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import pl.dominikgaloch.pracalicencjacka.dialogs.CategoryDialog;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.model.Category;
import pl.dominikgaloch.pracalicencjacka.data.model.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.CategoryViewModel;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.utilities.FragmentUtilities;
import pl.dominikgaloch.pracalicencjacka.utilities.LocationAdapter;

public class LocationListFragment extends Fragment {
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private LocationAdapter adapter;
    private LocationViewModel locationViewModel;
    private CategoryViewModel categoryViewModel;
    private FragmentUtilities fragmentUtilities;
    private Context context;
    private SearchView searchWidget;
    private MenuItem removeCategoryItem;
    private int selectedCategoryId;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);
        context = getContext();
        selectedCategoryId = 0;

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        fragmentUtilities = new FragmentUtilities(getActivity());
        fragmentUtilities.setToolbarTitle(getString(R.string.listView));

        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        adapter = new LocationAdapter(getContext(), getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        setCategoriesObserver();
        setLocationsObserver();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                locationViewModel.setCategoryId((Integer) tab.getTag());
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
        searchItem.setVisible(true);
        addCategoryItem.setVisible(true);
        removeCategoryItem = menu.findItem(R.id.action_remove_category);
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
                CategoryDialog categoryDialog = new CategoryDialog(getActivity(), context);
                categoryDialog.create(tabLayout);
                return true;
            case R.id.action_remove_category:
                createDeleteConfirmDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createDeleteConfirmDialog() {
        DialogInterface.OnClickListener dialogCallback = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        int tabPosition = tabLayout.getSelectedTabPosition();
                        TabLayout.Tab tabToRemove = tabLayout.getTabAt(tabPosition);
                        int categoryId = (int) tabToRemove.getTag();
                        categoryViewModel.deleteCategoryById(categoryId);
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

    private void setCategoriesObserver() {
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                for (Category category : categories) {
                    addTab(category);
                }
                if (tabLayout.getTabCount() == 0) {
                    removeCategoryItem.setEnabled(false);
                } else {
                    removeCategoryItem.setEnabled(true);
                }
            }
        });
    }

    private void setLocationsObserver() {
        locationViewModel.getLocationsByCategoryId().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.setList(locations);
            }
        });
    }

    private void addTab(Category category) {
        TabLayout.Tab tab = tabLayout.newTab().setText(category.getName()).setTag(category.getId());
        tabLayout.addTab(tab);
    }
}
