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
    [Authorize]
    public class AdminController : Controller
    {
        [HttpGet]
        public ActionResult Index()
        {
            using (var ctx = new WineExhibitionQuestionnaireEntities())
            {
               var model = ctx.CLIENT_DATA.ToList().OrderBy(o => o.PARTICIPATE).ToList();
               return View(model);
            }
        }

        [HttpGet]
        public ActionResult Dump()
        {
            using (var ctx = new WineExhibitionQuestionnaireEntities())
            {
                var sb = new StringBuilder();
                sb.AppendLine("Firmenname;Vorname;Nachname;E-Mail;Teilnahme;Standgröße;Eingetragen am");
                var entries = ctx.CLIENT_DATA.OrderBy(x => x.COMPANY_NAME).ToArray();
                entries.ForEach(x =>
                {
                    var participation = (x.PARTICIPATE == 0 ? "Ja, Standgröße ähnlich zur letzten Teilnahme" : (x.PARTICIPATE == 1 ? "Ja, aber andere Standgröße" : "Nein"));
                    sb.AppendLine($"{x.COMPANY_NAME};{x.FIRST_NAME};{x.LAST_NAME};{x.EMAIL};{participation};{x.STAND_SIZE};{x.CREATED_AT.ToString("dd.MM.yyy HH:mm")}");
                });
                var result = sb.ToString();
                return File(new MemoryStream(Encoding.Unicode.GetBytes(result)), "text/csv", "Vorbestellungen.csv");
            }
        }
        
    }
}