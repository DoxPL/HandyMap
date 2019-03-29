package pl.dominikgaloch.pracalicencjacka;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

public class FormDialog extends AlertDialog {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Context context;

    protected FormDialog(Context context) {
        super(context);
    }

    class Builder extends AlertDialog.Builder
    {

        public Builder(Context context) {
            super(context);
        }
    }
}
