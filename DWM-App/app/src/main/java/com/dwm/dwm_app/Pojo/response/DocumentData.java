package com.dwm.dwm_app.Pojo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class DocumentData {
    @SerializedName("Id")
    public UUID id;

    @SerializedName("WineName")
    public String name;

    @SerializedName("WineVintage")
    public String year;

    @SerializedName("CultivationCountry")
    public String country;

    @SerializedName("CultivationRegion")
    public String area;

    @SerializedName("CultivationRegionLocation")
    public String region;

    @SerializedName("WineCategory")
    public String wineCategory;

    @SerializedName("WineFlavour")
    public String flavour;

    @SerializedName("WineQualityLevel")
    public String quality;

    @SerializedName("WineType")
    public String type;

    @SerializedName("WineVinification")
    public String vinification;

    @SerializedName("BottleVolume")
    public String bottleVolume;

    @SerializedName("IsBio")
    public boolean organic;

    @SerializedName("Unfiltered")
    public boolean unfiltered;

    @SerializedName("BarrelSample")
    public boolean barrelSample;

    @SerializedName("ResidualSugar")
    public float sugar;

    @SerializedName("AlcoholContent")
    public float alcohol;

    @SerializedName("Acidity")
    public float acidity;

    @SerializedName("TotalSulphur")
    public float sulfur;

    @SerializedName("RelativeDensity")
    public float density;

    @SerializedName("CarbonDioxide")
    public float carbondioxide;

    @SerializedName("AmountBottles")
    public int bottleAmount;

    @SerializedName("AmountLitres")
    public int literAmount;

    @SerializedName("RetailPrice")
    public float price;

    @SerializedName("Lotnumber")
    public String lotnumber;

    @SerializedName("SubmitterCompany")
    public String presenterCompany;

    @SerializedName("CustomerCompany")
    public String clientCompany;

    @SerializedName("ProducerCompany")
    public String producerCompany;

    @SerializedName("ImporterCompany")
    public String importCompany;

    @SerializedName("CustomerIdent")
    public int clientNumber;

    @SerializedName("StorageNumber")
    public int storageNumber;

    @SerializedName("TrophyIdent")
    public String trophyCode;

    @SerializedName("TrophyYear")
    public int trophyYear;

    @SerializedName("TrophyName")
    public String trophyName;

    @SerializedName("TrophyEdition")
    public String trophyEdition;

    @SerializedName("MedalRank")
    public int ranking;

    @SerializedName("MedalName")
    public String medalName;

    @SerializedName("Award")
    public String award;

    @SerializedName("WineshootName")
    public String wineshootName;

    @SerializedName("LinkId")
    public String link;

    @SerializedName("IsAction")
    public boolean actionWine;

    @SerializedName("IsTrademark")
    public boolean isTrademark;

    @SerializedName("Published")
    public boolean published;

    @SerializedName("IsPublishedForPublic")
    public boolean isPublishedForPublic;

    @SerializedName("PublishDate")
    public boolean publishDate;

    @SerializedName("SubmitterStreet")
    public String submitterStreet;

    @SerializedName("CustomerStreet")
    public String customerStreet;

    @SerializedName("ProducerStreet")
    public String producerStreet;

    @SerializedName("ImporterStreet")
    public String importerStreet;

    @SerializedName("SubmitterZipCode")
    public String submitterZipCode;

    @SerializedName("CustomerZipCode")
    public String customerZipCode;

    @SerializedName("ProducerZipCode")
    public String producerZipCode;

    @SerializedName("ImporterZipCode")
    public String importerZipCode;

    @SerializedName("SubmitterCity")
    public String submitterCity;

    @SerializedName("CustomerCity")
    public String customerCity;

    @SerializedName("ProducerCity")
    public String producerCity;

    @SerializedName("ImporterCity")
    public String importerCity;

    @SerializedName("SubmitterCountry")
    public String submitterCountry;

    @SerializedName("CustomerCountry")
    public String customerCountry;

    @SerializedName("ProducerCountry")
    public String producerCountry;

    @SerializedName("ImporterCountry")
    public String importerCountry;

    @SerializedName("SubmitterEMail")
    public String submitterEMail;

    @SerializedName("CustomerEMail")
    public String customerEMail;

    @SerializedName("ProducerEMail")
    public String producerEMail;

    @SerializedName("ImporterEMail")
    public String importerEMail;

    @SerializedName("SubmitterPhone")
    public String submitterPhone;

    @SerializedName("CustomerPhone")
    public String customerPhone;

    @SerializedName("ProducerPhone")
    public String producerPhone;

    @SerializedName("ImporterPhone")
    public String importerPhone;

    @SerializedName("SubmitterMobile")
    public String submitterMobile;

    @SerializedName("CustomerMobile")
    public String customerMobile;

    @SerializedName("ProducerMobile")
    public String producerMobile;

    @SerializedName("ImporterMobile")
    public String importerMobile;

    @SerializedName("SubmitterFirstName")
    public String submitterFirstName;

    @SerializedName("CustomerFirstName")
    public String customerFirstName;

    @SerializedName("ProducerFirstName")
    public String producerFirstName;

    @SerializedName("ImporterFirstName")
    public String importerFirstName;

    @SerializedName("SubmitterLastName")
    public String submitterLastName;

    @SerializedName("CustomerLastName")
    public String customerLastName;

    @SerializedName("ProducerLastName")
    public String producerLastName;

    @SerializedName("ImporterLastName")
    public String importerLastName;

    @SerializedName("Barcode")
    public String barcode;

    @SerializedName("CreatedAt")
    public String dateCreated;

    @SerializedName("FinalResult")
    public float result;

    @SerializedName("Varietal")
    public List<String> varietals;
}
