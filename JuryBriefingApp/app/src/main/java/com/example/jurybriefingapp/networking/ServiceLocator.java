package com.example.jurybriefingapp.networking;

public class ServiceLocator {
    public static final String BASE_URL = "http://dev.wine-trophy.com:14814/";
    public static final String SIGNALR_URL = "http://score.wine-trophy.com:51573/signalr";
    //public static final String SIGNALR_LOCAL = "http://10.0.2.2:55588/";
    public static final String SIGNALR_LOCAL = "http://192.168.0.86:55588/signalr";

    public static final String GET_TASTING_ROOMS = "api/bewertugsApp/tastingRooms";
    public static final String JURY_BRIEFING_SLIDES_COUNT = "api/bewertugsApp/juryBriefingSlides";
    public static final String GET_JURY_BRIEFING_SLIDE = "api/bewertugsApp/juryBriefingSlides/{slideNumber}";
    public static final String GET_JURY_BRIEFING_ZIP = "/api/bewertugsApp/juryBriefingSlides/zip";

    public static final String SLIDES_HUB = "slidesHub";
}
