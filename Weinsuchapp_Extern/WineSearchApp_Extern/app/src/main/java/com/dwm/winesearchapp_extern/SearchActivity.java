package com.dwm.winesearchapp_extern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dwm.winesearchapp_extern.Pojo.TrophyCode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        findViewById(R.id.headerBtnLogout).setOnClickListener(ViewHelper.LogoutListener(this));

        populateCodeSpinner();
    }

    @Override
    public void onResume(){     //Setzt aktuelle Trophy-Wahl bei Verwendung des Android Zurück-Buttons der Toolbar
        super.onResume();
        if (Session.getSelectedCode() != null) {
            TrophyCode selectedTrophy = Session.getTrophyCodes().stream().filter(tc -> tc.Code.equals(Session.getSelectedCode())).findAny().orElse(null);
            int codePosition = Session.getTrophyCodes().indexOf(selectedTrophy);
            spinnerCode.setSelection(codePosition);

            int yearPosition = selectedTrophy.Years.indexOf(Session.getSelectedYear());
            spinnerYear.setSelection(yearPosition);
        }
    }

    private void findWine() {
        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);
        String selectedCode = spinnerCode.getSelectedItem().toString();
        String selectedYear = spinnerYear.getSelectedItem().toString();
        Session.setSelectedCode(selectedCode);
        Session.setSelectedYear(selectedYear);
        TrophyCode trophyCode = Session.getTrophyCodes().stream().filter(tc -> tc.Code.equals(selectedCode)).findAny().orElse(null);
        int yearIndex = trophyCode.Years.indexOf(selectedYear);

        UUID trophyId = trophyCode.Ids.get(yearIndex);
        String storageNumberString = txtStorageNumber.getText().toString().isEmpty() ? "0" : txtStorageNumber.getText().toString();
        int storageNumber = Integer.parseInt(storageNumberString);
        boolean success = Utils.GetWineData(this, trophyId, storageNumber);
        if (!success) {
            ViewHelper.ToggleLoadingAnimation(this, View.GONE);
            return;
        }

        Intent i = new Intent(getApplicationContext(), WinedetailsActivity.class);
        getApplicationContext().startActivity(i);
        ViewHelper.ToggleLoadingAnimation(this, View.GONE);
    }

    private void populateCodeSpinner() {
        List<String> codes = new ArrayList<>();
        for (TrophyCode trophyCode : Session.getTrophyCodes()) {
            codes.add(trophyCode.Code);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, codes);
        spinnerCode.setAdapter(adapter);
        spinnerCode.setOnItemSelectedListener(codeSelectListener);
        spinnerYear.setOnItemSelectedListener(yearSelectListener);
    }

    private void populateYearSpinner() {
        String selectedCode = spinnerCode.getSelectedItem().toString();
        TrophyCode trophyCode = Session.getTrophyCodes().stream().filter(tc -> tc.Code.equals(selectedCode)).findAny().orElse(null);

        if (trophyCode.Years.size() > 0) {
            spinnerYear.setVisibility(View.VISIBLE);
            tvMinus.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, trophyCode.Years);
            spinnerYear.setAdapter(adapter);
        }
        else {
            spinnerYear.setVisibility(View.INVISIBLE);
            tvMinus.setVisibility(View.INVISIBLE);
            spinnerYear.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, new ArrayList<String>()));
            tvName.setText((trophyCode.Names.get(0)));
        }
    }

    AdapterView.OnItemSelectedListener codeSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            populateYearSpinner();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    AdapterView.OnItemSelectedListener yearSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String selectedCode = spinnerCode.getSelectedItem().toString();
            String selectedYear = spinnerYear.getSelectedItem().toString();
            TrophyCode trophyCode = Session.getTrophyCodes().stream().filter(tc -> tc.Code.equals(selectedCode)).findAny().orElse(null);
            int yearIndex = trophyCode.Years.indexOf(selectedYear);
            tvName.setText(trophyCode.Names.get(yearIndex));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

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