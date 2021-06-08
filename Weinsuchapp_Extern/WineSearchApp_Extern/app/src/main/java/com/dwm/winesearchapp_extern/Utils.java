package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Intent;

import com.dwm.winesearchapp_extern.Pojo.TrophyCode;
import com.dwm.winesearchapp_extern.Pojo.Document;

import java.util.UUID;

public class Utils {

    public static boolean GetWineData(Activity activity, UUID trophyId, int storageNumber) {
        UUID probenpassId = WineSearchServices.FindProbenpassId(activity, trophyId, storageNumber);
        if (probenpassId == null) return false;

        Document document = WineSearchServices.GetWineData(activity, probenpassId);
        Session.setCurrentWine(document);

        VarietalData[] varietalData = WineSearchServices.GetWineVarietalData(activity, probenpassId);
        Session.setWineVarietals(varietalData);

        return true;
    }

    public static void ParseTrophyData(TrophyData[] codes) {
        for (TrophyData data : codes) {
            String code, name, year;
            UUID id;
            code = data.TrophyCode.split("[-][0-9]")[0];
            if (data.TrophyCode.contains("-")) {
                year = data.TrophyCode.replace(code + "-","");
            }
            else {
                year = null;
            }
            name = data.TrophyName;
            id = data.Id;

            TrophyCode trophyCode = Session.getTrophyCodes().stream().filter(tc -> tc.Code.equals(code)).findAny().orElse(null);
            if (trophyCode != null) {
                trophyCode.Years.add(year);
                trophyCode.Names.add(name);
                trophyCode.Ids.add(id);
            }
            else {
                Session.getTrophyCodes().add(new TrophyCode(id, code, year, name));
            }
        }
    }

    public static void Logout(Activity activity) {
        Session.WipeData();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}
