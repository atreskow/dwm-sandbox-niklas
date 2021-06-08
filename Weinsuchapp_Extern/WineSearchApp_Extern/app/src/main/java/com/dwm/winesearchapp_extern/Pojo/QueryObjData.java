package com.dwm.winesearchapp_extern.Pojo;

public class QueryObjData {
    public String[] QueryTokens;
    public FacetQueryGroup[] FacetQueryGroups;
}

class FacetQueryGroup {
    public String FieldName;
    public String[] Values;
}
