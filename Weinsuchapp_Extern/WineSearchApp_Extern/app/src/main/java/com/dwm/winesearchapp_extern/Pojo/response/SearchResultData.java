
package com.dwm.winesearchapp_extern.Pojo.response;

import com.dwm.winesearchapp_extern.Pojo.Facet;

import java.util.List;

public class SearchResultData {
    public List<String> QueryTokenHits;
    public List<Hit> Hits;
    public int TotalHits;
    public List<Facet> Facets;
}
