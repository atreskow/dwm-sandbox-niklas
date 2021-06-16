package com.dwm.winesearchapp_extern;

import android.app.Activity;

import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.response.FacetData;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class WineSearchServices {

    private static final Gson gson = new Gson();

    public static WineData GetWineData(String lang, WineSearchData bd) {
        JSONObject bodyData = Utils.ParseObjectToJSONObject(bd);

        String serviceUrl = ServiceLocator.SEARCH.replace("{lang}", lang);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.PostData(serviceUrl, bodyData);
            WineData wineData = gson.fromJson(jsonObject.get("value").toString(), WineData.class);
            return wineData;
        } catch (Exception e) {
            return null;
        }
    }

    public static FacetData[] GetWineFacets(String lang) {
        JSONObject bodyData = Utils.GetFacetOverview();

        String serviceUrl = ServiceLocator.FACETS.replace("{lang}", lang);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.PostData(serviceUrl, bodyData);
            FacetData[] facetData = gson.fromJson(jsonObject.get("value").toString(), FacetData[].class);
            return facetData;
        } catch (Exception e) {
            return null;
        }
    }
}
