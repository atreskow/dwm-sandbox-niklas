package com.dwm.winesearchapp_extern.Pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String HEADER_TROPHY_NAME = "Wine Trophy (Contest)";
    public static final String HEADER_TROPHY_YEAR = "Wine Trophy (Year)";
    public static final String HEADER_MEDAL_NAME = "Award (Medaille)";
    public static final String HEADER_WINE_FLAVOUR = "Wine Flavour";
    public static final String HEADER_WINE_TYPE = "Wine Type";
    public static final String HEADER_WINE_VINIFICATION = "Wine Vinification";
    public static final String HEADER_WINE_CATEGORY = "Wine Category";
    public static final String HEADER_CULTIVATION_COUNTRY = "Cultivation Country";
    public static final String HEADER_WINE_VINTAGE = "Wine Vintage";
    public static final String HEADER_IS_BIO = "Organic Wine";
    public static final String HEADER_WINE_VARIETAL = "Wine Varietal";

    public static final List<String> FacetValues = new ArrayList<>(Arrays.asList(
            "trophy_name",
            "trophy_year",
            "wine_flavour",
            "wine_type",
            "wine_vinification",
            "wine_category",
            "cultivation_country",
            "medal_name",
            "is_bio",
            "wine_vintage",
            "varietal"
    ));
}
