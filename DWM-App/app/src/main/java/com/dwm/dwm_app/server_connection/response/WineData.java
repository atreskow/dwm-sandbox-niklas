package com.dwm.dwm_app.server_connection.response;

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