package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.FileData;
import com.dwm.winesearchapp_extern.Pojo.Document;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class WineSearchServices {

    private static final Gson gson = new Gson();

    public static boolean Login(Activity activity, String username, String pw) {
        JSONObject bodyData = null;
        try {
            bodyData = new JSONObject();
            bodyData.put("app", "WineSearchApp");
            bodyData.put("userName", username);
            bodyData.put("Password", pw);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.PostData(ServiceLocator.CREATE_TOKEN, bodyData);
            LoginData loginData = gson.fromJson(jsonObject.get("value").toString(), LoginData.class);
            Session.setId(loginData.Id);
            Session.setToken(loginData.Token);
            return true;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return Login(activity, username, pw);
            }
            else {
                return false;
            }
        }
    }

    public static TrophyData[] GetTrophyData(Activity activity) {
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(ServiceLocator.GET_TROPHY_DATA);
            TrophyData[] trophyData = gson.fromJson(jsonObject.get("value").toString(), TrophyData[].class);
            return trophyData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetTrophyData(activity);
            }
            else {
                return null;
            }
        }
    }

    public static boolean RefreshToken() {
        JSONObject bodyData = null;
        try {
            bodyData = new JSONObject();
            bodyData.put("Id", Session.getId());
            bodyData.put("Token", Session.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JsonObject jsonObject = ServerConnectionMethods.PostData(ServiceLocator.REFRESH_TOKEN, bodyData);
            LoginData loginData = gson.fromJson(jsonObject.get("value").toString(), LoginData.class);

            Session.setId(loginData.Id);
            Session.setToken(loginData.Token);
            return true;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public static UUID FindProbenpassId(Activity activity, UUID trophyId, int storageNumber) {
        String serviceUrl = ServiceLocator.FIND_PROBENPASS
                                .replace("{trophyId}", trophyId.toString())
                                .replace("{storageNumber}", String.valueOf(storageNumber));
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            UUID[] probenpassIds = gson.fromJson(jsonObject.get("value").toString(), UUID[].class);
            return probenpassIds[0];
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return FindProbenpassId(activity, trophyId, storageNumber);
            }
            else {
                return null;
            }
        }
    }

    public static Document GetWineData(Activity activity, UUID wineId) {
        String serviceUrl = ServiceLocator.GET_WINEDATA
                .replace("{id}", wineId.toString());
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            Document document = gson.fromJson(jsonObject.get("value").toString(), Document.class);
            return document;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetWineData(activity, wineId);
            }
            else {
                return null;
            }
        }
    }

    public static VarietalData[] GetWineVarietalData(Activity activity, UUID wineId) {
        String serviceUrl = ServiceLocator.GET_WINE_VARIETALS
                .replace("{id}", wineId.toString());
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            VarietalData[] varietalData = gson.fromJson(jsonObject.get("value").toString(), VarietalData[].class);
            return varietalData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetWineVarietalData(activity, wineId);
            }
            else {
                return null;
            }
        }
    }

    public static String[] GetBottleImageNames(Activity activity, UUID wineId) {
        String serviceUrl = ServiceLocator.GET_BOTTLE_IMAGE_NAMES
                .replace("{id}", wineId.toString());
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            String[] imageNameData = gson.fromJson(jsonObject.get("value").toString(), String[].class);
            return imageNameData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetBottleImageNames(activity, wineId);
            }
            else {
                return null;
            }
        }
    }

    public static Bitmap GetBottleImage(Activity activity, UUID wineId, String imageName) {
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
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetBottleImage(activity, wineId, imageName);
            }
            else {
                return null;
            }
        }
    }

    public static Bitmap GetMedalImage(Activity activity, String trophyCode, int ranking) {
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
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetMedalImage(activity, trophyCode, ranking);
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
                case 401:
                    if (RefreshToken()) {
                        return true;
                    }
                    else {
                        ViewHelper.ShowToast(activity, "Sie waren zu lange inaktiv und müssen sich erneut anmelden.");
                        Intent i = new Intent(activity, LoginActivity.class);
                        i.putExtra("TIMEOUT", true);
                        activity.startActivity(i);
                    }
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
