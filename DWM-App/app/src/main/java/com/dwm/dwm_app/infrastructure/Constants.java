package com.dwm.dwm_app.infrastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String WINE_LINK = "https://results.wine-trophy.com/{language}/wine/";

    public static final List<String> FACET_VALUES = new ArrayList<>(Arrays.asList(
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

    public static final List<String> FACET_BLACKLIST = new ArrayList<>(Arrays.asList(
            "Category IV",
            "false"
    ));
}
