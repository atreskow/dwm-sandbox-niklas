package com.example.jurybriefingapp.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jurybriefingapp.Constants;
import com.example.jurybriefingapp.R;
import com.example.jurybriefingapp.Utils;
import com.example.jurybriefingapp.networking.DownloadData;
import com.example.jurybriefingapp.networking.RoomData;
import com.example.jurybriefingapp.networking.PresentationServices;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipInputStream;

public class PresentationActivity extends AppCompatActivity {

    public List<File> Slides = new ArrayList<>();
    public ImageView slideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        slideView = findViewById(R.id.presentationSlide);

        Intent i = getIntent();
        RoomData roomData = (RoomData) i.getSerializableExtra("roomData");

        new Thread(() -> {
            boolean updateSlides = checkPresentationVersion();
            String root = this.getFilesDir().toString();
            File filedirectory = new File(root + "/" + Constants.SLIDES_DIR + "/");
            if(!filedirectory.exists()){
                filedirectory.mkdir();
            }

            if (updateSlides || filedirectory.listFiles().length == 0) {
                DownloadData data = PresentationServices.GetJurySlideZip(this);
                ZipInputStream stream = Utils.Base64ToZipStream(data.FileData);
                try {
                    Utils.UnzipStream(stream, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Slides.addAll(Arrays.asList(filedirectory.listFiles()));
            Slides.sort((f1, f2) -> {
                int i1 = Integer.parseInt(f1.getName().split("\\.")[0]);
                int i2 = Integer.parseInt(f2.getName().split("\\.")[0]);
                return Integer.compare(i1, i2);
            });

            runOnUiThread(() -> {
                slideView.setImageBitmap(BitmapFactory.decodeFile(Slides.get(roomData.Slide).getAbsolutePath()));
            });
        }).start();
    }

    private boolean checkPresentationVersion() {
        //Ask Server for Version
        if ("Local Version".equals("Server Version")) {
            return false;
        }
        else {
            return true;
        }
    }

}
