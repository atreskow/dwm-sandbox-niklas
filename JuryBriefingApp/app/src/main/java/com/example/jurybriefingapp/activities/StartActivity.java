package com.example.jurybriefingapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jurybriefingapp.R;
import com.example.jurybriefingapp.UpdateButtonClickListener;
import com.example.jurybriefingapp.Utils;
import com.example.jurybriefingapp.networking.RoomData;
import com.example.jurybriefingapp.ViewHelper;
import com.example.jurybriefingapp.networking.PresentationServices;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import java.io.Serializable;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);

        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("https://dl.dropbox.com/s/zskko5ipvra5ac5/update-changelog.json?dl=0")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        if (isUpdateAvailable) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);

                            builder.setMessage(update.getReleaseNotes())
                                    .setTitle("Available update")
                                    .setNegativeButton("Update Now", new UpdateButtonClickListener(StartActivity.this, update.getUrlToDownload()))
                                    .setPositiveButton("Later", _dismissButtonCLickListener);

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else {
                            new Thread(() ->  getRooms()).start();
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                        new Thread(() ->  getRooms()).start();
                    }
                });

        appUpdaterUtils.start();
    }

    private DialogInterface.OnClickListener _dismissButtonCLickListener = (dialogInterface, i) -> new Thread(() ->  getRooms()).start();

    private void getRooms() {
        List<RoomData> roomData = PresentationServices.GetRoomsData(this);
        if (roomData != null && Utils.RoomListHasPresentation(roomData)) {
            if (roomData.size() == 1) {
                Intent i  = new Intent(this, PresentationActivity.class);
                i.putExtra("roomData", (Serializable) roomData.get(0));
                startActivity(i);
            }
            else {
                Intent i  = new Intent(this, RoomActivity.class);
                i.putExtra("roomData", (Serializable) roomData);
                startActivity(i);
            }
        }
        else {
            Intent i  = new Intent(this, WebViewActivity.class);
            startActivity(i);
        }
        ViewHelper.ToggleLoadingAnimation(this, View.GONE);
        ViewHelper.Finish(this);
    }
}
