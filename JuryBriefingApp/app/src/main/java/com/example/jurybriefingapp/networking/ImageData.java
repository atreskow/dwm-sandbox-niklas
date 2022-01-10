package com.example.jurybriefingapp.networking;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class ImageData {

    @SerializedName("ID")
    public UUID Id;

    @SerializedName("SLIDE_NUMBER")
    public int SlideNumber;

    @SerializedName("SLIDE_DATA")
    public String SlideData;

}
