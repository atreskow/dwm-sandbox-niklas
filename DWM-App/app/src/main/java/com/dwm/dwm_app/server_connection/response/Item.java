package com.dwm.dwm_app.server_connection.response;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("Value")
    public String value;

    @SerializedName("Count")
    public int count;
}
