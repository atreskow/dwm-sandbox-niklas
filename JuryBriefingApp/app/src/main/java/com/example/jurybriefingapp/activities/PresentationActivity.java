package com.example.jurybriefingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jurybriefingapp.Constants;
import com.example.jurybriefingapp.R;
import com.example.jurybriefingapp.Session;
import com.example.jurybriefingapp.Utils;
import com.example.jurybriefingapp.ViewHelper;
import com.example.jurybriefingapp.networking.DownloadData;
import com.example.jurybriefingapp.networking.RoomData;
import com.example.jurybriefingapp.networking.PresentationServices;
import com.example.jurybriefingapp.networking.ServiceLocator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipInputStream;

import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class PresentationActivity extends AppCompatActivity {

    private List<File> _slides;
    private ImageView _slideView;
    private ProgressBar _progressBar;
    private RelativeLayout _progressLayout;
    private RoomData _roomData;

    private File _fileDirectory;

    private HubConnection _hubConnection;
    private HubProxy _hubProxy;
    private ClientTransport _clientTransport;
    private boolean _signalRConnected = true;

    private SharedPreferences _sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        Intent i = getIntent();
        _roomData = (RoomData) i.getSerializableExtra("roomData");
        _slideView = findViewById(R.id.presentationSlide);
        _progressBar = findViewById(R.id.progressBar);
        _progressLayout = findViewById(R.id.progressLayout);

        _sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String root = this.getFilesDir().toString();
        _fileDirectory = new File(root + "/" + Constants.SLIDES_DIR + "/");

        setupSignalR();

        new Thread(() -> {
            if(!_fileDirectory.exists()){
                _fileDirectory.mkdir();
            }

            if (!isUpToDateSlidesVersion() || _fileDirectory.listFiles().length == 0) {
                runSlidesProgressThread();
                getSlides();
            }

            addSlides();
            showSlideImage();
        }).start();

        runReconnectThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _signalRConnected = false;
        _hubConnection.disconnect();
    }

    private boolean isUpToDateSlidesVersion() {
        String slidesLocalVersion = _sharedPref.getString(Constants.SLIDES_VERSION_TITLE, "");
        String slidesServerVersion = PresentationServices.GetSlidesChecksum(this);

        if (slidesLocalVersion.equals(slidesServerVersion)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void runReconnectThread() {
        new Thread(() -> {
            while (_signalRConnected) {
                if (_hubConnection.getState() == ConnectionState.Disconnected) {
                    runOnUiThread(() -> {
                        ViewHelper.ShowToast(this, "Disconnected... Reconnecting");
                    });

                    SignalRFuture<Void> signalRFuture = _hubConnection.start(_clientTransport);
                    try {
                        signalRFuture.get();

                        //Nach Disconnect k??nnte es sein, dass Events nicht angekommen sind (anderer Slide, Pr??sentation fertig, andere Pr??sentation)
                        _roomData = PresentationServices.GetRoomData(this, _roomData.Id);
                        if (_roomData.Status == 1) {
                            startWebViewActivity();
                        }
                        else if (!isUpToDateSlidesVersion()) {
                            getSlides();
                            addSlides();
                            showSlideImage();
                        }
                        else {
                            showSlideImage();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void runSlidesProgressThread() {
        Session.FileSize = 1;
        Session.Loaded = 0;
        runOnUiThread(() -> {
            _progressLayout.setVisibility(View.VISIBLE);
        });
        new Thread(() -> {
            while (Session.Loaded < Session.FileSize) {
                int percent = Math.round((float) Session.Loaded / Session.FileSize * 100.0f);
                runOnUiThread(() -> {
                    _progressBar.setProgress(percent);
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                _progressLayout.setVisibility(View.GONE);
            });
        }).start();
    }

    private void showSlideImage() {
        runOnUiThread(() -> {
            try {
                _slideView.setImageBitmap(BitmapFactory.decodeFile(_slides.get(_roomData.Slide).getAbsolutePath()));
            }
            catch (IndexOutOfBoundsException e) { //Falls Folien ge??ndert werden k??nnte m??glicherweise eine Folie aus der Liste gefordert werden, die nicht existiert
                ViewHelper.ShowToast(this, "Fehler beim Ausw??hlen des Pr??sentationsslides");
            }
        });
    }

    private void getSlides() {
        String slidesVersion = PresentationServices.GetSlidesChecksum(this);

        DownloadData data = PresentationServices.GetJurySlideZip(this);
        ZipInputStream stream = Utils.Base64ToZipStream(data.FileData);
        try {
            Utils.DeleteFilesInDirectory(_fileDirectory);
            Utils.UnzipStream(stream, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor _editor = _sharedPref.edit();
        _editor.putString(Constants.SLIDES_VERSION_TITLE, slidesVersion);
        _editor.commit();
    }

    private void addSlides() {
        _slides = new ArrayList<>();
        _slides.addAll(Arrays.asList(_fileDirectory.listFiles()));
        Collections.sort(_slides, (f1, f2) -> {
            int i1 = Integer.parseInt(f1.getName().split("\\.")[0]);
            int i2 = Integer.parseInt(f2.getName().split("\\.")[0]);
            return Integer.compare(i1, i2);
        });
    }

    private void setupSignalR() {
        _hubConnection = new HubConnection(ServiceLocator.BASE_URL + ServiceLocator.SIGNALR);
        _hubProxy = _hubConnection.createHubProxy(ServiceLocator.SLIDES_HUB);
        _clientTransport = new ServerSentEventsTransport(_hubConnection.getLogger());

        SignalRFuture<Void> signalRFuture = _hubConnection.start(_clientTransport);

        setupEvents();

        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setupEvents() {
        _hubProxy.on("showSlide", (tastingRoomId, slideId) -> {
            if (tastingRoomId.equals(_roomData.Id)) {
                _roomData.Slide = slideId;
                showSlideImage();
            }
        }, UUID.class, int.class);

        _hubProxy.on("goToStartPage", (tastingRoomId) -> {
            if (tastingRoomId.equals(_roomData.Id)) {
                startWebViewActivity();
            }
        }, UUID.class);

        _hubProxy.on("onJuryBriefingSlidesUploaded", () -> {
            new Thread(() -> {
                getSlides();
                addSlides();
                showSlideImage();
            }).start();
        });

        _hubProxy.on("ping", () -> {
            runOnUiThread(() -> {
                ViewHelper.ShowToast(this, "Ping");
            });
        });
    }

    private void startWebViewActivity() {
        Intent intent  = new Intent(this, WebViewActivity.class);
        startActivity(intent);
        ViewHelper.Finish(this);
        _signalRConnected = false;
        _hubConnection.disconnect();
    }
}
