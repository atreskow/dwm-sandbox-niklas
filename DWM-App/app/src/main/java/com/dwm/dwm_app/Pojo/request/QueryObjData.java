package com.dwm.dwm_app.Pojo.request;

import java.util.List;

public class QueryObjData {

    public QueryObjData(List<String> queryTokens, List<FacetQueryGroup> facetQueryGroup) {
        this.queryTokens = queryTokens;
        facetQueryGroups = facetQueryGroup;
    }

    public List<String> queryTokens;
    public List<FacetQueryGroup> facetQueryGroups;
}
