package com.dwm.winesearchapp_extern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox _darkmodeCheckbox;
    private CheckBox _toolbarCheckbox;
    private CheckBox _medalCheckbox;

    private SharedPreferences _sharedPref;
    private SharedPreferences.Editor _editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHelper.setTheme(this);
        setContentView(R.layout.activity_settings);
        ViewHelper.setupToolbar(this);

        _sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        _editor = _sharedPref.edit();

        _darkmodeCheckbox = findViewById(R.id.darkmode_checkbox);
        _toolbarCheckbox = findViewById(R.id.toolbar_checkbox);
        _medalCheckbox = findViewById(R.id.medal_checkbox);

        _darkmodeCheckbox.setChecked(_sharedPref.getBoolean("darkmodeEnabled", false));
        _toolbarCheckbox.setChecked(_sharedPref.getBoolean("toolbarBottom", false));
        _medalCheckbox.setChecked(_sharedPref.getBoolean("medalPreview", true));

        _darkmodeCheckbox.setOnCheckedChangeListener(_darkmodeListener);
        _toolbarCheckbox.setOnCheckedChangeListener(_toolbarListener);
        _medalCheckbox.setOnCheckedChangeListener(_medalListener);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void reload() {
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        getApplicationContext().startActivity(i);
        ViewHelper.finish(this);
    }

    private final CompoundButton.OnCheckedChangeListener _darkmodeListener = (radioGroup, isChecked) -> {
        _editor.putBoolean("darkmodeEnabled", isChecked);
        _editor.commit();
        reload();
    };

    private final CompoundButton.OnCheckedChangeListener _toolbarListener = (radioGroup, isChecked) -> {
        _editor.putBoolean("toolbarBottom", isChecked);
        _editor.commit();
        reload();
    };

    private final CompoundButton.OnCheckedChangeListener _medalListener = (radioGroup, isChecked) -> {
        _editor.putBoolean("medalPreview", isChecked);
        _editor.commit();
        reload();
    };
}
