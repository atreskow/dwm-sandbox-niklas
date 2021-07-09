package com.dwm.winesearchapp_extern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

    private EditText _wineNameTextView;
    private TextView _wineListSearchAmountTextView;
    private Button _searchButton;
    private Button _resetButton;
    private ImageView _settingsImageView;

    private ActionBarDrawerToggle _actionBarDrawerToggle;
    private DrawerLayout _drawerLayout;
    private ExpandableListView _expandableListView;
    private NavDrawerAdapter _navDrawerAdapter;
    private ArrayList<String> _facetHeaderList;
    private HashMap<String, List<NavDrawerItem>> _facetHeaderChildMap;

    private WineListAdapter _wineListAdapter;
    private List<WineListItem> _wineItemList;
    private ListView _wineListView;
    private boolean _wineListScrollIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHelper.setTheme(this);
        setContentView(R.layout.activity_search);
        ViewHelper.setupToolbar(this);

        if (Session.getFacetQueryGroups().size() == 0) {
            Session.getFacetQueryGroups().add(Utils.getRankedOnlyFacetGroup());
        }

        boolean toolbarBottom = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("toolbarBottom", false);
        if (toolbarBottom) {
            ViewHelper.changeSearchPosition(this);
            ViewHelper.changeDrawerGravity(this);
        }

        setupViews();
        setupActionBar();
        setupListener();

        startNewWineSearch();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (_actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        _wineNameTextView = findViewById(R.id.txtStorageNumber);
        _wineNameTextView.setText(Session.getWineName());
        _searchButton = findViewById(R.id.btnSearch);
        _resetButton = findViewById(R.id.btnReset);
        _drawerLayout = findViewById(R.id.drawer_layout);
        _expandableListView = findViewById(R.id.list_slidermenu);
        _wineListView = findViewById(R.id.wineList);
        _wineListSearchAmountTextView = findViewById(R.id.txtViewBottom);
        _settingsImageView = findViewById(R.id.settings);
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        _actionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        _drawerLayout,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                )
        {
        };
        _drawerLayout.addDrawerListener(_actionBarDrawerToggle);
        _actionBarDrawerToggle.syncState();
    }

    private void setupListener() {
        _searchButton.setOnClickListener(_searchListener);
        _resetButton.setOnClickListener(_resetFilterListener);
        _wineNameTextView.setOnEditorActionListener(_searchEnterListener);
        _settingsImageView.setOnClickListener(_settingsListener);

        _wineListAdapter = new WineListAdapter(this, R.layout.wine_list_item);
        _wineListView.setAdapter(_wineListAdapter);
        _wineListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(!_wineListScrollIsLoading && !Session.allWinesLoaded(_wineListAdapter.getCount()))
                    {
                        _wineListScrollIsLoading = true;
                        new Thread(() ->  addWines()).start();
                    }
                }
            }
        });
    }

    public void startNewWineSearch() {
        _wineListAdapter.clear();
        String wineName = _wineNameTextView.getText().toString();
        Session.setWineName(wineName);

        new Thread(() ->  {
            WineData data = addWines();
            if (data == null) {
                ViewHelper.showToast(this, getResources().getString(R.string.internet_error));
                ViewHelper.toggleLoadingAnimation(this, View.GONE);
                return;
            }
            addFacetsToSidebar(data.extendedFacets);
        }).start();
    }

    //Die Funtkion wird für neue SUchen und zum Erweitern der Wein-Such-Liste aufgerufen
    private WineData addWines() {
        ViewHelper.toggleLoadingAnimation(this, View.VISIBLE);

        QueryObjData queryObjData = Utils.generateQueryObjData();
        OptionData optionData = Utils.generateOptionData(_wineListAdapter.getCount());
        WineSearchData wineSearchData = new WineSearchData(queryObjData, optionData);
        WineData wineData = WineSearchServices.getWineData(getResources().getString(R.string.language), wineSearchData);

        if (wineData == null) {
            return null;
        }

        List<Hit> wineDataList = wineData.searchResult.hits;
        _wineItemList = new ArrayList<>();

        //Setzt bei neuer Anfrage die Anzahl der Ergebnisse neu
        if (_wineListAdapter.getCount() == 0) {
            Session.setMaxWinesSearch(wineData.searchResult.totalHits);
        }

        for (Hit wine : wineDataList) {
            DocumentData documentData = wine.document;
            WineListItem wineListItem = new WineListItem(documentData);
            _wineItemList.add(wineListItem);
        }

        runOnUiThread(() -> {
            _wineListAdapter.addAll(_wineItemList);
            _wineListAdapter.notifyDataSetChanged();
            updateSearchBottomText();
            ViewHelper.toggleLoadingAnimation(this, View.GONE);
        });
        _wineListScrollIsLoading = false;
        return wineData;
    }

    private void updateSearchBottomText() {
        String text = String.format(getResources().getString(R.string.winelist_bottomText), _wineListAdapter.getCount(), Session.getMaxWinesSearch());
        _wineListSearchAmountTextView.setText(text);
    }

    private void addFacetsToSidebar(List<Facet> facets) {
        _facetHeaderList = new ArrayList<>();
        _facetHeaderChildMap = new HashMap<>();

        for (Facet facet : facets) {
            String headerText = Utils.getHeaderForValue(this, facet.field);
            _facetHeaderList.add(headerText);

            List<NavDrawerItem> menuElements = new ArrayList<>();
            for (Item item : facet.items) {
                if (Utils.isBlacklistedFacet(item.value)) {
                    continue;
                }
                NavDrawerItem dataItem = new NavDrawerItem(item.value, facet.field, item.count);
                menuElements.add(dataItem);
            }

            //Wenn eine Kategorie keine Kinder mehr beinhaltet wird die Kategorie entfernt
            if (menuElements.size() == 0) {
                _facetHeaderList.remove(headerText);
                continue;
            }

            Collections.sort(menuElements, Comparator.comparing(a -> a.name));
            if (headerText.equals(getResources().getString(R.string.header_trophy_year)) || headerText.equals(getResources().getString(R.string.header_wine_vintage))) {
                Collections.reverse(menuElements);
            }

            //Übergibt die booleans der vorherigen Facet Liste
            menuElements = Utils.transferFacetTrues(facet.field, menuElements);

            _facetHeaderChildMap.put(headerText, menuElements);
        }

        runOnUiThread(() -> {
            _resetButton.setVisibility(ViewHelper.getResetButtonVisbility(_wineNameTextView.getText().toString()));
            _navDrawerAdapter = new NavDrawerAdapter(this, _facetHeaderList, _facetHeaderChildMap);
            _expandableListView.setAdapter(_navDrawerAdapter);
            _drawerLayout.closeDrawers();
        });
    }

    private final View.OnClickListener _resetFilterListener = view -> {
        Session.setFacetQueryGroups(new ArrayList<>());
        Session.getFacetQueryGroups().add(Utils.getRankedOnlyFacetGroup());
        Session.setWineName("");
        _wineNameTextView.setText("");
        startNewWineSearch();
    };

    private final View.OnClickListener _searchListener = view -> {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_wineNameTextView.getWindowToken(), 0);
        startNewWineSearch();
    };

    private final View.OnClickListener _settingsListener = view -> {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    };

    private final TextView.OnEditorActionListener _searchEnterListener = (v, actionId, event) -> {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_wineNameTextView.getWindowToken(), 0);
            startNewWineSearch();
            handled = true;
        }
        return handled;
    };
}