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
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.data.models.Category;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.CategoryViewModel;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationSavedCallback;
import pl.dominikgaloch.pracalicencjacka.utilities.Validator;

public class FormDialog {

    private Activity activity;
    private Context context;
    private GeoPoint point;
    private ArrayAdapter<String> spinnerAdapter;
    private View dialogView;
    private EditText etName;
    private EditText etDescription;
    private EditText etLatitude;
    private EditText etLongitude;
    private Spinner spPinColor;
    private Spinner spCategory;
    private LinearLayout manualCoordinatesForm;
    private Button btnSave;
    private Switch swManualInput;
    private AlertDialog.Builder dialogBuilder;
    private List<Integer> categoryIdList;
    private CategoryViewModel categoryViewModel;
    private String locationName;

    public FormDialog(Activity activity, Context context, GeoPoint geoPoint) {
        this.activity = activity;
        this.context = context;
        this.point = geoPoint;
        categoryIdList = new ArrayList<>();
        categoryViewModel = ViewModelProviders.of((FragmentActivity) activity).get(CategoryViewModel.class);
        initComponents();
    }

    public FormDialog(Activity activity, Context context, GeoPoint geoPoint, String name) {
        this.activity = activity;
        this.context = context;
        this.point = geoPoint;
        this.locationName = name;
        categoryIdList = new ArrayList<>();
        categoryViewModel = ViewModelProviders.of((FragmentActivity) activity).get(CategoryViewModel.class);
        initComponents();
    }

    private void initComponents() {
        dialogView = activity.getLayoutInflater().inflate(R.layout.place_form_dialog, null);
        etName = dialogView.findViewById(R.id.etName);
        etDescription = dialogView.findViewById(R.id.etDescription);
        etLatitude = dialogView.findViewById(R.id.etLat);
        etLongitude = dialogView.findViewById(R.id.etLng);
        spPinColor = dialogView.findViewById(R.id.spPinColor);
        spCategory = dialogView.findViewById(R.id.spCategory);
        manualCoordinatesForm = dialogView.findViewById(R.id.manualCoordsForm);
        btnSave = dialogView.findViewById(R.id.btnSave);
        swManualInput = dialogView.findViewById(R.id.swAutoManual);
        if (locationName != null) {
            etName.setText(locationName);
        }
    }

    public void create(final LocationSavedCallback callback) {
        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        spinnerAdapter = new ArrayAdapter(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);
        addSpinnerElementsFromDatabase();

        swManualInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                if (checkInputData()) {
                    String name = etName.getText().toString();
                    String description = etDescription.getText().toString();
                    int selectedColor = spPinColor.getSelectedItemPosition();
                    int selectedCategory = spCategory.getSelectedItemPosition();
                    int categoryId = categoryIdList.get(selectedCategory);
                   
                    Location locationToInsert = new Location(name, description, point.getLatitude(), point.getLongitude(), selectedColor, categoryId);
                    callback.onDialogSuccess(locationToInsert);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void addSpinnerElementsFromDatabase() {
        categoryViewModel.getAllCategories().observe((FragmentActivity) activity, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                for (Category category : categories) {
                    spinnerAdapter.add(category.getName());
                    categoryIdList.add(category.getId());
                }
                spinnerAdapter.notifyDataSetChanged();

                if (spCategory.getCount() == 0) {
                    categoryViewModel.insertCategory(new Category(context.getString(R.string.tab_default)));
                }
            }
        });
    }

    private boolean checkInputData() {
        if (!Validator.validateName(etName.getText().toString())) {
            Snackbar.make(dialogView, context.getString(R.string.form_incorrect_name), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (!Validator.validateDescription(etDescription.getText().toString())) {
            Snackbar.make(dialogView, context.getString(R.string.form_incorrect_description), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (swManualInput.isChecked() && (!Validator.validateCoord(etLatitude.getText().toString()) ||
                !Validator.validateCoord(etLongitude.getText().toString()))) {
            Snackbar.make(dialogView, context.getString(R.string.form_incorrect_coordinates), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
