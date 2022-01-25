package com.example.jurybriefingapp.networking;

public class ServiceLocator {
    public static final String BASE_URL = "http://score.wine-trophy.com:51573/";

    public static final String GET_TASTING_ROOMS = "api/briefing/tastingRooms";
    public static final String GET_TASTING_ROOM = "api/briefing/tastingRooms/{id}";
    public static final String GET_BRIEFING_SLIDE = "api/briefing/slides/{slideNumber}";
    public static final String BRIEFING_SLIDES_COUNT = "api/briefing/slides/count";
    public static final String GET_JURY_BRIEFING_ZIP = "api/briefing/slides/zip";
    public static final String BRIEFING_SLIDES_CHECKSUM = "api/briefing/slides/checksum";

    public static final String SIGNALR = "signalr";
    public static final String SLIDES_HUB = "slidesHub";
}
