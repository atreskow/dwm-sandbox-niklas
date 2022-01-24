package com.example.jurybriefingapp.networking;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PresentationServices {

    private static final Gson gson = new Gson();

    public static List<RoomData> GetRoomsData(Activity activity) {
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(ServiceLocator.GET_TASTING_ROOMS);
            RoomData[] roomData = gson.fromJson(jsonObject.get("value").toString(), RoomData[].class);
            return Arrays.asList(roomData);
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetRoomsData(activity);
            }
            else {
                return null;
            }
        }
    }

    public static RoomData GetRoomData(Activity activity, UUID uuid) {
        String serviceUrl = ServiceLocator.GET_TASTING_ROOM
                .replace("{id}", uuid.toString());
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            RoomData roomData = gson.fromJson(jsonObject.get("value").toString(), RoomData.class);
            return roomData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetRoomData(activity, uuid);
            }
            else {
                return null;
            }
        }
    }

    public static String GetSlidesVersion(Activity activity) {
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(ServiceLocator.GET_SLIDES_VERSION);
            String slidesVersion = gson.fromJson(jsonObject.get("value").toString(), String.class);
            return slidesVersion;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetSlidesVersion(activity);
            }
            else {
                return null;
            }
        }
    }

    public static int GetJuryBriefingSlidesCount(Activity activity) {
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(ServiceLocator.JURY_BRIEFING_SLIDES_COUNT);
            int roomData = gson.fromJson(jsonObject.get("value").toString(), int.class);
            return roomData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetJuryBriefingSlidesCount(activity);
            }
            else {
                return 0;
            }
        }
    }

    public static Bitmap GetJurySlideBitmap(Activity activity, int slideNumber) {
        String serviceUrl = ServiceLocator.GET_JURY_BRIEFING_SLIDE
                .replace("{slideNumber}", String.valueOf(slideNumber));
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            ImageData imageData = gson.fromJson(jsonObject.get("value").toString(), ImageData.class);
            byte[] imageByteArray = Base64.decode(imageData.SlideData, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            return decodedByte;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetJurySlideBitmap(activity, slideNumber);
            }
            else {
                return null;
            }
        }
    }

    public static String GetJurySlideBase64(Activity activity, int slideNumber) {
        String serviceUrl = ServiceLocator.GET_JURY_BRIEFING_SLIDE
                .replace("{slideNumber}", String.valueOf(slideNumber));
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            ImageData imageData = gson.fromJson(jsonObject.get("value").toString(), ImageData.class);
            return imageData.SlideData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetJurySlideBase64(activity, slideNumber);
            }
            else {
                return null;
            }
        }
    }

    public static DownloadData GetJurySlideZip(Activity activity) {
        String serviceUrl = ServiceLocator.GET_JURY_BRIEFING_ZIP;
        JsonObject jsonObject = null;
        try {
            jsonObject = ServerConnectionMethods.GetData(serviceUrl);
            DownloadData imageData = gson.fromJson(jsonObject.get("value").toString(), DownloadData.class);
            return imageData;
        } catch (Exception e) {
            if (handleStatus(jsonObject, activity, e.toString())) {
                return GetJurySlideZip(activity);
            }
            else {
                return null;
            }
        }
    }


    private static boolean handleStatus(JsonObject jsonObject, Activity activity, String exception) {
        return false;
        /*
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
        }*/
    }
}
