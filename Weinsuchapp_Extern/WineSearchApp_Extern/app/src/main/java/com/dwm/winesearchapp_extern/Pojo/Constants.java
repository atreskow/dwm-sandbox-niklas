package com.dwm.winesearchapp_extern.Pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final List<String> FacetValues = new ArrayList<>(Arrays.asList(
            "trophy_name",
            "trophy_year",
            "medal_name",
            "wine_vintage",
            "wine_category",
            "wine_type",
            "wine_flavour",
            "wine_vinification",
            "is_bio",
            "cultivation_country",
            "varietal"
    ));

    public static final List<String> FacetBlacklist = new ArrayList<>(Arrays.asList(
            "Category IV",
            "false"
    ));
}
