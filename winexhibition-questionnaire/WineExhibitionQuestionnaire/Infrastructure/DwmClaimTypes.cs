using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    public class DwmClaimTypes
    {
        public static readonly string CLAIM_TYPE_JUROR_ID = "JurorId";
        public static readonly string CLAIM_TYPE_TOKEN_ID = "authTokenId";
        public static readonly string CLAIM_TYPE_TOKEN = "authToken";
        public static readonly string CLAIM_TYPE_FULL_NAME = "fullName";
    }
}