package com.dwm.winesearchapp_extern.Pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String HEADER_TROPHY_NAME = "Wine Trophy (Contest)";
    public static final String HEADER_TROPHY_YEAR = "Wine Trophy (Jahr)";
    public static final String HEADER_MEDAL_NAME = "Award (Medaille)";
    public static final String HEADER_WINE_FLAVOUR = "Geschmack";
    public static final String HEADER_WINE_TYPE = "Weinart";
    public static final String HEADER_WINE_VINIFICATION = "Ausbau";
    public static final String HEADER_WINE_CATEGORY = "Kategorie";
    public static final String HEADER_CULTIVATION_COUNTRY = "Anbauland";
    public static final String HEADER_WINE_VINTAGE = "Jahrgang";
    public static final String HEADER_IS_BIO = "Biologisch";
    public static final String HEADER_WINE_VARIETAL = "Rebsorte";

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

    public static final List<String> FacetBlacklist = new ArrayList<>(Arrays.asList(
            "Category IV",
            "false",
            "diverse Rebsorten",
            "nicht angegeben",
            "kein Jahrgang"
    ));
}
