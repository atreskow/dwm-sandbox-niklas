package com.dwm.dwm_app.Pojo.request;

public class WineSearchData {

    public WineSearchData(QueryObjData queryObj, OptionData optionData) {
        this.queryObj = queryObj;
        options = optionData;
    }

    public QueryObjData queryObj;
    public OptionData options;
}
