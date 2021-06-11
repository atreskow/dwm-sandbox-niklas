namespace WineExhibitionQuestionnaire.Infrastructure
{
    public static class DwmRoles
    {
        public const string BACKSTAGE = "Backstage";
        public const string STAFF_EXTERN = "StaffExtern";
        public const string STAFF = "Staff";
        public const string BILLING = "Billing";
        public const string BOOK_KEEPING = "BookKeeping";
        public const string ADMIN = "Administrator";
        public const string SUPERIOR = "Superior";
        public const string CUSTOMER = "Customer";
        public const string JURY_MEMBER = "JuryMember";
        
        public static readonly string[] ALL = new []
        {
            BACKSTAGE,
            STAFF_EXTERN,
            STAFF,
            BILLING,
            BOOK_KEEPING,
            ADMIN,
            SUPERIOR,
            CUSTOMER,
            JURY_MEMBER
        };

        public static string[] From(params string[] role)
        {
            return role;
        }
    }
}