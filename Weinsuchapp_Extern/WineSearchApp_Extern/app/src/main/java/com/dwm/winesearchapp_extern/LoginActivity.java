package com.dwm.winesearchapp_extern;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dwm.winesearchapp_extern.Pojo.Constants;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsername;
    EditText txtPassword;
    Button btnLogin;

    boolean timeout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Constants.setLoginActivityName(this.getClass().getSimpleName());
        timeout = getIntent().getBooleanExtra("TIMEOUT", false);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(loginListener);
    }

    View.OnClickListener loginListener = view ->
    {
        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);
        new Thread(() ->  login()).start();
    };

    private void login() {
        boolean success = WineSearchServices.Login(this, txtUsername.getText().toString(), txtPassword.getText().toString());
        if (!success) {
            ViewHelper.ToggleLoadingAnimation(this, View.GONE);
            return;
        }

        if (timeout)  { //Wenn zuvor eingelogt und auf Grund von timeout relog, wird nach refreshen des Tokens die vorherige Activity wieder aufgerufen
            ViewHelper.Finish(this);
            ViewHelper.ToggleLoadingAnimation(this, View.GONE);
            return;
        }

        TrophyData[] trophyData = WineSearchServices.GetTrophyData(this);
        Utils.ParseTrophyData(trophyData);
        Intent i  = new Intent(this, SearchActivity.class);
        startActivity(i);
        ViewHelper.ToggleLoadingAnimation(this, View.GONE);
    }
}