package com.dwm.winesearchapp_extern;

import android.app.Activity;

import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;
import com.google.gson.Gson;

import org.json.JSONObject;

public class Utils {
    public static boolean GetWineData(Activity activity, WineSearchData data) {
        String lang = "de";
        WineData winedata = WineSearchServices.GetWineData(activity, lang, data);
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

    public static String[] SetQueryTokens(String wineName, int fromYear, int toYear) {
        String [] queryTokens = new String[] {
                        "+wine_name:*" + wineName + "*",
                        "+wine_vintage:[ " + fromYear + " TO " + toYear + "]"
                };
        return queryTokens;
    }

}
