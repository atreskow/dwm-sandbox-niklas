package com.dwm.winesearchapp_extern;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwm.winesearchapp_extern.Pojo.Document;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;

public class WinedetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winedetails);
        ViewHelper.SetupToolbar(this);

        findViewById(R.id.dropGroupGeneral).setOnClickListener(toggleGeneral);
        findViewById(R.id.dropGroupIngredients).setOnClickListener(toggleIngredients);
        findViewById(R.id.dropGroupClient).setOnClickListener(toggleClient);
        findViewById(R.id.dropGroupManagement).setOnClickListener(toggleManagement);

        findViewById(R.id.headerBtnLogout).setOnClickListener(ViewHelper.LogoutListener(this));
        findViewById(R.id.headerBtnLeft).setOnClickListener(wineLeft);
        findViewById(R.id.headerBtnRight).setOnClickListener(wineRight);



        addData();
        new Thread(() ->  getImages()).start();
    }

    private void getImages() {
        String[] imageNames = WineSearchServices.GetBottleImageNames(this, Session.getCurrentWine().Id);

        for (String imageName : imageNames) {
            Bitmap bottleImage = WineSearchServices.GetBottleImage(this, Session.getCurrentWine().Id, imageName);
            if (imageName.endsWith("F")) {
                runOnUiThread(() -> ((ImageView) findViewById(R.id.imageFront)).setImageBitmap(bottleImage));
            } else {
                runOnUiThread(() -> ((ImageView) findViewById(R.id.imageBack)).setImageBitmap(bottleImage));
            }
        }

        if (Session.getCurrentWine().Ranking > 0 && Session.getCurrentWine().Ranking < 4) {
            Bitmap medalImage = WineSearchServices.GetMedalImage(this, Session.getCurrentWine().TrophyCode, Session.getCurrentWine().Ranking);
            runOnUiThread(() -> ((ImageView) findViewById(R.id.imageMedal)).setImageBitmap(medalImage));
        }
    }

    private void addData() {
        Document wine = Session.getCurrentWine();
        ViewHelper.SetValue(R.id.headerTxtStorageNumber, wine.StorageNumber, this);

        ViewHelper.SetValue(R.id.txtName_value, wine.Name, this);
        ViewHelper.SetValue(R.id.txtInfo_value, wine.AdditionalInfo, this);
        ViewHelper.SetValue(R.id.txtYear_value, wine.Year, this);
        ViewHelper.SetValue(R.id.txtCountry_value, wine.Country, this);
        ViewHelper.SetValue(R.id.txtArea_value, wine.Area, this);
        ViewHelper.SetValue(R.id.txtRegion_value, wine.Region, this);
        ViewHelper.SetValue(R.id.txtCategory_value, wine.WineCategory, this);
        ViewHelper.SetValue(R.id.txtFlavour_value, wine.Flavour, this);
        ViewHelper.SetValue(R.id.txtQuality_value, wine.Quality, this);
        ViewHelper.SetValue(R.id.txtType_value, wine.Type, this);
        ViewHelper.SetValue(R.id.txtVinification_value, wine.Vinification, this);
        ViewHelper.SetValue(R.id.txtVolume_value, wine.BottleVolume, this);
        ViewHelper.SetValue(R.id.txtOrganic_value, wine.Organic, this);
        ViewHelper.SetValue(R.id.txtUnfiltered_value, wine.Unfiltered, this);
        ViewHelper.SetValue(R.id.txtSample_value, wine.BarrelSample, this);
        ViewHelper.SetValue(R.id.txtSugar_value, wine.Sugar, "g/l", this);
        ViewHelper.SetValue(R.id.txtAcidity_value, wine.Acidity, "g/l", this);
        ViewHelper.SetValue(R.id.txtSulfur_value, wine.Sulfur, "mg/l", this);
        ViewHelper.SetValue(R.id.txtDensity_value, wine.Density, this);
        ViewHelper.SetValue(R.id.txtCarbondioxide_value, wine.Carbondioxide,"mg/l", this);
        ViewHelper.SetValue(R.id.txtBottleamount_value, wine.BottleAmount,this);
        ViewHelper.SetValue(R.id.txtLiteramount_value, wine.LiterAmount,this);
        ViewHelper.SetValue(R.id.txtPrice_value, wine.Price, "€", this);
        ViewHelper.SetValue(R.id.txtAlcohol_value, wine.Alcohol, "%", this);
        ViewHelper.SetValue(R.id.txtPresenter_value, wine.PresenterCompany, this);
        ViewHelper.SetValue(R.id.txtClient_value, wine.ClientCompany, this);
        ViewHelper.SetValue(R.id.txtProducer_value, wine.ProducerCompany, this);
        ViewHelper.SetValue(R.id.txtImporteur_value, wine.ImportCompany, this);
        ViewHelper.SetValue(R.id.txtClientnumber_value, wine.ClientNumber, this);
        ViewHelper.SetValue(R.id.txtStoragenumber_value, wine.StorageNumber, this);
        ViewHelper.SetValue(R.id.txtAction_value, wine.ActionWine, this);
        ViewHelper.SetValue(R.id.txtImported_value, wine.Imported, this);
        ViewHelper.SetValue(R.id.txtBarcode_value, wine.Barcode, this);
        ViewHelper.SetValue(R.id.txtCreated_value, wine.DateCreated.split("T")[0], this);
        ViewHelper.SetValue(R.id.txtRanking_value, wine.Ranking, this);
        ViewHelper.SetValue(R.id.txtResult_value, wine.Result, this);
        ViewHelper.SetValue(R.id.txtDeleted_value, wine.Deleted, this);
        ViewHelper.SetValue(R.id.txtComment_value, wine.InternalComment, this);

        LinearLayout ingredientLayout = findViewById(R.id.groupIngredients);
        if (Session.getWineVarietals().length == 1) {
            LinearLayout layout = createVarietalView("Rebsorte", Session.getWineVarietals()[0].Name);
            ingredientLayout.addView(layout);
        }
        else {
            for (int i = 0; i < Session.getWineVarietals().length; i++) {
                LinearLayout layoutName = createVarietalView("Rebsorte " + (i+1), Session.getWineVarietals()[i].Name + " (" + Session.getWineVarietals()[i].Percentage + "%)");
                ingredientLayout.addView(layoutName);
            }
        }

    }

    private LinearLayout createVarietalView(String name, String value) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
        layout.setLayoutParams(params);

        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        TextView txtName = new TextView(this);
        txtName.setLayoutParams(txtParams);
        txtName.setPadding(15,0,0,0);
        txtName.setText(name);
        layout.addView(txtName);

        TextView txtValue = new TextView(this);
        txtValue.setLayoutParams(txtParams);
        txtValue.setText(value);
        layout.addView(txtValue);

        return layout;
    }

    View.OnClickListener toggleGeneral = view -> {
        LinearLayout layout = findViewById(R.id.groupGeneral);
        ImageView image = findViewById(R.id.dropSymbolGeneral);

        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.baseline_arrow_drop_down_24);
        }
        else {
            layout.setVisibility(View.GONE);
            image.setImageResource(R.drawable.baseline_arrow_left_24);
        }
    };

    View.OnClickListener toggleIngredients = view -> {
        LinearLayout layout = findViewById(R.id.groupIngredients);
        ImageView image = findViewById(R.id.dropSymbolIngredients);

        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.baseline_arrow_drop_down_24);
        }
        else {
            layout.setVisibility(View.GONE);
            image.setImageResource(R.drawable.baseline_arrow_left_24);
        }
    };

    View.OnClickListener toggleClient = view -> {
        LinearLayout layout = findViewById(R.id.groupClient);
        ImageView image = findViewById(R.id.dropSymbolClient);

        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.baseline_arrow_drop_down_24);
        }
        else {
            layout.setVisibility(View.GONE);
            image.setImageResource(R.drawable.baseline_arrow_left_24);
        }
    };

    View.OnClickListener toggleManagement = view -> {
        LinearLayout layout = findViewById(R.id.groupManagement);
        ImageView image = findViewById(R.id.dropSymbolManagement);


        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.baseline_arrow_drop_down_24);
        }
        else {
            layout.setVisibility(View.GONE);
            image.setImageResource(R.drawable.baseline_arrow_left_24);
        }
    };

    private void changeWine(int storageNumber) {
        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);
        UUID trophyId = Session.getCurrentWine().TrophyId;
        boolean success = Utils.GetWineData(this, trophyId, storageNumber);
        if (success) {
            Intent i = new Intent(getApplicationContext(), WinedetailsActivity.class);
            getApplicationContext().startActivity(i);
            ViewHelper.Finish(this);
        }
        ViewHelper.ToggleLoadingAnimation(this, View.GONE);
    }

    View.OnClickListener wineLeft = view -> {
        int storageNumber = Session.getCurrentWine().StorageNumber - 1;
        new Thread(() ->  changeWine(storageNumber)).start();
    };

    View.OnClickListener wineRight = view -> {
        int storageNumber = Session.getCurrentWine().StorageNumber + 1;
        new Thread(() ->  changeWine(storageNumber)).start();
    };
}