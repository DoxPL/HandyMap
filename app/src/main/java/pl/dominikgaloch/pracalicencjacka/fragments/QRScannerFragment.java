package pl.dominikgaloch.pracalicencjacka.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import pl.dominikgaloch.pracalicencjacka.FormDialog;
import pl.dominikgaloch.pracalicencjacka.R;
import pl.dominikgaloch.pracalicencjacka.data.models.Location;
import pl.dominikgaloch.pracalicencjacka.data.viewmodel.LocationViewModel;
import pl.dominikgaloch.pracalicencjacka.interfaces.LocationSavedCallback;
import pl.dominikgaloch.pracalicencjacka.utilities.FragmentUtilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.osmdroid.util.GeoPoint;


public class QRScannerFragment extends Fragment {
    private IntentIntegrator integrator;
    private LocationViewModel locationViewModel;
    private FragmentUtilities fragmentUtilities;
    public QRScannerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrscanner, container, false);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        integrator = (IntentIntegrator) IntentIntegrator.forSupportFragment(this);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
        fragmentUtilities = new FragmentUtilities(getActivity());
        fragmentUtilities.setToolbarTitle(getString(R.string.qrscannerView));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
            Snackbar.make(getView(), getString(R.string.qr_read_failed), Snackbar.LENGTH_LONG).show();
        } else {
            GeoPoint point = parseGeoPoint(result.getContents());
            if(point != null) {
                FormDialog formDialog = new FormDialog(getActivity(), getContext(), point);

                formDialog.create(new LocationSavedCallback() {
                    @Override
                    public void onDialogSuccess(Location locationToInsert) {
                        locationViewModel.insert(locationToInsert);
                        fragmentUtilities.switchFragment(new MapFragment(locationToInsert));
                    }
                });
            } else {
                Snackbar.make(getView(), getString(R.string.qr_incorrect_coordinates), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private GeoPoint parseGeoPoint(String text) {
        String[] coordinates = text.split(",");
        if (coordinates.length == 2) {
            double latitude, longitude;
            try {
                latitude = Double.parseDouble(coordinates[0]);
                longitude = Double.parseDouble(coordinates[1]);
                return new GeoPoint(latitude, longitude);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
