using Autofac;
using Autofac.Integration.Mvc;
using Microsoft.AspNet.Identity;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Logging;
using Microsoft.IdentityModel.Tokens;
using Microsoft.Owin;
using Microsoft.Owin.Security.Cookies;
using Owin;
using System;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Web.Helpers;
using System.Web.Mvc;
using WineExhibitionQuestionnaire.Infrastructure;

[assembly: OwinStartupAttribute(typeof(WineExhibitionQuestionnaire.Startup))]
namespace WineExhibitionQuestionnaire
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            RegisterServices(app);
            ConfigureOAuth(app);
            IdentityModelEventSource.ShowPII = true;
        }

        private void RegisterServices(IAppBuilder app)
        {
            var builder = new ContainerBuilder();
            builder.RegisterType<AuthService>().As<AuthService>().InstancePerRequest();
            builder.RegisterType<ClientAuthenticationHandler>().As<AuthenticationHandler>().InstancePerRequest();
            builder.RegisterControllers(typeof(MvcApplication).Assembly);
            var container = builder.Build();

            var resolver = new AutofacDependencyResolver(container);
            DependencyResolver.SetResolver(resolver);
            app.UseAutofacMiddleware(container);
        }
        public void ConfigureOAuth(IAppBuilder app)
        {
            app.UseCookieAuthentication(new CookieAuthenticationOptions
            {
                AuthenticationType = DefaultAuthenticationTypes.ApplicationCookie,
                LoginPath = new PathString("/Account/Login"),
                CookieName = "access_token",
                Provider = new CookieAuthenticationProvider
                {
                    OnValidateIdentity = OnValidateIdentity,
                    OnException = (context =>
                    {
                        throw context.Exception;
                    })
                }
            });
            app.UseExternalSignInCookie(DefaultAuthenticationTypes.ExternalCookie);

            AntiForgeryConfig.UniqueClaimTypeIdentifier = ClaimTypes.NameIdentifier;
        }

        private Task OnValidateIdentity(CookieValidateIdentityContext context)
        {
            var authHandler = DependencyResolver.Current.GetService<AuthenticationHandler>();
            var authService = DependencyResolver.Current.GetService<AuthService>();
            return Task.Run(() =>
            {
                try
                {
                    var tokenClaim = context.Identity.FindFirst(DwmClaimTypes.CLAIM_TYPE_TOKEN);
                    var tokenIdClaim = context.Identity.FindFirst(DwmClaimTypes.CLAIM_TYPE_TOKEN_ID);
                    var token = tokenClaim.Value;
                    var tokenId = Guid.Parse(tokenIdClaim.Value);
                    try
                    {
                        authHandler.ValidateToken(token);
                    }
                    catch (SecurityTokenExpiredException stex)
                    {
                        var response = authService.HttpPost(new AuthToken(token, tokenId), Infrastructure.Constants.REFRESH_TOKEN);
                        var authToken = authService.GetAuthTokenFromJson(response);

                        if (authToken == null)
                        {
                            throw new Exception($"refresh expired token '{tokenId}' failed");
                        }

                        var newIdentity = authHandler.CreateIdentity(authToken);
                        var auth = context.OwinContext.Authentication;
                        auth.SignOut(context.Options.AuthenticationType);
                        auth.SignIn(context.Properties, newIdentity);
                        context.ReplaceIdentity(newIdentity);
                    }
                }
                catch (Exception ex)
                {
                    var auth = context.OwinContext.Authentication;
                    auth.SignOut(context.Options.AuthenticationType);
                    context.RejectIdentity();
                }
            });
        }
    }
}
