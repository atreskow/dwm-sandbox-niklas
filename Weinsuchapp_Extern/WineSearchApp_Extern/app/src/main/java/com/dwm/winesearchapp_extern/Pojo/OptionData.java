package com.dwm.winesearchapp_extern.Pojo;

public class OptionData {
    public int Top;
    public int Skip;
    public SortParam[] SortParams;
    public String[] ResultAttributes;
    public String[] Facets;
    public String[] HighlightFields;

}

class SortParam {
    public String FieldName;
    public boolean OrderAsc;
}
