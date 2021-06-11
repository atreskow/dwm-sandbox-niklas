using System;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Principal;
using System.Text;

using Microsoft.IdentityModel.Tokens;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    public abstract class AuthenticationHandler
    {
        public abstract string GetAuthenticationType();
        public abstract string GetSecretKey();
        public abstract AuthToken GetAuthTokenFromCurrentRequest();

        public ClaimsIdentity CreateIdentity(AuthToken authToken)
        {
            var principal = ValidateToken(authToken.Token);
            var userName = principal.Identity.Name;
            var jwtIdentity = new ClaimsIdentity(new JwtIdentity(
                true, userName, GetAuthenticationType()
            ));
            principal.Claims.ForEach(claim => { jwtIdentity.AddClaim(claim); });
            jwtIdentity.AddClaim(new Claim(DwmClaimTypes.CLAIM_TYPE_TOKEN, authToken.Token));
            jwtIdentity.AddClaim(new Claim(DwmClaimTypes.CLAIM_TYPE_TOKEN_ID, authToken.Id.ToString()));
            return jwtIdentity;
        }
        
        public ClaimsPrincipal ValidateToken(string authToken)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var validationParameters = GetValidationParameters();
            SecurityToken validatedToken;
            ClaimsPrincipal principal = tokenHandler.ValidateToken(authToken, validationParameters, out validatedToken);
            return principal;
        }
        private TokenValidationParameters GetValidationParameters()
        {
            var SIGNING_KEY = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(GetSecretKey()));
            return new TokenValidationParameters()
            {
                ValidateLifetime = true, // Because there is no expiration in the generated token
                ValidateAudience = false, // Because there is no audiance in the generated token
                ValidateIssuer = false,   // Because there is no issuer in the generated token
                IssuerSigningKey = SIGNING_KEY,
                ClockSkew = TimeSpan.Zero
            };
        }
    }

    public class JwtIdentity : IIdentity
    {
        private bool _isAuthenticated;
        private string _name;
        private string _authenticationType;

        public JwtIdentity() { }
        public JwtIdentity(bool isAuthenticated, string name, string authenticationType)
        {
            _isAuthenticated = isAuthenticated;
            _name = name;
            _authenticationType = authenticationType;
        }
        public string AuthenticationType
        {
            get { return _authenticationType; }
        }

        public bool IsAuthenticated
        {
            get { return _isAuthenticated; }
        }

        public string Name
        {
            get { return _name; }
        }
    }
}
