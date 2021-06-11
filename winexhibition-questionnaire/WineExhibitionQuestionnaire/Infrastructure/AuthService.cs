using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Web;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    public class AuthService
    {
        public AuthService()
        {
        }

        public String HttpPost(object body, String service)
        {
            using (var client = new HttpClient())
            {
                var jsonData = JsonConvert.SerializeObject(body);
                var stringContent = new StringContent(jsonData, Encoding.UTF8, "application/json");
                var response = client.PostAsync(Constants.SERVICE_URL + service, stringContent);

                String responseMsg = "";

                try
                {
                    HttpResponseMessage msg = response.Result.EnsureSuccessStatusCode();
                    responseMsg = msg.Content.ReadAsStringAsync().Result;
                }
                catch (Exception e)
                {
                    responseMsg = e.ToString();
                }

                return responseMsg;
            }
        }

        public HttpCookie CreateCookie(String name, String value)
        {
            HttpCookie UserCookie = new HttpCookie(name);
            UserCookie.Value = value;
            UserCookie.Expires = DateTime.Now.AddHours(1);
            return UserCookie;
        }

        public AuthToken GetAuthTokenFromJson(String json)
        {
            var responseObject = JObject.Parse(json);
            var status = Convert.ToInt32(responseObject["status"]);
            var value = Convert.ToString(responseObject["value"]);

            if (status == 200 && !value.IsEmpty())
            {
                Guid Id = Guid.Parse(Convert.ToString(responseObject["value"]["Id"]));
                String Token = Convert.ToString(responseObject["value"]["Token"]);

                return new AuthToken(Token, Id);
            }
            return null;
        }
    }
}