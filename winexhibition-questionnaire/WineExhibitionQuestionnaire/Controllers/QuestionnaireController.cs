using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Web.Mvc;
using System.Globalization;
using System.Threading;
using WineExhibitionQuestionnaire.Models;
using WineExhibitionQuestionnaire.DataProvider;

namespace WineExhibitionQuestionnaire.Controllers
{
    public class QuestionnaireController : Controller
    {
        protected override IAsyncResult BeginExecuteCore(AsyncCallback callback, object state)
        {
            ChangeCulture("de-DE");
            return base.BeginExecuteCore(callback, state);
        }

        [HttpGet]
        public ActionResult Index(string language)
        {
            var model = new ExhibitorModel();
            if (language != null)
            {
                ViewData["language"] = language;
                ChangeCulture(language);
            }

            return View(model);     
        }

        [HttpPost]
        public ActionResult Index(ExhibitorModel model)
        {
            ViewData["language"] = model.lang;

            if (ModelState.IsValid)
            {
                using (var ctx = new WineExhibitionQuestionnaireEntities())
                {
                    var entity = new CLIENT_DATA
                    {
                        ID = Guid.NewGuid(),
                        COMPANY_NAME = model.CompanyName,
                        FIRST_NAME = model.FirstName,
                        LAST_NAME = model.LastName,
                        EMAIL = model.EMail,
                        PARTICIPATE = model.Participate,
                        CREATED_AT = DateTime.Now
                    };
                    if (model.Participate == 1)
                    {
                        entity.STAND_SIZE = model.Size;
                    }

                    ctx.CLIENT_DATA.Add(entity);

                    ctx.SaveChanges();

                    return RedirectToAction("Success", new { orderId = entity.ID, language = model.lang });
                }
            }

            return View(model);
        }

        public ActionResult Success(Guid orderId, string language)
        {
            if (language != null)
            {
                ViewData["language"] = language;
                ChangeCulture(language);
            }

            using (var ctx = new WineExhibitionQuestionnaireEntities())
            {
                var entity = ctx.CLIENT_DATA.First(x => x.ID == orderId);
                return View(entity);
            }
        }

        public static void ChangeCulture(string countryCode)
        {
            if (countryCode == null)
            {
                return;
            }

            CultureInfo culture = new CultureInfo(countryCode, true);
            culture.NumberFormat.NumberDecimalSeparator = ",";
            Thread.CurrentThread.CurrentCulture = culture;
            Thread.CurrentThread.CurrentUICulture = culture;
        }
        
    }
}