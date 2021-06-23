package com.dwm.winesearchapp_extern.Pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

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
            "false"
    ));
}
