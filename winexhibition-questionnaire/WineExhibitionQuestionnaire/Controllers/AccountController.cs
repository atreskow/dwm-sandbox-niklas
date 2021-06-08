using WineExhibitionQuestionnaire.Infrastructure;
using WineExhibitionQuestionnaire.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Web;
using System.Web.Mvc;

namespace DwmSurpriseBoxReservation.Controllers
{
    public class AccountController : Controller
    {
        [HttpGet]
        public ActionResult Login(string returnUrl)
        {
            UserModel model = new UserModel();
            model.ReturnUrl = returnUrl;
            return View(model);
        }

        [HttpPost]
        public ActionResult Login(UserModel model)
        {

            if (ModelState.IsValid)
            {
                var authHandler = DependencyResolver.Current.GetService<AuthenticationHandler>();

                ClaimsIdentity identity = authHandler.CreateIdentity(model.ClientAuthToken);
                var context = HttpContext.Request.GetOwinContext();
                var authMgr = context.Authentication;
                authMgr.SignIn(identity);

                if (model.ReturnUrl == null)
                {
                    return RedirectToAction("Index", "Admin");
                }
                else
                {
                    String decodedUrl = Server.UrlDecode(model.ReturnUrl);
                    if (Url.IsLocalUrl(decodedUrl))
                    {
                        return Redirect(decodedUrl);
                    }
                    else
                    {
                        return RedirectToAction("Index", "Admin");
                    }
                }
            }

            return View(model);
        }

        [HttpGet]
        public void Logout()
        {
            var context = HttpContext.Request.GetOwinContext();
            var authMgr = context.Authentication;
            authMgr.SignOut();
        }
    }
}