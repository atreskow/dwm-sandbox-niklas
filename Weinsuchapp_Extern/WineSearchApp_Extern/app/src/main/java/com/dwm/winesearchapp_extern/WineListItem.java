package com.dwm.winesearchapp_extern;

import android.widget.TextView;

import com.dwm.winesearchapp_extern.Pojo.response.DocumentData;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;

import java.util.UUID;
import java.util.stream.Collectors;

public class WineListItem {

    public WineListItem(DocumentData data) {
        Id = data.Id;
        WineName = data.Name;
        Producer = "Producer: " + data.ProducerCompany;
        Origin = "Origin: " + data.Country;
        Varietal = "Varietals: " + data.Varietal.stream().collect(Collectors.joining(", "));
        Award = "Award: " + data.Award;
        TrophyCode = data.TrophyCode;
        Ranking = data.Ranking;
    }

    public UUID Id;
    public String WineName;
    public String Producer;
    public String Origin;
    public String Varietal;
    public String Award;

    public String TrophyCode;
    public int Ranking;
}
