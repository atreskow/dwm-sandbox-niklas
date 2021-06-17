package com.dwm.winesearchapp_extern;

import android.app.Activity;

import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.request.OptionData;
import com.dwm.winesearchapp_extern.Pojo.request.QueryObjData;
import com.dwm.winesearchapp_extern.Pojo.request.SortParam;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class Utils {
    public static WineData GetWineData(WineSearchData data) {
        String lang = "de";
        WineData winedata = WineSearchServices.GetWineData(lang, data);
        return winedata;
    }

    public static <T> JSONObject ParseObjectToJSONObject(T object) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        JSONObject bodyData = null;

        try {
            String jsonString = gson.toJson(object);
            bodyData = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyData;
    }

    public static JSONObject GetFacetOverview() {
        String jsonString = "{\n" +
                "   \"FacetFields\":[\n" +
                "      \"trophy_name\",\n" +
                "      \"trophy_year\",\n" +
                "      \"wine_flavour\",\n" +
                "      \"wine_type\",\n" +
                "      \"wine_vinification\",\n" +
                "      \"wine_category\",\n" +
                "      \"cultivation_country\",\n" +
                "      \"medal_name\",\n" +
                "      \"is_bio\",\n" +
                "      \"wine_vintage\",\n" +
                "      \"varietal\"\n" +
                "   ],\n" +
                "   \"MinCount\":1,\n" +
                "   \"Limit\":100\n" +
                "}";

        JSONObject bodyData = null;

        try {
            bodyData = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyData;
    }

    public static QueryObjData GenerateQueryObjData() {
        String[] queryTokens = new String[] { "+wine_name:*" + Session.GetWineName() + "*" };
        QueryObjData queryObjData = new QueryObjData(queryTokens, Session.GetFacetQueryGroupsArray());

        return queryObjData;
    }

    public static OptionData GenerateOptionData() {
        SortParam[] sortParams = new SortParam[] {
                new SortParam("trophy_name", false)
        };
        OptionData optionData = new OptionData(Session.GetWinesPerPage(),
                Session.GetPage() * Session.GetWinesPerPage(),
                sortParams,
                null,
                Constants.FacetValues,
                null);
        return optionData;
    }

    public static String GetHeaderForValue(String value) {
        switch (value) {
            case "trophy_name": return Constants.HEADER_TROPHY_NAME;
            case "trophy_year": return Constants.HEADER_TROPHY_YEAR;
            case "medal_name": return Constants.HEADER_MEDAL_NAME;
            case "wine_vintage": return Constants.HEADER_WINE_VINTAGE;
            case "wine_category": return Constants.HEADER_WINE_CATEGORY;
            case "wine_type": return Constants.HEADER_WINE_TYPE;
            case "wine_flavour": return Constants.HEADER_WINE_FLAVOUR;
            case "wine_vinification": return Constants.HEADER_WINE_VINIFICATION;
            case "is_bio": return Constants.HEADER_IS_BIO;
            case "cultivation_country": return Constants.HEADER_CULTIVATION_COUNTRY;
            case "varietal": return Constants.HEADER_WINE_VARIETAL;
            default: return "???";
        }
    }

}
