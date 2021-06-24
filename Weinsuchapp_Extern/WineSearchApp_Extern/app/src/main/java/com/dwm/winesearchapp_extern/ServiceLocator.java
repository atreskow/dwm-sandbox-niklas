package com.dwm.winesearchapp_extern;

public class ServiceLocator {
    public static final String BASE_URL = "http://system.wine-trophy.com:14814";

    public static final String SEARCH = "/api/{lang}/wineSearch/search";
    public static final String FACETS = "/api/{lang}/wineSearch/facets";

    public static final String GET_BOTTLE_IMAGE_NAMES = "/api/probenpass/{id}/photos";
    public static final String GET_BOTTLE_IMAGE = "/api/probenpass/{id}/photos/{name}";
    public static final String GET_MEDAL_IMAGE = "/api/trophyMedals/{trophyPrefix}/png/normal/{rank}";
}
