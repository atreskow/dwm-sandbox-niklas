package com.example.jurybriefingapp.networking;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class RoomData implements Serializable {
    @SerializedName("ID")
    public UUID Id;

    @SerializedName("ROOM_NUMBER")
    public int RoomNumber;

    @SerializedName("STATUS")
    public int Status; //0 -> Präsentation, 1 -> Verkostung

    @SerializedName("SLIDE")
    public int Slide;
}
