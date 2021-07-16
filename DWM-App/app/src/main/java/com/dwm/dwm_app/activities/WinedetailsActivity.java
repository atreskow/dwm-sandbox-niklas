package com.dwm.dwm_app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dwm.dwm_app.adapters.BottleImagePagerAdapter;
import com.dwm.dwm_app.adapters.HeightWrappingViewPager;
import com.dwm.dwm_app.R;
import com.dwm.dwm_app.infrastructure.Session;
import com.dwm.dwm_app.infrastructure.Utils;
import com.dwm.dwm_app.infrastructure.ViewHelper;
import com.dwm.dwm_app.adapters.WineListItem;
import com.dwm.dwm_app.server_connection.WineSearchServices;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.tabs.TabLayout;

import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

public class WinedetailsActivity extends AppCompatActivity {

    private BottleImagePagerAdapter _bottleImagePagerAdapter;
    private HeightWrappingViewPager _viewPager;
    private LinearLayout _descriptionLayout;

    private TextView _descriptionHeader;
    private TextView _producerHeader;
    private TextView _wineRegionHeader;
    private TextView _grapeInformationHeader;
    private TextView _availabilityHeader;

    private Button _shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHelper.setTheme(this);
        setContentView(R.layout.activity_winedetails);
        ViewHelper.setupToolbar(this);

        _viewPager = findViewById(R.id.imagePager);
        _descriptionLayout = findViewById(R.id.descriptionLayout);

        _descriptionHeader = findViewById(R.id.descriptionHeader);
        _descriptionHeader.setOnClickListener(_descriptionListener);

        _producerHeader = findViewById(R.id.producerHeader);
        _producerHeader.setOnClickListener(_producerListener);

        _wineRegionHeader = findViewById(R.id.wineRegionHeader);
        _wineRegionHeader.setOnClickListener(_wineRegionListener);

        _grapeInformationHeader = findViewById(R.id.grapeInformationHeader);
        _grapeInformationHeader.setOnClickListener(_grapeInformationListener);

        _availabilityHeader = findViewById(R.id.availabilityHeader);
        _availabilityHeader.setOnClickListener(_availabilityListener);

        _shareButton = findViewById(R.id.btnShare);
        _shareButton.setOnClickListener(_shareButtonListener);

        findViewById(R.id.layoutProducer).setOnClickListener(_producerListListener);

        WineListItem wine = Session.getSelectedListItem();
        addData(wine);

        new Thread(() ->  getImages(wine)).start();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public Intent getParentActivityIntent() {
        Intent parentIntent= getIntent();
        String className = parentIntent.getStringExtra("ParentClassSource");

        if (className == null) {
            className = getPackageName() + "." + "SearchActivity";
        }

        Intent newIntent=null;
        try {
            //you need to define the class with package name
            newIntent = new Intent(this, Class.forName(className));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newIntent;
    }

    private void getImages(WineListItem wine) {
        String[] imageNames = WineSearchServices.getBottleImageNames(wine.id);

        if (Utils.hasAward(wine.ranking)) {
            Bitmap medalImage = WineSearchServices.getMedalImage(wine.trophyCode, wine.ranking);
            runOnUiThread(() -> ((ImageView) findViewById(R.id.imageMedal)).setImageBitmap(medalImage));
        }
        else {
            runOnUiThread(() -> ((ImageView) findViewById(R.id.imageMedal)).setVisibility(View.GONE));
        }

        if (imageNames != null && imageNames.length != 0) {
            Bitmap[] bottleImages = new Bitmap[2];
            for (String imageName : imageNames) {
                Bitmap bottleImage = WineSearchServices.getBottleImageType(wine, "big", "png", imageName);
                if (imageName.endsWith("F")) {
                    bottleImages[0] = bottleImage;
                }
                else {
                    bottleImages[1] = bottleImage;
                }
            }
            _bottleImagePagerAdapter = new BottleImagePagerAdapter(this, bottleImages);

            runOnUiThread(() -> {
                _viewPager.setAdapter(_bottleImagePagerAdapter);
                _viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
                tabLayout.setupWithViewPager(_viewPager, true);
            });
        }
        else {
            runOnUiThread(() -> {
                findViewById(R.id.tabDots).setVisibility(View.GONE);
                _viewPager.setVisibility(View.GONE);
            });
        }

    }

    private void addData(WineListItem wine) {

        if (wine.award != null) {
            String[] trophySplit = wine.award.split(" - ");

            ViewHelper.setValue(R.id.txtTrophy_value, trophySplit[1], this);
            ViewHelper.setValue(R.id.txtMedalName_value, trophySplit[0], this);
        }

        ViewHelper.setValue(R.id.txtName_value, wine.wineName, this);
        ViewHelper.setValue(R.id.txtYear_value, wine.vintage, this);
        ViewHelper.setValue(R.id.txtProducer_value, wine.producer, this);
        ViewHelper.setValue(R.id.txtCountry_value, wine.country, this);
        ViewHelper.setValue(R.id.txtRegion_value, wine.region, this);
        ViewHelper.setValue(R.id.txtCategory_value, wine.category, this);
        ViewHelper.setValue(R.id.txtFlavour_value, wine.flavour, this);
        ViewHelper.setValue(R.id.txtType_value, wine.type, this);
        ViewHelper.setValue(R.id.txtVinification_value, wine.vinification, this);
        ViewHelper.setValue(R.id.txtOrganic_value, wine.organic, this);
        ViewHelper.setValue(R.id.txtSugar_value, wine.sugar, "g/l", this);
        ViewHelper.setValue(R.id.txtAcidity_value, wine.acidity, "g/l", this);
        ViewHelper.setValue(R.id.txtSulfur_value, wine.sulfur, "mg/l", this);
        ViewHelper.setValue(R.id.txtAlcohol_value, wine.alcohol, "%", this);

        String varietalText = wine.varietal.size() > 1 ? getResources().getString(R.string.varietals) : getResources().getString(R.string.varietal);
        ViewHelper.setValue(R.id.txtVarietal, varietalText, this);
        ViewHelper.setValue(R.id.txtVarietal_value, wine.varietal.stream().collect(Collectors.joining(", ")),this);

        if (wine.region == null || wine.region.isEmpty()) {
            findViewById(R.id.txtRegion).setVisibility(View.GONE);
            findViewById(R.id.txtRegion_value).setVisibility(View.GONE);
        }
    }

    private void openCloseDescription(int headerRes, int textRes) {
        _descriptionHeader.setTextColor(MaterialColors.getColor(this, R.attr.textColor, Color.BLACK));
        _producerHeader.setTextColor(MaterialColors.getColor(this, R.attr.textColor, Color.BLACK));
        _wineRegionHeader.setTextColor(MaterialColors.getColor(this, R.attr.textColor, Color.BLACK));
        _grapeInformationHeader.setTextColor(MaterialColors.getColor(this, R.attr.textColor, Color.BLACK));
        _availabilityHeader.setTextColor(MaterialColors.getColor(this, R.attr.textColor, Color.BLACK));

        findViewById(R.id.descriptionText).setVisibility(View.GONE);
        findViewById(R.id.producerText).setVisibility(View.GONE);
        findViewById(R.id.wineRegionText).setVisibility(View.GONE);
        findViewById(R.id.grapeInformationText).setVisibility(View.GONE);
        findViewById(R.id.availabilityText).setVisibility(View.GONE);

        TextView header = findViewById(headerRes);
        TextView text = findViewById(textRes);

        if (text.getVisibility() == View.GONE) {
            text.setVisibility(View.VISIBLE);
            header.setTextColor(getResources().getColor(R.color.buttonPrimary, null));
        }
        else {
            text.setVisibility(View.GONE);
            header.setTextColor(MaterialColors.getColor(this, R.attr.textColor, Color.BLACK));
        }
    }

    private final View.OnClickListener _descriptionListener = view -> {
        openCloseDescription(R.id.descriptionHeader, R.id.descriptionText);
    };

    private final View.OnClickListener _producerListener = view -> {
        openCloseDescription(R.id.producerHeader, R.id.producerText);
    };

    private final View.OnClickListener _wineRegionListener = view -> {
        openCloseDescription(R.id.wineRegionHeader, R.id.wineRegionText);
    };

    private final View.OnClickListener _grapeInformationListener = view -> {
        openCloseDescription(R.id.grapeInformationHeader, R.id.grapeInformationText);
    };

    private final View.OnClickListener _availabilityListener = view -> {
        openCloseDescription(R.id.availabilityHeader, R.id.availabilityText);
    };

    private final View.OnClickListener _producerListListener = view -> {
        Intent intent = new Intent(this, ProducerActivity.class);
        startActivity(intent);
    };

    private final View.OnClickListener _shareButtonListener = view -> {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Utils.getWineLink(Session.getSelectedListItem().wineLink));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share)));
    };
}