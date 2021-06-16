package com.dwm.winesearchapp_extern;

import android.app.Activity;

import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;
import com.google.gson.Gson;

import org.json.JSONObject;

public class Utils {
    public static boolean GetWineData(WineSearchData data) {
        String lang = "de";
        WineData winedata = WineSearchServices.GetWineData(lang, data);
        return true;
    }

    public static <T> JSONObject ParseObjectToJSONObject(T object) {
        Gson gson = new Gson();
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
                "      \"medal_name\"\n" +
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

    public static String[] SetQueryTokens(String wineName, int fromYear, int toYear) {
        String [] queryTokens = new String[] {
                        "+wine_name:*" + wineName + "*",
                        "+wine_vintage:[ " + fromYear + " TO " + toYear + "]"
                };
        return queryTokens;
    }

    public static String GetHeaderForValue(String value) {
        switch (value) {
            case "trophy_name": return Constants.HEADER_TROPHY_NAME;
            case "trophy_year": return Constants.HEADER_TROPHY_YEAR;
            case "wine_flavour": return Constants.HEADER_WINE_FLAVOUR;
            case "wine_type": return Constants.HEADER_WINE_TYPE;
            case "wine_vinification": return Constants.HEADER_WINE_VINIFICATION;
            case "wine_category": return Constants.HEADER_WINE_CATEGORY;
            case "cultivation_country": return Constants.HEADER_CULTIVATION_COUNTRY;
            case "medal_name": return Constants.HEADER_MEDAL_NAME;
            default: return "???";
        }
    }

}
