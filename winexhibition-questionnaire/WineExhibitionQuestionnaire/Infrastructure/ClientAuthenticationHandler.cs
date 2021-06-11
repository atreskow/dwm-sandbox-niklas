using System;
using System.Web;
using Microsoft.AspNet.Identity;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    public class ClientAuthenticationHandler : AuthenticationHandler
    {
        public override string GetAuthenticationType()
        {
            return DefaultAuthenticationTypes.ApplicationCookie;
        }

        public override string GetSecretKey()
        {
            var config = Configuration.WebAppConfiguration.Instance.AuthenticationConfig;
            return config.SecretKey;
        }

        public override AuthToken GetAuthTokenFromCurrentRequest()
        {
            var owinContext = HttpContext.Current?.Request.GetOwinContext();
            var claimsPrincipal = owinContext?.Authentication.User;
            if (claimsPrincipal != null)
            {
            }

            var token = claimsPrincipal.FindFirst(x => x.Type == DwmClaimTypes.CLAIM_TYPE_TOKEN)?.Value;
            var tokenId = claimsPrincipal.FindFirst(x => x.Type == DwmClaimTypes.CLAIM_TYPE_TOKEN_ID)?.Value;
            if (token != null && tokenId != null)
            {
                return new AuthToken(token, Guid.Parse(tokenId));
            }
            return null;
        }
    }
}