using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Web.Mvc;
using Mailjet.Client;
using Mailjet.Client.Resources;
using Newtonsoft.Json.Linq;
using System.Threading.Tasks;
using Recaptcha.Web.Mvc;
using Recaptcha.Web;
using MigraDoc.DocumentObjectModel;
using MigraDoc.Rendering;
using System.Diagnostics;
using PdfSharp.Pdf;
using System.Collections.Generic;
using System.Globalization;
using System.Threading;
using WineExhibitionQuestionnaire.Models;
using WineExhibitionQuestionnaire.DataProvider;
using WineExhibitionQuestionnaire.Infrastructure;

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
            //Captcha
            RecaptchaVerificationHelper recaptchaHelper = this.GetRecaptchaVerificationHelper();
            if (String.IsNullOrEmpty(recaptchaHelper.Response))
            {
                ModelState.AddModelError("CaptchaError", @Resources.LocRes.Error_Captcha_01);
                return View(model);
            }
            RecaptchaVerificationResult recaptchaResult = recaptchaHelper.VerifyRecaptchaResponse();
            if (!recaptchaResult.Success)
            {
                ModelState.AddModelError("CaptchaError", @Resources.LocRes.Error_Captcha_02);
                return View(model);
            } 


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

        static async Task SendEmail(CLIENT_DATA model, string subject)
        {
            byte[] bytes = GetPdfBytes(model.ID);
            string base64string = Convert.ToBase64String(bytes);

            System.Globalization.CultureInfo culture = new System.Globalization.CultureInfo("de-DE");
            var mailJetConfig = WebAppConfiguration.Instance.MailJet;

            MailjetClient client = new MailjetClient(mailJetConfig.ApiKey, mailJetConfig.ApiSecret);
            MailjetRequest request = new MailjetRequest
            {
                Resource = Send.Resource,
            }
            .Property(Send.FromEmail, mailJetConfig.FromEmail)
            .Property(Send.FromName, mailJetConfig.FromName)
            .Property(Send.Subject, subject)
            .Property(Send.MjTemplateID, mailJetConfig.TemplateOrderId)                           //template ID muss bei bei neuem Template geändert werden
            .Property(Send.MjTemplateLanguage, true)
            .Property(Send.Recipients, new JArray
            {
                new JObject
                {
                    {"Email", model.EMAIL}
                }
            })
            .Property(Send.Attachments, new JArray
            {
                new JObject
                {
                    {"ContentType", "application/pdf"},
                    {"Filename", Resources.LocRes.Invoice_Filename},
                    {"Content", base64string}
                }
            })
            .Property(Send.Vars, new JObject
            {
                {"name", model.FIRST_NAME + " " + model.LAST_NAME},
                {"edit", mailJetConfig.EditUrl + model.ID}
            });
            /**
             * "name": Der Name der bestellenden Person. Wird derzeit zur Anrede verwendet
             * "id": Die Bestellnummer, welche beim Abholen / Überweisen vorgelegt / angegeben werden muss
             * "order": Eine Auflistung der Bestellung, wird derzeit im Code "formatiert"
             * "edit": Der Link zum Bearbeiten der Bestellung
            **/

        MailjetResponse response = await client.PostAsync(request);
            if (response.IsSuccessStatusCode)
            {
                Console.WriteLine(string.Format("Total: {0}, Count: {1}\n", response.GetTotal(), response.GetCount()));
                Console.WriteLine(response.GetData());
            }
            else
            {
                Console.WriteLine(string.Format("StatusCode: {0}\n", response.StatusCode));
                Console.WriteLine(string.Format("ErrorInfo: {0}\n", response.GetErrorInfo()));
                Console.WriteLine(response.GetData());
                Console.WriteLine(string.Format("ErrorMessage: {0}\n", response.GetErrorMessage()));
            }
        }

        /**                 Sonstige Funktionen             **/
        
        public ActionResult GetPdf(Guid id)
        {
            InvoicePdfGenerator invoice = new InvoicePdfGenerator();
            Document doc = invoice.GetInvoice(id);

            const bool unicode = false;
            const PdfFontEmbedding embedding = PdfFontEmbedding.Always;
            PdfDocumentRenderer pdfRenderer = new PdfDocumentRenderer(unicode, embedding);
            pdfRenderer.Document = doc;

            pdfRenderer.RenderDocument();
            var filename = Resources.LocRes.Invoice_Filename;

            using (MemoryStream stream = new MemoryStream())
            {
                pdfRenderer.PdfDocument.Save(stream, false);
                Response.Clear();
                Response.ContentType = "application/pdf";
                Response.AddHeader("Content-Disposition", String.Format("attachment;filename={0}", filename));
                Response.Expires = -1;
                Response.AddHeader("content-length", stream.Length.ToString());
                Response.BinaryWrite(stream.ToArray());
                Response.Flush();
            }
            Response.End();
                
            return null;
        }

        static byte[] GetPdfBytes(Guid id)
        {
            InvoicePdfGenerator invoice = new InvoicePdfGenerator();
            Document doc = invoice.GetInvoice(id);
            const PdfFontEmbedding embedding = PdfFontEmbedding.Always;

            PdfDocumentRenderer pdfRenderer = new PdfDocumentRenderer(false, embedding);

            pdfRenderer.Document = doc;
            pdfRenderer.RenderDocument();

            using (MemoryStream stream = new MemoryStream())
            {
                pdfRenderer.PdfDocument.Save(stream, false);
                byte[] bytes = stream.ToArray();
                return bytes;
            }                
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