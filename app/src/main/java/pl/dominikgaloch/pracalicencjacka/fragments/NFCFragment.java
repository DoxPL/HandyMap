package pl.dominikgaloch.pracalicencjacka.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pl.dominikgaloch.pracalicencjacka.R;

public class NFCFragment extends Fragment {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nfc, container, false);
        LinearLayout mainLayout = view.findViewById(R.id.mainLayout);
        TextView tvNoNfc = view.findViewById(R.id.tvNoNfcInfo);
        ImageButton btnReceiveData = view.findViewById(R.id.btnReceiveData);
        context = getContext();
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if(nfcAdapter == null) {
            mainLayout.setVisibility(View.GONE);
            tvNoNfc.setText(View.VISIBLE);
        } else {
            if(!nfcAdapter.isEnabled())
                openConnectionSettings();
            btnReceiveData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   nfcAdapter.setNdefPushMessageCallback();
                }
            });
        }
        return view;
    }

    public void openConnectionSettings() {
        Intent intent;
        if(Build.VERSION.SDK_INT >= 16) {
            intent = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
        } else {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        }
        startActivity(intent);
    }


}
