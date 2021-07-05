package com.dwm.winesearchapp_extern.Pojo.response;

import com.dwm.winesearchapp_extern.Pojo.Facet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WineData {

    @SerializedName("SearchResult")
    public SearchResultData searchResult;

    @SerializedName("ExtendedFacets")
    public List<Facet> extendedFacets;

    @SerializedName("SolrQuery")
    public String solrQuery;
}
