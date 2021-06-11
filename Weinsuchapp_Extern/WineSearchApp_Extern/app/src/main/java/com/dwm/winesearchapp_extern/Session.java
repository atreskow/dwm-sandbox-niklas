package com.dwm.winesearchapp_extern;

import com.dwm.winesearchapp_extern.Pojo.TrophyCode;
import com.dwm.winesearchapp_extern.Pojo.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session {
    private static UUID id;
    private static String token;

    private static List<TrophyCode> trophyCodes = new ArrayList<TrophyCode>();
    private static Document currentWine = null;
    private static VarietalData[] wineVarietals = null;

    private static String selectedCode = null;
    private static String selectedYear = null;

    public static UUID getId() {
        return id;
    }

    public static void setId(UUID id) {
        Session.id = id;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Session.token = token;
    }

    public static List<TrophyCode> getTrophyCodes() {
        return trophyCodes;
    }

    public static void setTrophyCodes(List<TrophyCode> trophyCodes) {
        Session.trophyCodes = trophyCodes;
    }

    public static Document getCurrentWine() {
        return currentWine;
    }

    public static void setCurrentWine(Document currentWine) {
        Session.currentWine = currentWine;
    }

    public static VarietalData[] getWineVarietals() {
        return wineVarietals;
    }

    public static void setWineVarietals(VarietalData[] wineVarietals) {
        Session.wineVarietals = wineVarietals;
    }

    public static void WipeData() {
        Session.id = null;
        Session.token = null;
        Session.trophyCodes = new ArrayList<TrophyCode>();;
        Session.currentWine = null;
        Session.wineVarietals = null;
        Session.selectedCode = null;
        Session.selectedYear = null;
    }

    public static String getSelectedCode() {
        return selectedCode;
    }

    public static void setSelectedCode(String selectedCode) {
        Session.selectedCode = selectedCode;
    }

    public static String getSelectedYear() {
        return selectedYear;
    }

    public static void setSelectedYear(String selectedYear) {
        Session.selectedYear = selectedYear;
    }
}
