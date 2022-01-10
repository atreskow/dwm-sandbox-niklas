package com.example.jurybriefingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jurybriefingapp.R;
import com.example.jurybriefingapp.networking.RoomData;

import java.io.Serializable;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent i = getIntent();
        List<RoomData> roomData = (List<RoomData>) i.getSerializableExtra("roomData");
        LinearLayout layout = findViewById(R.id.linearlayout);

        for (RoomData room : roomData) {
            Button button = new Button(this);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
            params.setMargins(0,0,0,75);
            button.setLayoutParams(params);
            button.setText(String.valueOf(room.RoomNumber));
            button.setOnClickListener(view -> {
                Intent intent  = new Intent(this, PresentationActivity.class);
                intent.putExtra("roomData", room);
                startActivity(intent);
            });

            layout.addView(button);
        }
    }
}
