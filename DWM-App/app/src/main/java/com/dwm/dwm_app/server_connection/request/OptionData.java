package com.dwm.dwm_app.server_connection.request;

import java.util.List;

public class OptionData {

    public OptionData (int top, int skip, List<SortParam> sortParams, List<String> resultAttributes, List<String> facets, List<String> highlightFields) {
        this.top = top;
        this.skip = skip;
        this.sortParams = sortParams;
        this.resultAttributes = resultAttributes;
        this.facets = facets;
        this.highlightFields = highlightFields;
    }

    public int top;
    public int skip;
    public List<SortParam> sortParams;
    public List<String> resultAttributes;
    public List<String> facets;
    public List<String> highlightFields;

}
