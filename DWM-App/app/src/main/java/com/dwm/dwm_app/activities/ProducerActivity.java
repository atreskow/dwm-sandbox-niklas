package com.dwm.dwm_app.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dwm.dwm_app.server_connection.request.FacetQueryGroup;
import com.dwm.dwm_app.server_connection.request.OptionData;
import com.dwm.dwm_app.server_connection.request.QueryObjData;
import com.dwm.dwm_app.server_connection.request.WineSearchData;
import com.dwm.dwm_app.server_connection.response.DocumentData;
import com.dwm.dwm_app.server_connection.response.Hit;
import com.dwm.dwm_app.server_connection.response.WineData;
import com.dwm.dwm_app.R;
import com.dwm.dwm_app.infrastructure.Session;
import com.dwm.dwm_app.infrastructure.Utils;
import com.dwm.dwm_app.infrastructure.ViewHelper;
import com.dwm.dwm_app.adapters.WineListAdapter;
import com.dwm.dwm_app.adapters.WineListItem;
import com.dwm.dwm_app.server_connection.WineSearchServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;


public class ProducerActivity extends AppCompatActivity {

    private TextView _producerNameTextView;
    private TextView _wineListSearchAmountTextView;

    private TextView _grandGoldCount;
    private TextView _goldCount;
    private TextView _silverCount;

    private WineListAdapter _wineListAdapter;
    private List<WineListItem> _wineItemList;
    private ListView _wineListView;
    private boolean _wineListScrollIsLoading = false;

    private List<FacetQueryGroup> facetQueryGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHelper.setTheme(this);
        setContentView(R.layout.activity_producer);
        ViewHelper.setupToolbar(this);

        boolean toolbarBottom = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("toolbarBottom", false);
        if (toolbarBottom) {
            ViewHelper.changeSearchPosition(this);
        }

        String producer = Session.getSelectedListItem().producer;
        facetQueryGroups.add(Utils.getRankedOnlyFacetGroup());
        facetQueryGroups.add(new FacetQueryGroup("producer_company", producer));
        List<String> medalValues = new ArrayList<>(Arrays.asList("Grand Gold", "Gold", "Silver", "No Selection = No Wine"));
        facetQueryGroups.add(new FacetQueryGroup("medal_name", medalValues));

        setupViews();
        setupListener();

        _producerNameTextView.setText(producer);

        startInitialWineSearch();
    }

    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void setupViews() {
        _producerNameTextView = findViewById(R.id.txtViewProducer);
        _wineListView = findViewById(R.id.wineList);
        _wineListSearchAmountTextView = findViewById(R.id.txtViewBottom);

        _grandGoldCount = findViewById(R.id.txtGrandGold_value);
        _goldCount = findViewById(R.id.txtGold_value);
        _silverCount = findViewById(R.id.txtSilver_value);
    }

    private void setupListener() {

        findViewById(R.id.layoutGrandGold).setOnClickListener(_grandGoldListener);
        findViewById(R.id.layoutGold).setOnClickListener(_goldListener);
        findViewById(R.id.layoutSilver).setOnClickListener(_silverListener);

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
                        new Thread(() -> startWineSearch()).start();
                    }
                }
            }
        });
    }

    public void startInitialWineSearch() {
        new Thread(() ->  {
            int totalHits = startWineSearch();
            getMedalAmount(totalHits);
        }).start();
    }

    public int startWineSearch() {
        ViewHelper.toggleLoadingAnimation(this, View.VISIBLE);

        WineData wineData = getWines();
        if (wineData == null) {
            ViewHelper.showToast(this, getResources().getString(R.string.internet_error));
            ViewHelper.toggleLoadingAnimation(this, View.GONE);
            return 0;
        }
        return addWines(wineData);
    }

    private WineData getWines() {
        QueryObjData queryObjData = Utils.generateQueryObjData(facetQueryGroups);
        OptionData optionData = Utils.generateOptionData(_wineListAdapter.getCount());
        WineSearchData wineSearchData = new WineSearchData(queryObjData, optionData);

        WineData wineData = WineSearchServices.getWineData(getResources().getString(R.string.language), wineSearchData);

        return wineData;
    }

    private int addWines(WineData wineData) {
        List<Hit> wineDataList = wineData.searchResult.hits;
        _wineItemList = new ArrayList<>();

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

        return wineData.searchResult.totalHits;
    }

    private void getMedalAmount(int totalHits) {
        QueryObjData queryObjData = Utils.generateQueryObjData(facetQueryGroups);
        OptionData optionData = Utils.generateOptionData(0, totalHits);
        WineSearchData wineSearchData = new WineSearchData(queryObjData, optionData);

        WineData wineData = WineSearchServices.getWineData(getResources().getString(R.string.language), wineSearchData);

        if (wineData == null) {
            ViewHelper.showToast(this, getResources().getString(R.string.internet_error));
            ViewHelper.toggleLoadingAnimation(this, View.GONE);
            return;
        }

        int grandGold = 0;
        int gold = 0;
        int silver = 0;

        List<Hit> wineDataList = wineData.searchResult.hits;

        for (Hit wine : wineDataList) {
            DocumentData documentData = wine.document;
            switch (documentData.ranking) {
                case 1: grandGold++;
                        break;
                case 2: gold++;
                        break;
                case 3: silver++;
                        break;
            }
        }

        //"lambda variables must be final" ........
        String finalGrandGold = String.valueOf(grandGold);
        String finalGold = String.valueOf(gold);
        String finalSilver = String.valueOf(silver);

        runOnUiThread(() -> {
            _grandGoldCount.setText(finalGrandGold);
            _goldCount.setText(finalGold);
            _silverCount.setText(finalSilver);

            ViewHelper.setVisibility(findViewById(R.id.layoutGrandGold), !finalGrandGold.equals("0"));
            ViewHelper.setVisibility(findViewById(R.id.layoutGold), !finalGold.equals("0"));
            ViewHelper.setVisibility(findViewById(R.id.layoutSilver), !finalSilver.equals("0"));
        });

        ViewHelper.toggleLoadingAnimation(this, View.GONE);
    }

    private void updateSearchBottomText() {
        String text = String.format(getResources().getString(R.string.winelist_bottomText), _wineListAdapter.getCount(), Session.getMaxWinesSearch());
        _wineListSearchAmountTextView.setText(text);
    }

    private final View.OnClickListener _grandGoldListener = view -> {
        CheckBox grandGoldCheckbox = findViewById(R.id.checkBoxGrandGold);
        checkBoxStatusChange(grandGoldCheckbox, "Grand Gold");
    };

    private final View.OnClickListener _goldListener = view -> {
        CheckBox goldCheckBox = findViewById(R.id.checkBoxGold);
        checkBoxStatusChange(goldCheckBox, "Gold");
    };

    private final View.OnClickListener _silverListener = view -> {
        CheckBox silverCheckBox = findViewById(R.id.checkBoxSilver);
        checkBoxStatusChange(silverCheckBox, "Silver");
    };

    private void checkBoxStatusChange(CheckBox checkBox, String value) {
        ViewHelper.toggleLoadingAnimation(this, View.VISIBLE);

        if (checkBox.isChecked()) {
            Utils.removeFacetQueryGroupValue(facetQueryGroups, "medal_name", value);
        }
        else {
            Utils.addFacetQueryGroupValue(facetQueryGroups, "medal_name", value);
        }
        checkBox.setChecked(!checkBox.isChecked());
        _wineListAdapter.clear();

        new Thread(() -> startWineSearch()).start();
    }
}