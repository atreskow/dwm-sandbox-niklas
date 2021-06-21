package com.dwm.winesearchapp_extern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.Facet;
import com.dwm.winesearchapp_extern.Pojo.Item;
import com.dwm.winesearchapp_extern.Pojo.request.OptionData;
import com.dwm.winesearchapp_extern.Pojo.request.QueryObjData;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.response.DocumentData;
import com.dwm.winesearchapp_extern.Pojo.response.Hit;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    EditText txtStorageNumber;
    Button btnSearch;

    DrawerLayout mDrawerLayout;

    ArrayList<String> listDataHeader;
    HashMap<String, List<NavDrawerItem>> listDataChild;
    List<WineListItem> wineListItems;

    ExpandableListView expListView;
    ExpandListAdapter listAdapter;

    ListView wineListView;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewHelper.SetupToolbar(this);

        txtStorageNumber = findViewById(R.id.txtStorageNumber);
        txtStorageNumber.setText(Session.GetWineName());
        btnSearch = findViewById(R.id.btnSearch);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        expListView = findViewById(R.id.list_slidermenu);
        wineListView = findViewById(R.id.wineList);

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

        btnSearch.setOnClickListener(searchListener);
        txtStorageNumber.setOnEditorActionListener(searchEnterListener);

        startWineSearchThread();
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

    public void startWineSearchThread() {
        new Thread(() ->  {
            WineData data = GetWineData();
            parseFacetData(data.ExtendedFacets);
        }).start();
    }

    public WineData GetWineData() {
        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);

        String wineName = txtStorageNumber.getText().toString();
        Session.SetWineName(wineName);

        QueryObjData queryObjData = Utils.GenerateQueryObjData();
        OptionData optionData = Utils.GenerateOptionData();
        WineSearchData data = new WineSearchData(queryObjData, optionData);

        WineData wineData = WineSearchServices.GetWineData("de", data);

        ViewHelper.ToggleLoadingAnimation(this, View.GONE);

        displayWineData(wineData);

        return wineData;
    }

    private void displayWineData(WineData wineData) {

        List<Hit> wineDataList = wineData.SearchResult.Hits;
        wineListItems = new ArrayList<>();

        for (Hit wine : wineDataList) {
            DocumentData document = wine.Document;
            WineListItem wineListItem = new WineListItem(document);
            wineListItems.add(wineListItem);
        }

        WineListAdapter adapter = new WineListAdapter(this, R.layout.wine_list_item);
        adapter.addAll(wineListItems);

        runOnUiThread(() ->
                wineListView.setAdapter(adapter)
        );
    }

    private void parseFacetData(List<Facet> facets) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (Facet facet : facets) {
            if (facet.Items.size() == 0) continue;

            String headerText = Utils.GetHeaderForValue(facet.Field);
            listDataHeader.add(headerText);

            List<NavDrawerItem> menuElements = new ArrayList<>();

            for (Item item : facet.Items) {
                NavDrawerItem dataItem = new NavDrawerItem(item.Value, facet.Field, item.Count);
                menuElements.add(dataItem);
            }
            Collections.sort(menuElements, Comparator.comparing(a -> a.Name));

            if (headerText.equals(Constants.HEADER_TROPHY_YEAR) || headerText.equals(Constants.HEADER_WINE_VINTAGE)) {
                Collections.reverse(menuElements);
            }

            Utils.TransferFacetTrues(facet.Field, menuElements);

            listDataChild.put(headerText, menuElements);
        }

        runOnUiThread(() -> {
            listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            mDrawerLayout.closeDrawers();
        });
    }

    View.OnClickListener searchListener = view -> {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtStorageNumber.getWindowToken(), 0);
        startWineSearchThread();
    };

    TextView.OnEditorActionListener searchEnterListener = (v, actionId, event) -> {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtStorageNumber.getWindowToken(), 0);
            startWineSearchThread();
            handled = true;
        }
        return handled;
    };
}