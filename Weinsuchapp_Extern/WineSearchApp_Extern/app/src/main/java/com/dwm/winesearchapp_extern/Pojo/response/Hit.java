package com.dwm.winesearchapp_extern.Pojo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hit {

    @SerializedName("Document")
    public DocumentData document;

    @SerializedName("Highlights")
    public List<String> highlights;

    @SerializedName("Score")
    public float score;
}
