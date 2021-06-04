using Autofac;
using Autofac.Integration.Mvc;
using WineExhibitionQuestionnaire.Infrastructure;
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

[assembly: OwinStartupAttribute(typeof(WineExhibitionQuestionnaire.Startup))]
namespace WineExhibitionQuestionnaire
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
