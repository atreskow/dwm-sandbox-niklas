using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    public class AuthToken
    {
        public String Token;
        public Guid Id;

        public AuthToken(String token, Guid id)
        {
            Token = token;
            Id = id;
        }
    }
}