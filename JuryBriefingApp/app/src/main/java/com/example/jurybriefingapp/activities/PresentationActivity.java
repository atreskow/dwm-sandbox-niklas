package com.example.jurybriefingapp.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jurybriefingapp.Constants;
import com.example.jurybriefingapp.R;
import com.example.jurybriefingapp.Utils;
import com.example.jurybriefingapp.networking.DownloadData;
import com.example.jurybriefingapp.networking.RoomData;
import com.example.jurybriefingapp.networking.PresentationServices;
import com.example.jurybriefingapp.networking.ServiceLocator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipInputStream;

import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class PresentationActivity extends AppCompatActivity {

    public List<File> Slides = new ArrayList<>();
    private ImageView _slideView;
    private RoomData _roomData;

    private HubConnection mHubConnection;
    private HubProxy mHubProxy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        Intent i = getIntent();
        _roomData = (RoomData) i.getSerializableExtra("roomData");
        _slideView = findViewById(R.id.presentationSlide);

        setupSignalR();

        new Thread(() -> {
            boolean updateSlides = checkPresentationVersion();
            String root = this.getFilesDir().toString();
            File filedirectory = new File(root + "/" + Constants.SLIDES_DIR + "/");
            if(!filedirectory.exists()){
                filedirectory.mkdir();
            }

            if (!updateSlides || filedirectory.listFiles().length == 0) {
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

            updateSlide();
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateSlide() {
        runOnUiThread(() -> {
            _slideView.setImageBitmap(BitmapFactory.decodeFile(Slides.get(_roomData.Slide).getAbsolutePath()));
        });
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

    private void setupSignalR() {
        mHubConnection = new HubConnection(ServiceLocator.SIGNALR_URL);
        mHubProxy = mHubConnection.createHubProxy(ServiceLocator.SLIDES_HUB);
        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);


        mHubProxy.on("showSlide", (tastingRoomId, slideId) -> {
            if (tastingRoomId.equals(_roomData.Id)) {
                _roomData.Slide = slideId;
                updateSlide();
            }
        }, UUID.class, int.class);


        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }
    }
}