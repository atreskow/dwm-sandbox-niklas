package com.dwm.winesearchapp_extern.Pojo.request;

public class WineSearchData {

    public WineSearchData(QueryObjData queryObj, OptionData optionData) {
        QueryObj = queryObj;
        Options = optionData;
    }

    public QueryObjData QueryObj;
    public OptionData Options;
}
