package com.dwm.winesearchapp_extern;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.google.android.material.tabs.TabLayout;

import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

public class WinedetailsActivity extends AppCompatActivity {

    private BottleImagePagerAdapter _bottleImagePagerAdapter;
    private HeightWrappingViewPager _viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winedetails);
        ViewHelper.SetupToolbar(this);
        _viewPager = findViewById(R.id.imagePager);

        WineListItem wine = Session.GetSelectedListItem();

        addData(wine);
        new Thread(() ->  getImages(wine)).start();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void getImages(WineListItem wine) {
        String[] imageNames = WineSearchServices.GetBottleImageNames(wine.Id);

        if (Utils.HasAward(wine.Ranking)) {
            Bitmap medalImage = WineSearchServices.GetMedalImage(wine.TrophyCode, wine.Ranking);
            runOnUiThread(() -> ((ImageView) findViewById(R.id.imageMedal)).setImageBitmap(medalImage));
        }
        else {
            runOnUiThread(() -> ((ImageView) findViewById(R.id.imageMedal)).setVisibility(View.GONE));
        }

        if (imageNames.length != 0) {
            Bitmap[] bottleImages = new Bitmap[2];
            for (String imageName : imageNames) {
                Bitmap bottleImage = WineSearchServices.GetBottleImageType(wine, "big", "png", imageName);
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

        if (wine.Award != null) {
            String[] trophySplit = wine.Award.split(" - ");

            ViewHelper.SetValue(R.id.txtTrophy_value, trophySplit[1], this);
            ViewHelper.SetValue(R.id.txtMedalName_value, trophySplit[0], this);
        }

        ViewHelper.SetValue(R.id.txtName_value, wine.WineName, this);
        ViewHelper.SetValue(R.id.txtYear_value, wine.Vintage, this);
        ViewHelper.SetValue(R.id.txtProducer_value, wine.Producer, this);
        ViewHelper.SetValue(R.id.txtCountry_value, wine.Country, this);
        ViewHelper.SetValue(R.id.txtRegion_value, wine.Region, this);
        ViewHelper.SetValue(R.id.txtCategory_value, wine.Category, this);
        ViewHelper.SetValue(R.id.txtFlavour_value, wine.Flavour, this);
        ViewHelper.SetValue(R.id.txtType_value, wine.Type, this);
        ViewHelper.SetValue(R.id.txtVinification_value, wine.Vinification, this);
        ViewHelper.SetValue(R.id.txtOrganic_value, wine.Organic, this);
        ViewHelper.SetValue(R.id.txtSugar_value, wine.Sugar, "g/l", this);
        ViewHelper.SetValue(R.id.txtAcidity_value, wine.Acidity, "g/l", this);
        ViewHelper.SetValue(R.id.txtSulfur_value, wine.Sulfur, "mg/l", this);
        ViewHelper.SetValue(R.id.txtAlcohol_value, wine.Alcohol, "%", this);

        String varietalText = wine.Varietal.size() > 1 ? getResources().getString(R.string.varietals) : getResources().getString(R.string.varietal);
        ViewHelper.SetValue(R.id.txtVarietal, varietalText, this);
        ViewHelper.SetValue(R.id.txtVarietal_value, wine.Varietal.stream().collect(Collectors.joining(", ")),this);

        if (wine.Region == null || wine.Region.isEmpty()) {
            findViewById(R.id.txtRegion).setVisibility(View.GONE);
            findViewById(R.id.txtRegion_value).setVisibility(View.GONE);
        }
    }
}