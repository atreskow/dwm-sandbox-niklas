package com.dwm.winesearchapp_extern.Pojo.request;

public class WineSearchData {

    public WineSearchData(QueryObjData queryObj, OptionData optionData) {
        this.queryObj = queryObj;
        options = optionData;
    }

    public QueryObjData queryObj;
    public OptionData options;
}
