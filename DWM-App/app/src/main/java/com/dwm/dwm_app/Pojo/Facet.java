package com.dwm.dwm_app.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Facet {

    @SerializedName("Field")
    public String field;

    @SerializedName("Items")
    public List<Item> items;
}
