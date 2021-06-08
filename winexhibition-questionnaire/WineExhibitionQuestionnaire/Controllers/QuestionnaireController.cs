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
        public ActionResult Index(ExhibitorModel data, string language)
        {
            var model = data ?? new ExhibitorModel();
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

                    var lang = Thread.CurrentThread.CurrentUICulture.Name;

                    return RedirectToAction("Success", new { orderId = entity.ID, language = lang });
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

        public void ChangeCulture(string countryCode)
        {
            CultureInfo culture = new CultureInfo(countryCode, true);
            culture.NumberFormat.NumberDecimalSeparator = ",";
            Thread.CurrentThread.CurrentCulture = culture;
            Thread.CurrentThread.CurrentUICulture = culture;
        }

        [HttpGet]
        public ActionResult Dump()
        {
            using (var ctx = new WineExhibitionQuestionnaireEntities())
            {
                var sb = new StringBuilder();
                sb.AppendLine("Reservierungsnummer;Vorname;Nachname;E-Mail;Telefon;Weißwein;Rotwein;Roséwein;Schaumwein;Abholtag;Abholzeit;Reserviert am");
                var entries = ctx.CLIENT_DATA.OrderBy(x => x.COMPANY_NAME).ToArray();
                entries.ForEach(x =>
                {
                    sb.AppendLine($"{x.COMPANY_NAME};{x.FIRST_NAME};{x.LAST_NAME};{x.EMAIL};{x.PARTICIPATE};{x.STAND_SIZE};{x.CREATED_AT.ToString("dd.MM.yyy HH:mm")}");
                });
                var result = sb.ToString();
                return File(new MemoryStream(Encoding.Unicode.GetBytes(result)), "text/csv", "Vorbestellungen.csv");
            }
        }
        
    }
}