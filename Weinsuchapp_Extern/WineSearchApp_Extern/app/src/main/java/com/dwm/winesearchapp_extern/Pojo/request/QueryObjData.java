package com.dwm.winesearchapp_extern.Pojo.request;

public class QueryObjData {

    public QueryObjData(String[] queryTokens, FacetQueryGroup[] facetQueryGroup) {
        QueryTokens = queryTokens;
        FacetQueryGroups = facetQueryGroup;
    }

    public String[] QueryTokens;
    public FacetQueryGroup[] FacetQueryGroups;
}
