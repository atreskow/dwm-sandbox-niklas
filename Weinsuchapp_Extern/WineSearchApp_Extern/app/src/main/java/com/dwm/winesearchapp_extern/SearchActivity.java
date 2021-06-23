package com.dwm.winesearchapp_extern;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    EditText txtStorageNumber;
    Button btnSearch;
    Button btnReset;

    DrawerLayout mDrawerLayout;

    ArrayList<String> listDataHeader;
    HashMap<String, List<NavDrawerItem>> listDataChild;
    List<WineListItem> wineListItems;

    ExpandableListView expListView;
    ExpandListAdapter listAdapter;

    WineListAdapter wineListAdapter;
    ListView wineListView;
    TextView bottomText;

    ActionBarDrawerToggle toggle;

    boolean flag_loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewHelper.SetupToolbar(this);

        txtStorageNumber = findViewById(R.id.txtStorageNumber);
        txtStorageNumber.setText(Session.GetWineName());
        btnSearch = findViewById(R.id.btnSearch);
        btnReset = findViewById(R.id.btnReset);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        expListView = findViewById(R.id.list_slidermenu);
        wineListView = findViewById(R.id.wineList);
        bottomText = findViewById(R.id.txtViewBottom);

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
        btnReset.setOnClickListener(resetFilterListener);
        txtStorageNumber.setOnEditorActionListener(searchEnterListener);

        wineListAdapter = new WineListAdapter(this, R.layout.wine_list_item);
        wineListView.setAdapter(wineListAdapter);
        wineListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(!flag_loading && !Session.AllWinesLoaded(wineListAdapter.getCount()))
                    {
                        flag_loading = true;
                        new Thread(() ->  addWines()).start();
                    }
                }
            }
        });

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
        wineListAdapter.clear();
        String wineName = txtStorageNumber.getText().toString();
        Session.SetWineName(wineName);
        new Thread(() ->  {
            WineData data = addWines();
            parseFacetData(data.ExtendedFacets);
        }).start();
    }

    private WineData addWines() {
        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);

        QueryObjData queryObjData = Utils.GenerateQueryObjData();
        OptionData optionData = Utils.GenerateOptionData(wineListAdapter.getCount());
        WineSearchData data = new WineSearchData(queryObjData, optionData);

        WineData wineData = WineSearchServices.GetWineData(getResources().getString(R.string.language), data);

        List<Hit> wineDataList = wineData.SearchResult.Hits;
        wineListItems = new ArrayList<>();

        if (wineListAdapter.getCount() == 0) {
            Session.SetMaxWinesSearch(wineData.SearchResult.TotalHits);
        }

        for (Hit wine : wineDataList) {
            DocumentData document = wine.Document;
            WineListItem wineListItem = new WineListItem(document);
            wineListItems.add(wineListItem);
        }

        runOnUiThread(() -> {
            wineListAdapter.addAll(wineListItems);
            wineListAdapter.notifyDataSetChanged();
            updateSearchedAmount();
            ViewHelper.ToggleLoadingAnimation(this, View.GONE);
        });

        flag_loading = false;

        return wineData;
    }

    private void updateSearchedAmount() {
        String text = String.format(getResources().getString(R.string.winelist_bottomText), wineListAdapter.getCount(), Session.GetMaxWinesSearch());
        bottomText.setText(text);
    }

    private void parseFacetData(List<Facet> facets) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (Facet facet : facets) {

            String headerText = Utils.GetHeaderForValue(this, facet.Field);
            listDataHeader.add(headerText);

            List<NavDrawerItem> menuElements = new ArrayList<>();

            for (Item item : facet.Items) {
                if (Utils.IsBlacklistedFacet(item.Value)) continue;
                NavDrawerItem dataItem = new NavDrawerItem(item.Value, facet.Field, item.Count);
                menuElements.add(dataItem);
            }
            if (menuElements.size() == 0) {
                listDataHeader.remove(headerText);
                continue;
            }

            Collections.sort(menuElements, Comparator.comparing(a -> a.Name));

            if (headerText.equals(getResources().getString(R.string.header_trophy_year)) || headerText.equals(getResources().getString(R.string.header_wine_vintage))) {
                Collections.reverse(menuElements);
            }

            menuElements = Utils.TransferFacetTrues(facet.Field, menuElements);

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

    View.OnClickListener resetFilterListener = view -> {
        Session.SetFacetQueryGroups(new ArrayList<>());
        Session.SetWineName("");
        txtStorageNumber.setText("");
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