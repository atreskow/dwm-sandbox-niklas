package com.dwm.winesearchapp_extern;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.Item;
import com.dwm.winesearchapp_extern.Pojo.request.FacetQueryGroup;
import com.dwm.winesearchapp_extern.Pojo.request.OptionData;
import com.dwm.winesearchapp_extern.Pojo.request.QueryObjData;
import com.dwm.winesearchapp_extern.Pojo.request.SortParam;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.response.FacetData;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity extends AppCompatActivity {

    EditText txtStorageNumber;
    Button btnSearch;

    DrawerLayout mDrawerLayout;

    ArrayList<String> listDataHeader;
    HashMap<String, List<NavDrawerItem>> listDataChild;

    ExpandableListView expListView;
    ExpandListAdapter listAdapter;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewHelper.SetupToolbar(this);

        txtStorageNumber = findViewById(R.id.txtStorageNumber);
        btnSearch = findViewById(R.id.btnSearch);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        expListView = findViewById(R.id.list_slidermenu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                )
        {
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        new Thread(() ->  getFacets()).start();

        btnSearch.setOnClickListener(searchListener);
        txtStorageNumber.setOnEditorActionListener(searchEnterListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
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

    private void getFacets() {
        FacetData[] facetData = WineSearchServices.GetWineFacets("de");

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (FacetData facet : facetData) {
            String headerText = Utils.GetHeaderForValue(facet.Field);
            listDataHeader.add(headerText);

            List<NavDrawerItem> menuElements = new ArrayList<>();

            for (Item item : facet.Items) {
                NavDrawerItem dataItem = new NavDrawerItem(item.Value, item.Count);
                menuElements.add(dataItem);
            }
            Collections.sort(menuElements, Comparator.comparing(a -> a.Name));

            if (headerText.equals(Constants.HEADER_TROPHY_YEAR)) {
                Collections.reverse(menuElements);
            }

            listDataChild.put(headerText, menuElements);
        }

        runOnUiThread(() -> {
                listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);
            }
        );
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
        boolean success = Utils.GetWineData(data);
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