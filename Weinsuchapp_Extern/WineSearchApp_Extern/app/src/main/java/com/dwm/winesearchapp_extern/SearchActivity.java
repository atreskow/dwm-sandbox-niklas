package com.dwm.winesearchapp_extern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class SearchActivity extends AppCompatActivity {

    Spinner spinnerCode;
    Spinner spinnerYear;
    EditText txtStorageNumber;
    TextView tvMinus;
    TextView tvName;
    Button btnSearch;
    ImageButton btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewHelper.SetupToolbar(this);

        txtStorageNumber = findViewById(R.id.txtStorageNumber);

        spinnerCode = findViewById(R.id.spinner_code);
        spinnerYear = findViewById(R.id.spinner_year);

        tvMinus = findViewById(R.id.tvMinus);
        tvName = findViewById(R.id.tvName);

        btnSearch = findViewById(R.id.btnLogin);
        btnScan = findViewById(R.id.btnScan);

        btnSearch.setOnClickListener(searchListener);
        txtStorageNumber.setOnEditorActionListener(searchEnterListener);
        btnScan.setOnClickListener(qrButtonListener);
    }

    @Override
    public void onResume(){     //Setzt aktuelle Trophy-Wahl bei Verwendung des Android Zurück-Buttons der Toolbar
        super.onResume();
        /*
        if (Session.getSelectedCode() != null) {
            TrophyCode selectedTrophy = Session.getTrophyCodes().stream().filter(tc -> tc.Code.equals(Session.getSelectedCode())).findAny().orElse(null);
            int codePosition = Session.getTrophyCodes().indexOf(selectedTrophy);
            spinnerCode.setSelection(codePosition);

            int yearPosition = selectedTrophy.Years.indexOf(Session.getSelectedYear());
            spinnerYear.setSelection(yearPosition);
        }
        */
    }

    private void findWine() {
        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);
        String selectedCode = spinnerCode.getSelectedItem().toString();
        String selectedYear = spinnerYear.getSelectedItem().toString();

        String wineName = txtStorageNumber.getText().toString();
        boolean success = Utils.GetWineData(this, wineName);
        if (!success) {
            ViewHelper.ToggleLoadingAnimation(this, View.GONE);
            return;
        }

        Intent i = new Intent(getApplicationContext(), WinedetailsActivity.class);
        getApplicationContext().startActivity(i);
        ViewHelper.ToggleLoadingAnimation(this, View.GONE);
    }

    View.OnClickListener searchListener = view -> {
        new Thread(() ->  findWine()).start();
    };

    TextView.OnEditorActionListener searchEnterListener = (v, actionId, event) -> {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            new Thread(() ->  findWine()).start();
            handled = true;
        }
        return handled;
    };

    View.OnClickListener qrButtonListener = view -> {
        IntentIntegrator integrator = new IntentIntegrator(SearchActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Lagernummer QR Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
            } else {
                txtStorageNumber.setText(result.getContents());
                new Thread(() ->  findWine()).start();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}