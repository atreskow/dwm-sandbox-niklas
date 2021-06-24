package com.dwm.winesearchapp_extern.Pojo.request;

import java.util.ArrayList;
import java.util.List;

public class FacetQueryGroup {

    public FacetQueryGroup(String fieldName, String value) {
        FieldName = fieldName;
        Values.add(value);
    }
    public String FieldName;
    public List<String> Values = new ArrayList<>();
}
