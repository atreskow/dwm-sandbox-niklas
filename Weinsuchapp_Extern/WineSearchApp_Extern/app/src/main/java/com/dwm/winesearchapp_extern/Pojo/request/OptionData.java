package com.dwm.winesearchapp_extern.Pojo.request;

import java.util.List;

public class OptionData {

    public OptionData (int top, int skip, List<SortParam> sortParams, List<String> resultAttributes, List<String> facets, List<String> highlightFields) {
        Top = top;
        Skip = skip;
        SortParams = sortParams;
        ResultAttributes = resultAttributes;
        Facets = facets;
        HighlightFields = highlightFields;
    }

    public int Top;
    public int Skip;
    public List<SortParam> SortParams;
    public List<String> ResultAttributes;
    public List<String> Facets;
    public List<String> HighlightFields;

}
