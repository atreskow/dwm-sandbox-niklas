using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    public static class Constants
    {
        public const String SERVICE_URL = "http://system.wine-trophy.com:14814";
        public const String CREATE_TOKEN = "/api/auth/create";
        public const String REFRESH_TOKEN = "/api/auth/refreshToken";
    }
}