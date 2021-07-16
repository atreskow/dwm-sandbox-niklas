package com.dwm.dwm_app.server_connection.response;

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
