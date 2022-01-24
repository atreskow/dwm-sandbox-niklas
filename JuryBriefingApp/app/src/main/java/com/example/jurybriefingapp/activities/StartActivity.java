package com.example.jurybriefingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jurybriefingapp.R;
import com.example.jurybriefingapp.Utils;
import com.example.jurybriefingapp.networking.RoomData;
import com.example.jurybriefingapp.ViewHelper;
import com.example.jurybriefingapp.networking.PresentationServices;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ViewHelper.ToggleLoadingAnimation(this, View.VISIBLE);

        new Thread(() ->  getRooms()).start();
    }

    private void getRooms() {
        List<RoomData> roomData = PresentationServices.GetRoomData(this);
        if (roomData != null && Utils.RoomListHasPresentation(roomData)) {
            Intent i  = new Intent(this, RoomActivity.class);
            i.putExtra("roomData", (Serializable) roomData);
            startActivity(i);
        }
        else {
            Intent i  = new Intent(this, WebViewActivity.class);
            startActivity(i);
        }
        ViewHelper.ToggleLoadingAnimation(this, View.GONE);
        ViewHelper.Finish(this);
    }
}
