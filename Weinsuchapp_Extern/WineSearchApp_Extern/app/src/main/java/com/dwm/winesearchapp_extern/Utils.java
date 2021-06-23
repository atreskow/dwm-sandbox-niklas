package com.dwm.winesearchapp_extern;

import android.content.Context;

import com.dwm.winesearchapp_extern.Pojo.Constants;
import com.dwm.winesearchapp_extern.Pojo.request.FacetQueryGroup;
import com.dwm.winesearchapp_extern.Pojo.request.OptionData;
import com.dwm.winesearchapp_extern.Pojo.request.QueryObjData;
import com.dwm.winesearchapp_extern.Pojo.request.SortParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static <T> JSONObject ParseObjectToJSONObject(T object) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        JSONObject bodyData = null;

        try {
            String jsonString = gson.toJson(object);
            bodyData = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyData;
    }

    public static QueryObjData GenerateQueryObjData() {
        List<String> queryTokens = new ArrayList<>();
        queryTokens.add("+wine_name:*" + Session.GetWineName() + "*" );
        QueryObjData queryObjData = new QueryObjData(queryTokens, Session.GetFacetQueryGroups());

        return queryObjData;
    }

    public static OptionData GenerateOptionData(int loaded) {
        List<SortParam> sortParams = new ArrayList<>();
        sortParams.add(new SortParam("trophy_year", false));
        OptionData optionData = new OptionData(Session.GetWinesPerPage(),
                loaded,
                sortParams,
                null,
                Constants.FacetValues,
                null);
        return optionData;
    }

    public static String GetHeaderForValue(Context context, String value) {
        switch (value) {
            case "trophy_name": return context.getResources().getString(R.string.header_trophy_name);
            case "trophy_year": return context.getResources().getString(R.string.header_trophy_year);
            case "medal_name": return context.getResources().getString(R.string.header_medal_name);
            case "wine_vintage": return context.getResources().getString(R.string.header_wine_vintage);
            case "wine_category": return context.getResources().getString(R.string.header_wine_category);
            case "wine_type": return context.getResources().getString(R.string.header_wine_type);
            case "wine_flavour": return context.getResources().getString(R.string.header_wine_flavour);
            case "wine_vinification": return context.getResources().getString(R.string.header_wine_vinification);
            case "is_bio": return context.getResources().getString(R.string.header_is_organic);
            case "cultivation_country": return context.getResources().getString(R.string.header_cultivation_country);
            case "varietal": return context.getResources().getString(R.string.header_wine_varietal);
            default: return "???";
        }
    }

    public static List<NavDrawerItem> TransferFacetTrues(String headerText, List<NavDrawerItem> items) {
        List<NavDrawerItem> navDrawerItemList = new ArrayList<>();
        for (FacetQueryGroup facetGroup : Session.GetFacetQueryGroups()) {
            if (facetGroup.FieldName.equals(headerText)) {
                for (String value : facetGroup.Values) {
                    for (NavDrawerItem item : items) {
                        if (value.equals(item.Name)) {
                            item.Checked = true;
                            navDrawerItemList.add(item);
                            items.remove(item);
                            break;
                        }
                    }
                }
            }
        }
        navDrawerItemList.addAll(items);

        return navDrawerItemList;
    }

    public static boolean HasAward (int ranking) {
        return ranking > 0 && ranking < 4;
    }

    public static boolean IsBlacklistedFacet(String facetName) {
        return Constants.FacetBlacklist.contains(facetName);
    }
}
