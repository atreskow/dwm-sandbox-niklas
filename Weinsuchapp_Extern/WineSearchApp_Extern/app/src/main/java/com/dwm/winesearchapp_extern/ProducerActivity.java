package com.dwm.winesearchapp_extern;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.dwm.winesearchapp_extern.Pojo.request.OptionData;
import com.dwm.winesearchapp_extern.Pojo.request.QueryObjData;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.response.DocumentData;
import com.dwm.winesearchapp_extern.Pojo.response.Hit;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;


public class ProducerActivity extends AppCompatActivity {

    private TextView _producerNameTextView;
    private TextView _wineListSearchAmountTextView;

    private WineListAdapter _wineListAdapter;
    private List<WineListItem> _wineItemList;
    private ListView _wineListView;
    private boolean _wineListScrollIsLoading = false;

    private String _producer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHelper.setTheme(this);
        setContentView(R.layout.activity_producer);
        ViewHelper.setupToolbar(this);

        boolean toolbarBottom = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("toolbarBottom", false);
        if (toolbarBottom) {
            ViewHelper.changeSearchPosition(this);
            ViewHelper.changeDrawerGravity(this);
        }

        Intent intent = getIntent();
        _producer = Session.getSelectedListItem().producer;

        setupViews();
        setupListener();

        _producerNameTextView.setText(_producer);

        startWineSearch(_producer);
    }

    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void setupViews() {
        _producerNameTextView = findViewById(R.id.txtViewProducer);
        _wineListView = findViewById(R.id.wineList);
        _wineListSearchAmountTextView = findViewById(R.id.txtViewBottom);
    }

    private void setupListener() {

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
                        startWineSearch(_producer);
                    }
                }
            }
        });
    }

    public void startWineSearch(String producer) {
        ViewHelper.toggleLoadingAnimation(this, View.VISIBLE);

        QueryObjData queryObjData = Utils.generateQueryObjData(producer);
        OptionData optionData = Utils.generateOptionData(_wineListAdapter.getCount());
        WineSearchData wineSearchData = new WineSearchData(queryObjData, optionData);

        new Thread(() ->  {
            WineData wineData = WineSearchServices.getWineData(getResources().getString(R.string.language), wineSearchData);
            if (wineData == null) {
                ViewHelper.showToast(this, getResources().getString(R.string.internet_error));
                ViewHelper.toggleLoadingAnimation(this, View.GONE);
                return;
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
        }).start();
    }

    private void updateSearchBottomText() {
        String text = String.format(getResources().getString(R.string.winelist_bottomText), _wineListAdapter.getCount(), Session.getMaxWinesSearch());
        _wineListSearchAmountTextView.setText(text);
    }
}