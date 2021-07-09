package com.dwm.dwm_app.Pojo.request;

import java.util.ArrayList;
import java.util.List;

public class FacetQueryGroup {

    public FacetQueryGroup(String fieldName, String value) {
        this.fieldName = fieldName;
        values.add(value);
    }

    public String fieldName;
    public List<String> values = new ArrayList<>();
}
