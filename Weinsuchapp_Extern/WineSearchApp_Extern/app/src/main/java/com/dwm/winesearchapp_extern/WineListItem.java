package com.dwm.winesearchapp_extern;

import android.widget.TextView;

import com.dwm.winesearchapp_extern.Pojo.response.DocumentData;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WineListItem {

    public WineListItem(DocumentData data) {
        Id = data.Id;
        WineName = data.Name;
        Producer = data.ProducerCompany;
        Country = data.Country;
        Region = data.Region;
        Varietal = data.Varietal;
        Award = data.Award;
        TrophyCode = data.TrophyCode;
        StorageNumber = data.StorageNumber;
        Ranking = data.Ranking;

        //------------- Ab hier relevant für Winedetails Activity

        Vintage = data.Year;
        Category = data.WineCategory;
        Flavour = data.Flavour;
        Type = data.Type;
        Vinification = data.Vinification;

        Alcohol = data.Alcohol;
        Acidity = data.Acidity;
        Sugar = data.Sugar;
        Sulfur = data.Sulfur;
        Organic = data.Organic;
    }

    public UUID Id;
    public String WineName;
    public String Producer;
    public List<String> Varietal;
    public String Award;

    public String TrophyCode;
    public int StorageNumber;
    public int Ranking;

    //------------- Ab hier relevant für Winedetails Activity

    public String Vintage;
    public String Category;
    public String Flavour;
    public String Type;
    public String Region;
    public String Country;
    public String Vinification;

    public float Alcohol;
    public float Acidity;
    public float Sugar;
    public float Sulfur;
    public boolean Organic;
}
