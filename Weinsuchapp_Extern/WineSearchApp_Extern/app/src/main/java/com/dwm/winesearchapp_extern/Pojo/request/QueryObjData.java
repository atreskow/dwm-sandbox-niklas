package com.dwm.winesearchapp_extern.Pojo.request;

import java.util.List;

public class QueryObjData {

    public QueryObjData(List<String> queryTokens, List<FacetQueryGroup> facetQueryGroup) {
        QueryTokens = queryTokens;
        FacetQueryGroups = facetQueryGroup;
    }

    public List<String> QueryTokens;
    public List<FacetQueryGroup> FacetQueryGroups;
}
