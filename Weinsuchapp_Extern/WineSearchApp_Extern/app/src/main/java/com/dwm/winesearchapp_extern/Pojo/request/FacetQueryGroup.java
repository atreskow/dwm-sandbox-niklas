package com.dwm.winesearchapp_extern.Pojo.request;

public class FacetQueryGroup {

    public FacetQueryGroup(String fieldName, String[] values) {
        FieldName = fieldName;
        Values = values;
    }
    public String FieldName;
    public String[] Values;
}
