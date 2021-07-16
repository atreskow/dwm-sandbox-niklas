
package com.dwm.dwm_app.server_connection.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultData {

    @SerializedName("QueryTokenHits")
    public List<String> queryTokenHits;

    @SerializedName("Hits")
    public List<Hit> hits;

    @SerializedName("TotalHits")
    public int totalHits;

    @SerializedName("Facets")
    public List<Facet> facets;
}
