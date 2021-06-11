using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    public class LoginData
    {
        public String app;
        public String userName;
        public String Password;

        public LoginData(String user, String pw)
        {
            userName = user;
            Password = pw;
            app = "WinePackageApp";
        }
    }
}