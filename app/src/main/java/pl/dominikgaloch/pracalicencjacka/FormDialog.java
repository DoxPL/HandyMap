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

    public static void createFormDialog(Activity activity, Context context, final GeoPoint point, final LocationSavedCallback callback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = activity.getLayoutInflater().inflate(R.layout.place_form_dialog, null);
        final EditText etName = dialogView.findViewById(R.id.tName);
        final EditText etDescription = dialogView.findViewById(R.id.tDescription);
        final Spinner spPinColor = dialogView.findViewById(R.id.spPinColor);
        final Spinner spCategory = dialogView.findViewById(R.id.spCategory);
        final LinearLayout manualCoordinatesForm = dialogView.findViewById(R.id.manualCoordsForm);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Switch swInputType = dialogView.findViewById(R.id.swAutoManual);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item);
        final List<Category> allCategories = new ArrayList<>();
        spCategory.setAdapter(spinnerAdapter);
        fillSpinner(activity, allCategories, spinnerAdapter);

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
                int color = spPinColor.getSelectedItemPosition();
                int category = allCategories.get(spCategory.getSelectedItemPosition()).getId();
                Location locationToInsert = new Location(name, description, point.getLatitude(), point.getLongitude(), color, category);
                callback.onDialogSuccess(locationToInsert);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static void fillSpinner(Activity activity, final List<Category> allCategories, final ArrayAdapter<String> adapter) {
        CategoryViewModel categoryViewModel = ViewModelProviders.of((FragmentActivity) activity).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe((FragmentActivity) activity, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                allCategories.addAll(categories);
                for(Category category : categories) {
                    adapter.add(category.getName());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}
