using WineExhibitionQuestionnaire.Infrastructure;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Web.Mvc;

namespace WineExhibitionQuestionnaire.Models
{
    public class UserModel : IValidatableObject
    {
        [Required]
        public String Name { get; set; }

        [Required]
        public String Password { get; set; }

        public string ReturnUrl { get; set; }

        public AuthToken ClientAuthToken { get; set; }

        public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
        {
            var result = new List<ValidationResult>();

            var authService = DependencyResolver.Current.GetService<AuthService>();
            var response = authService.HttpPost(new LoginData(Name, Password), Constants.CREATE_TOKEN);

            ClientAuthToken = authService.GetAuthTokenFromJson(response);

            if (ClientAuthToken == null)
            {
                result.Add(new ValidationResult(@Resources.LocRes.Error_Login, new List<string>() { "WrongData" }));
            }
           
            return result;
        }
    }
}