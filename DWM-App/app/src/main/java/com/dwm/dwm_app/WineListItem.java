package com.dwm.dwm_app;

import com.dwm.dwm_app.Pojo.response.DocumentData;

import java.util.List;
import java.util.UUID;

public class WineListItem {

    public WineListItem(DocumentData data) {
        id = data.id;
        wineName = data.name;
        producer = data.producerCompany;
        country = data.country;
        region = data.region;
        varietal = data.varietals;
        award = data.award;
        trophyCode = data.trophyCode;
        storageNumber = data.storageNumber;
        ranking = data.ranking;

        //------------- Ab hier relevant für Winedetails Activity

        vintage = data.year;
        category = data.wineCategory;
        flavour = data.flavour;
        type = data.type;
        vinification = data.vinification;

        alcohol = data.alcohol;
        acidity = data.acidity;
        sugar = data.sugar;
        sulfur = data.sulfur;
        organic = data.organic;

        wineLink = data.link;
    }

    public UUID id;
    public String wineName;
    public String producer;
    public List<String> varietal;
    public String award;

    public String trophyCode;
    public int storageNumber;
    public int ranking;

    //------------- Ab hier relevant für Winedetails Activity

    public String vintage;
    public String category;
    public String flavour;
    public String type;
    public String region;
    public String country;
    public String vinification;

    public float alcohol;
    public float acidity;
    public float sugar;
    public float sulfur;
    public boolean organic;

    public String wineLink;
}
