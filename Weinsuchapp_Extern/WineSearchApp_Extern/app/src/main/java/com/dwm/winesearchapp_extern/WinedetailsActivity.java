package com.dwm.winesearchapp_extern;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WinedetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winedetails);
        ViewHelper.SetupToolbar(this);
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
}