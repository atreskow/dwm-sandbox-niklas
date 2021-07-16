package com.dwm.dwm_app.server_connection.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Facet {

    @SerializedName("Field")
    public String field;

    @SerializedName("Items")
    public List<Item> items;
}
