package com.dwm.winesearchapp_extern;

import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dwm.winesearchapp_extern.Pojo.request.FacetQueryGroup;
import com.dwm.winesearchapp_extern.Pojo.request.OptionData;
import com.dwm.winesearchapp_extern.Pojo.request.QueryObjData;
import com.dwm.winesearchapp_extern.Pojo.request.SortParam;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;


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

        String wineName = txtStorageNumber.getText().toString();
        String[] queryTokens = Utils.SetQueryTokens(wineName, 2013, 2020);
        List<FacetQueryGroup> facetQueryGroups = new ArrayList<>();
        for (int i = 0; i < 1; i++) { //temporäre bedingung zum testen
            String[] values = new String[] {"Berlin -Sommerverkostung-", "Asia Wine Trophy"};
            FacetQueryGroup facetQueryGroup = new FacetQueryGroup("trophy_name", values);
            facetQueryGroups.add(facetQueryGroup);
        }
        QueryObjData queryObjData = new QueryObjData(queryTokens, facetQueryGroups.toArray(new FacetQueryGroup[0]));

        int top = 50;
        int skip = 0;
        List<SortParam> sortParams = new ArrayList<>();
        for (int i = 0; i < 1; i++) { //temporäre bedingung zum testen
            String fieldName = "trophy_name";
            boolean orderAsc = false;
            sortParams.add(new SortParam(fieldName, orderAsc));
        }
        String[] resultAttributes = null;
        String[] facets = new String[] {"wine_type", "wine_flavour"};
        String[] highlightFields = null;
        OptionData optionData = new OptionData(top, skip, sortParams.toArray(new SortParam[0]), resultAttributes, facets, highlightFields);

        WineSearchData data = new WineSearchData(queryObjData, optionData);
        boolean success = Utils.GetWineData(this, data);
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