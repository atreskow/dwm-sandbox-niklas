package com.dwm.winesearchapp_extern.Pojo.request;

import java.util.List;

public class FacetQueryGroup {

    public FacetQueryGroup(String fieldName, List<String> values) {
        FieldName = fieldName;
        Values = values;
    }
    public String FieldName;
    public List<String> Values;
}
