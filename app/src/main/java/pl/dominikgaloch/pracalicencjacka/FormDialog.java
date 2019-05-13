package pl.dominikgaloch.pracalicencjacka;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.activities.MainActivity;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.CategoryViewModel;
import pl.dominikgaloch.pracalicencjacka.fragments.MapFragment;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationSavedCallback;

public class FormDialog {

    private Activity activity;
    private Context context;
    private GeoPoint point;
    private ArrayAdapter<String> spinnerAdapter;
    private View dialogView;
    private EditText etName;
    private EditText etDescription;
    private Spinner spPinColor;
    private Spinner spCategory;
    private LinearLayout manualCoordinatesForm;
    private Button btnSave;
    private Switch swInputType;
    private AlertDialog.Builder dialogBuilder;
    private List<Integer> categoryIdList;

    public FormDialog(Activity activity, Context context, GeoPoint geoPoint) {
        this.activity = activity;
        this.context = context;
        this.point = geoPoint;
        categoryIdList = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        dialogView = activity.getLayoutInflater().inflate(R.layout.place_form_dialog, null);
        etName = dialogView.findViewById(R.id.tName);
        etDescription = dialogView.findViewById(R.id.tDescription);
        spPinColor = dialogView.findViewById(R.id.spPinColor);
        spCategory = dialogView.findViewById(R.id.spCategory);
        manualCoordinatesForm = dialogView.findViewById(R.id.manualCoordsForm);
        btnSave = dialogView.findViewById(R.id.btnSave);
        swInputType = dialogView.findViewById(R.id.swAutoManual);
    }


    public void create(final LocationSavedCallback callback) {
        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        spinnerAdapter = new ArrayAdapter(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);
        addSpinnerElementsFromDatabase();

        swInputType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    manualCoordinatesForm.setVisibility(View.VISIBLE);
                } else {
                    manualCoordinatesForm.setVisibility(View.GONE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                int selectedColor = spPinColor.getSelectedItemPosition();
                int selectedCategory = spCategory.getSelectedItemPosition();
                int categoryId = categoryIdList.get(selectedCategory);
                Location locationToInsert = new Location(name, description, point.getLatitude(), point.getLongitude(), selectedColor, categoryId);
                callback.onDialogSuccess(locationToInsert);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addSpinnerElementsFromDatabase() {
        final CategoryViewModel categoryViewModel = ViewModelProviders.of((FragmentActivity) activity).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe((FragmentActivity) activity, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if(categories.size() == 0) {
                    categoryViewModel.insertCategory(new Category(context.getString(R.string.tab_default)));
                }

                for(Category category : categories) {
                    spinnerAdapter.add(category.getName());
                    categoryIdList.add(category.getId());
                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

}
