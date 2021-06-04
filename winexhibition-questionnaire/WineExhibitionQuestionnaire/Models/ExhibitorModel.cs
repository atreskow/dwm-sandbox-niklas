using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;


namespace WineExhibitionQuestionnaire.Models
{
    public class ExhibitorModel : IValidatableObject
    {

        [Required(AllowEmptyStrings = false, ErrorMessage = "Firma erforderlich")]
        public string CompanyName { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "Vorname erforderlich")]
        public string FirstName { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "Nachname erforderlich")]
        public string LastName { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "E-Mail erforderlich")]
        public string EMail { get; set; }

        [Required]
        public byte Participate { get; set; }

        public int Size { get; set; }

        public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
        {
            var result = new List<ValidationResult>();

            return result;
        }
    }
}