package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.dwm.winesearchapp_extern.Pojo.Facet;
import com.dwm.winesearchapp_extern.Pojo.request.WineSearchData;
import com.dwm.winesearchapp_extern.Pojo.response.FileData;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.UUID;

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

    public static String[] GetBottleImageNames(UUID wineId) {
        String serviceUrl = ServiceLocator.GET_BOTTLE_IMAGE_NAMES
                .replace("{id}", wineId.toString());
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            String[] imageNameData = gson.fromJson(jsonObject.get("value").toString(), String[].class);
            return imageNameData;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap GetBottleImage(UUID wineId, String imageName) {
        String serviceUrl = ServiceLocator.GET_BOTTLE_IMAGE
                .replace("{id}", wineId.toString())
                .replace("{name}", imageName);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            FileData imageData = gson.fromJson(jsonObject.get("value").toString(), FileData.class);
            byte[] imageByteArray = Base64.decode(imageData.FileData, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap GetBottleImageType(WineListItem item, String imageSize, String imageType, String imageName) {
        String serviceUrl = ServiceLocator.GET_BOTTLE_IMAGE_TYPE
                .replace("{trophyIdent}", item.TrophyCode)
                .replace("{storageNumber}", String.valueOf(item.StorageNumber))
                .replace("{size}", imageSize)
                .replace("{wineshootName}", imageName)
                .replace("{imageType}", imageType);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            FileData imageData = gson.fromJson(jsonObject.get("value").toString(), FileData.class);
            byte[] imageByteArray = Base64.decode(imageData.FileData, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap GetMedalImage(String trophyCode, int ranking) {
        String serviceUrl = ServiceLocator.GET_MEDAL_IMAGE
                .replace("{trophyPrefix}", trophyCode)
                .replace("{rank}", String.valueOf(ranking));
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            FileData imageData = gson.fromJson(jsonObject.get("value").toString(), FileData.class);
            byte[] imageByteArray = Base64.decode(imageData.FileData, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }
}
