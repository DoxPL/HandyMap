package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.osmdroid.util.GeoPoint;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.model.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.dialogs.FormDialog;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationSavedCallback;
import pl.dominikgaloch.pracalicencjacka.utilities.FragmentUtilities;
import pl.dominikgaloch.pracalicencjacka.utilities.Validator;


public class QRScannerFragment extends Fragment {
    private IntentIntegrator integrator;
    private LocationViewModel locationViewModel;
    private FragmentUtilities fragmentUtilities;
    private FloatingActionButton fab;
    private Button btnScan;

    public QRScannerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrscanner, container, false);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        fragmentUtilities = new FragmentUtilities(getActivity());
        fragmentUtilities.setToolbarTitle(getString(R.string.qrscannerView));
        fab = getActivity().findViewById(R.id.fab);
        btnScan = view.findViewById(R.id.btnScanQR);
        fab.setVisibility(View.GONE);
        integrator = (IntentIntegrator) IntentIntegrator.forSupportFragment(QRScannerFragment.this);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator.initiateScan();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
            Snackbar.make(getView(), getString(R.string.qr_read_failed), Snackbar.LENGTH_LONG).show();
            return;
        }
        String[] resultData = (result.getContents() != null) ? result.getContents().split(",") : new String[0];
        GeoPoint point = parseGeoPoint(resultData);
        if (point == null) {
            Snackbar.make(getView(), getString(R.string.qr_incorrect_coordinates), Snackbar.LENGTH_LONG).show();
            return;
        }
        FormDialog formDialog;
        if (resultData.length > 2)
            formDialog = new FormDialog(getActivity(), getContext(), point, resultData[2]);
        else
            formDialog = new FormDialog(getActivity(), getContext(), point);

        formDialog.create(new LocationSavedCallback() {
            @Override
            public void onDialogSuccess(Location locationToInsert) {
                locationViewModel.insert(locationToInsert);
                fragmentUtilities.switchFragment(MapFragment.newInstance(locationToInsert));
            }
        });
    }


    private GeoPoint parseGeoPoint(String[] data) {
        double latitude, longitude;
        if (data.length > 1 && Validator.validateCoord(data[0]) && Validator.validateCoord(data[1])) {
            latitude = Double.parseDouble(data[0]);
            longitude = Double.parseDouble(data[1]);
            return new GeoPoint(latitude, longitude);
        }
        return null;
    }
}
