package com.dwm.winesearchapp_extern;

import com.dwm.winesearchapp_extern.Pojo.Facet;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
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

    public static Facet[] GetWineFacets(String lang) {
        JSONObject bodyData = null; //Utils.GetFacetOverview();

        String serviceUrl = ServiceLocator.FACETS.replace("{lang}", lang);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.PostData(serviceUrl, bodyData);
            Facet[] facetData = gson.fromJson(jsonObject.get("value").toString(), Facet[].class);
            return facetData;
        } catch (Exception e) {
            return null;
        }
    }
}
