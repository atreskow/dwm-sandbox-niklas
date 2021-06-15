package com.dwm.winesearchapp_extern.Pojo.request;

public class SortParam {

    public SortParam(String fieldName, boolean orderAsc) {
        FieldName = fieldName;
        OrderAsc = orderAsc;
    }
    public String FieldName;
    public boolean OrderAsc;
}
