package com.dwm.winesearchapp_extern.Pojo.response;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class DocumentData {
    @SerializedName("Id")
    public UUID Id;

    @SerializedName("WineName")
    public String Name;

    @SerializedName("AdditionalInfo")
    public String AdditionalInfo;

    @SerializedName("ArticleNumber")
    public String ArticleNumber;

    @SerializedName("WineVintage")
    public String Year;

    @SerializedName("CultivationCountry")
    public String Country;

    @SerializedName("CultivationRegion")
    public String Area;

    @SerializedName("CultivationRegionLocation")
    public String Region;

    @SerializedName("WineCategory")
    public String WineCategory;

    @SerializedName("WineFlavour")
    public String Flavour;

    @SerializedName("WineQualityLevel")
    public String Quality;

    @SerializedName("WineType")
    public String Type;

    @SerializedName("WineVinification")
    public String Vinification;

    @SerializedName("BottleVolume")
    public String BottleVolume;

    @SerializedName("IsBio")
    public boolean Organic;

    @SerializedName("Unfiltered")
    public boolean Unfiltered;

    @SerializedName("BarrelSample")
    public boolean BarrelSample;

    @SerializedName("ResidualSugar")
    public float Sugar;

    @SerializedName("AlcoholContent")
    public float Alcohol;

    @SerializedName("Acidity")
    public float Acidity;

    @SerializedName("TotalSulphur")
    public float Sulfur;

    @SerializedName("RelativeDensity")
    public float Density;

    @SerializedName("CarbonDioxide")
    public float Carbondioxide;

    @SerializedName("AmountBottles")
    public int BottleAmount;

    @SerializedName("AmountLitres")
    public int LiterAmount;

    @SerializedName("RetailPrice")
    public float Price;

    @SerializedName("Lotnumber")
    public String Lotnumber;

    @SerializedName("SubmitterCompany")
    public String PresenterCompany;

    @SerializedName("CustomerCompany")
    public String ClientCompany;

    @SerializedName("ProducerCompany")
    public String ProducerCompany;

    @SerializedName("ImporterCompany")
    public String ImportCompany;

    @SerializedName("CustomerIdent")
    public int ClientNumber;

    @SerializedName("StorageNumber")
    public int StorageNumber;

    @SerializedName("TrophyIdent")
    public String TrophyCode;

    @SerializedName("TrophyYear")
    public int TrophyYear;

    @SerializedName("TrophyName")
    public String TrophyName;

    @SerializedName("TrophyEdition")
    public String TrophyEdition;

    @SerializedName("MedalRank")
    public int Ranking;

    @SerializedName("MedalName")
    public String MedalName;

    @SerializedName("Award")
    public String Award;

    @SerializedName("WineshootName")
    public String WineshootName;

    @SerializedName("LinkId")
    public String Code;

    @SerializedName("IsAction")
    public boolean ActionWine;

    @SerializedName("IsTrademark")
    public boolean IsTrademark;

    @SerializedName("Published")
    public boolean Published;

    @SerializedName("IsPublishedForPublic")
    public boolean IsPublishedForPublic;

    @SerializedName("PublishDate")
    public boolean PublishDate;
    @SerializedName("SubmitterStreet")
    public String SubmitterStreet;

    @SerializedName("CustomerStreet")
    public String CustomerStreet;

    @SerializedName("ProducerStreet")
    public String ProducerStreet;

    @SerializedName("ImporterStreet")
    public String ImporterStreet;

    @SerializedName("SubmitterZipCode")
    public String SubmitterZipCode;

    @SerializedName("CustomerZipCode")
    public String CustomerZipCode;

    @SerializedName("ProducerZipCode")
    public String ProducerZipCode;

    @SerializedName("ImporterZipCode")
    public String ImporterZipCode;

    @SerializedName("SubmitterCity")
    public String SubmitterCity;

    @SerializedName("CustomerCity")
    public String CustomerCity;

    @SerializedName("ProducerCity")
    public String ProducerCity;

    @SerializedName("ImporterCity")
    public String ImporterCity;

    @SerializedName("SubmitterCountry")
    public String SubmitterCountry;

    @SerializedName("CustomerCountry")
    public String CustomerCountry;

    @SerializedName("ProducerCountry")
    public String ProducerCountry;

    @SerializedName("ImporterCountry")
    public String ImporterCountry;

    @SerializedName("SubmitterEMail")
    public String SubmitterEMail;

    @SerializedName("CustomerEMail")
    public String CustomerEMail;

    @SerializedName("ProducerEMail")
    public String ProducerEMail;

    @SerializedName("ImporterEMail")
    public String ImporterEMail;

    @SerializedName("SubmitterPhone")
    public String SubmitterPhone;

    @SerializedName("CustomerPhone")
    public String CustomerPhone;

    @SerializedName("ProducerPhone")
    public String ProducerPhone;

    @SerializedName("ImporterPhone")
    public String ImporterPhone;

    @SerializedName("SubmitterMobile")
    public String SubmitterMobile;

    @SerializedName("CustomerMobile")
    public String CustomerMobile;

    @SerializedName("ProducerMobile")
    public String ProducerMobile;

    @SerializedName("ImporterMobile")
    public String ImporterMobile;

    @SerializedName("SubmitterFirstName")
    public String SubmitterFirstName;

    @SerializedName("CustomerFirstName")
    public String CustomerFirstName;

    @SerializedName("ProducerFirstName")
    public String ProducerFirstName;

    @SerializedName("ImporterFirstName")
    public String ImporterFirstName;

    @SerializedName("SubmitterLastName")
    public String SubmitterLastName;

    @SerializedName("CustomerLastName")
    public String CustomerLastName;

    @SerializedName("ProducerLastName")
    public String ProducerLastName;

    @SerializedName("ImporterLastName")
    public String ImporterLastName;

    @SerializedName("Barcode")
    public String Barcode;

    @SerializedName("CreatedAt")
    public String DateCreated;

    @SerializedName("FinalResult")
    public float Result;

    @SerializedName("Varietal")
    public String[] Varietal;
}
