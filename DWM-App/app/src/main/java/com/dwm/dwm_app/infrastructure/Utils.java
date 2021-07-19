package com.dwm.dwm_app.infrastructure;

import android.content.Context;

import com.dwm.dwm_app.adapters.NavDrawerItem;
import com.dwm.dwm_app.R;
import com.dwm.dwm_app.server_connection.request.FacetQueryGroup;
import com.dwm.dwm_app.server_connection.request.OptionData;
import com.dwm.dwm_app.server_connection.request.QueryObjData;
import com.dwm.dwm_app.server_connection.request.SortParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static <T> JSONObject parseObjectToJSONObject(T object) {
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

    public static QueryObjData generateQueryObjData(String queryToken, String value) {
        List<String> queryTokens = new ArrayList<>();
        queryTokens.add(String.format("+%s:*%s*", queryToken, value));
        QueryObjData queryObjData = new QueryObjData(queryTokens, Session.getFacetQueryGroups());
        return queryObjData;
    }

    public static QueryObjData generateQueryObjData(List<FacetQueryGroup> facetQueryGroups) {
        List<String> queryTokens = new ArrayList<>();
        QueryObjData queryObjData = new QueryObjData(queryTokens, facetQueryGroups);

        return queryObjData;
    }

    public static OptionData generateOptionData(int loaded) {
        List<SortParam> sortParams = new ArrayList<>();
        sortParams.add(new SortParam("trophy_year", false));
        OptionData optionData = new OptionData(Session.getWinesPerPage(),
                loaded,
                sortParams,
                null,
                Constants.FACET_VALUES,
                null);
        return optionData;
    }

    public static OptionData generateOptionData(int loaded, int amount) {
        List<SortParam> sortParams = new ArrayList<>();
        sortParams.add(new SortParam("trophy_year", false));
        OptionData optionData = new OptionData(amount,
                loaded,
                sortParams,
                null,
                new ArrayList<>(),
                null);
        return optionData;
    }

    public static String getHeaderForValue(Context context, String value) {
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

    public static List<NavDrawerItem> transferFacetTrues(String headerText, List<NavDrawerItem> items) {
        List<NavDrawerItem> navDrawerItemList = new ArrayList<>();
        for (FacetQueryGroup facetGroup : Session.getFacetQueryGroups()) {
            if (facetGroup.fieldName.equals(headerText)) {
                for (String value : facetGroup.values) {
                    for (NavDrawerItem item : items) {
                        if (value.equals(item.name)) {
                            item.checked = true;
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

    public static boolean hasAward(int ranking) {
        return ranking > 0 && ranking < 4;
    }

    public static boolean isBlacklistedFacet(String facetName) {
        return Constants.FACET_BLACKLIST.contains(facetName);
    }

    public static String getWineLink(String wineLink) {
        String lang = Locale.getDefault().getLanguage();
        return Constants.WINE_LINK.replace("{language}", lang) + wineLink;
    }

    public static FacetQueryGroup getRankedOnlyFacetGroup() {
        FacetQueryGroup facetQueryGroup = new FacetQueryGroup("medal_rank", "1");
        facetQueryGroup.values.add("2");
        facetQueryGroup.values.add("3");
        return facetQueryGroup;
    }

    public static void addFacetQueryGroupValue(List<FacetQueryGroup> facetQueryGroups, String name, String value) {
        for (FacetQueryGroup group : facetQueryGroups) {
            if (group.fieldName.equals(name)) {
                group.values.add(value);
                return;
            }
        }
        facetQueryGroups.add(new FacetQueryGroup(name, value));
    }

    public static void removeFacetQueryGroupValue(List<FacetQueryGroup> facetQueryGroups, String name, String value) {
        for (FacetQueryGroup group : facetQueryGroups) {
            if (group.fieldName.equals(name)) {
                group.values.remove(value);
                if (group.values.size() == 0) {
                    facetQueryGroups.remove(group);
                }
                return;
            }
        }
    }
}
