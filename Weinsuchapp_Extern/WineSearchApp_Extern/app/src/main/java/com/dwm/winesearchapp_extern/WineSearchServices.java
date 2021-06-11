package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.FileData;
import com.dwm.winesearchapp_extern.Pojo.Document;
import com.dwm.winesearchapp_extern.Pojo.WineData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class WineSearchServices {

    private static final Gson gson = new Gson();

    public static WineData GetWineData(Activity activity, String lang) {
        JSONObject bodyData = null;
        try {
            bodyData = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String serviceUrl = ServiceLocator.SEARCH.replace("{lang}", lang);
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.PostData(serviceUrl, bodyData);
            WineData wineData = gson.fromJson(jsonObject.get("value").toString(), WineData.class);
            return wineData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetWineData(activity, lang);
            }
            else {
                return null;
            }
        }
    }


    private static boolean handleStatus(JsonObject jsonObject, Activity activity, String exception) {
        try {
            int status = Integer.parseInt(jsonObject.get("status").toString());
            switch (status) {
                case 200:
                    if (activity.getClass().getSimpleName().equals(Constants.getLoginActivityName())) {
                        ViewHelper.ShowToast(activity, "Benutzername oder Passwort ist nicht korrekt. Bitte überprüfen Sie die eingegebenen Daten.");
                    }
                    else {
                        ViewHelper.ShowToast(activity, "Es konnte kein Wein mit dieser Lagernummer gefunden werden.");
                    }
                    return false;
                case 403:
                    ViewHelper.ShowToast(activity, "Keine Zugangsrechte vorhanden");
                    return false;
                default:
                    ViewHelper.ShowToast(activity, "Ein Fehler ist aufgetreten. Fehlermeldung: " + exception);
                    return false;
            }
        }
        catch (NullPointerException e) {
            ViewHelper.ShowToast(activity, "Ein Fehler ist aufgetreten. Besteht eine Verbindung zum Internet?");
            return false;
        }
    }
}
