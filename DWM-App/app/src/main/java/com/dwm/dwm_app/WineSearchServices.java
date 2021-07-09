package com.dwm.dwm_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.dwm.dwm_app.Pojo.Facet;
import com.dwm.dwm_app.Pojo.request.WineSearchData;
import com.dwm.dwm_app.Pojo.response.FileData;
import com.dwm.dwm_app.Pojo.response.WineData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.UUID;

public class WineSearchServices {

    private static final Gson GSON = new Gson();

    public static WineData getWineData(String lang, WineSearchData bd) {
        JSONObject bodyData = Utils.parseObjectToJSONObject(bd);

        String serviceUrl = ServiceLocator.SEARCH.replace("{lang}", lang);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.postData(serviceUrl, bodyData);
            WineData wineData = GSON.fromJson(jsonObject.get("value").toString(), WineData.class);
            return wineData;
        } catch (Exception e) {
            return null;
        }
    }

    public static Facet[] getWineFacets(String lang) {
        JSONObject bodyData = null; //Utils.GetFacetOverview();

        String serviceUrl = ServiceLocator.FACETS.replace("{lang}", lang);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.postData(serviceUrl, bodyData);
            Facet[] facetData = GSON.fromJson(jsonObject.get("value").toString(), Facet[].class);
            return facetData;
        } catch (Exception e) {
            return null;
        }
    }

    public static String[] getBottleImageNames(UUID wineId) {
        String serviceUrl = ServiceLocator.GET_BOTTLE_IMAGE_NAMES
                .replace("{id}", wineId.toString());
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.getData(serviceUrl);
            String[] imageNameData = GSON.fromJson(jsonObject.get("value").toString(), String[].class);
            return imageNameData;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getBottleImage(UUID wineId, String imageName) {
        String serviceUrl = ServiceLocator.GET_BOTTLE_IMAGE
                .replace("{id}", wineId.toString())
                .replace("{name}", imageName);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.getData(serviceUrl);
            FileData imageData = GSON.fromJson(jsonObject.get("value").toString(), FileData.class);
            byte[] imageByteArray = Base64.decode(imageData.fileData, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getBottleImageType(WineListItem item, String imageSize, String imageType, String imageName) {
        String serviceUrl = ServiceLocator.GET_BOTTLE_IMAGE_TYPE
                .replace("{trophyIdent}", item.trophyCode)
                .replace("{storageNumber}", String.valueOf(item.storageNumber))
                .replace("{size}", imageSize)
                .replace("{wineshootName}", imageName)
                .replace("{imageType}", imageType);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.getData(serviceUrl);
            FileData imageData = GSON.fromJson(jsonObject.get("value").toString(), FileData.class);
            byte[] imageByteArray = Base64.decode(imageData.fileData, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getMedalImage(String trophyCode, int ranking) {
        String serviceUrl = ServiceLocator.GET_MEDAL_IMAGE
                .replace("{trophyPrefix}", trophyCode)
                .replace("{rank}", String.valueOf(ranking));
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.getData(serviceUrl);
            FileData imageData = GSON.fromJson(jsonObject.get("value").toString(), FileData.class);
            byte[] imageByteArray = Base64.decode(imageData.fileData, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }
}
