package com.dwm.winesearchapp_extern.Pojo.request;

public class OptionData {

    public OptionData (int top, int skip, SortParam[] sortParams, String[] resultAttributes, String[] facets, String[] highlightFields) {
        Top = top;
        Skip = skip;
        SortParams = sortParams;
        ResultAttributes = resultAttributes;
        Facets = facets;
        HighlightFields = highlightFields;
    }

    public int Top;
    public int Skip;
    public SortParam[] SortParams;
    public String[] ResultAttributes;
    public String[] Facets;
    public String[] HighlightFields;

}
