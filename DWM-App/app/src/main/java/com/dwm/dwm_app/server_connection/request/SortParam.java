package com.dwm.dwm_app.server_connection.request;

public class SortParam {

    public SortParam(String fieldName, boolean orderAsc) {
        this.fieldName = fieldName;
        this.orderAsc = orderAsc;
    }
    public String fieldName;
    public boolean orderAsc;
}
