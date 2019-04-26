package pl.dominikgaloch.pracalicencjacka;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import org.osmdroid.util.GeoPoint;

public class FormDialog {

    private Context context;
    private GeoPoint point;
    private EditText etName;
    private EditText etDescription;
    private Spinner spPinColor;
    private ArrayAdapter spColorAdapter;
    private LinearLayout manualCoordinatesForm;
    private Button btnSave;
    private Switch swInputType;

    public FormDialog(Context context, GeoPoint point) {
        this.context = context;
        this.point = point;
        //initComponents();
    }

    public void initComponents(View dialogView) {
        etName = dialogView.findViewById(R.id.tName);
        etDescription = dialogView.findViewById(R.id.tDescription);
        spPinColor = dialogView.findViewById(R.id.spPinColor);
        manualCoordinatesForm = dialogView.findViewById(R.id.manualCoordsForm);
        btnSave = dialogView.findViewById(R.id.btnSave);
        swInputType = dialogView.findViewById(R.id.swAutoManual);
    }

    public void initCallbacks() {

    }
}
