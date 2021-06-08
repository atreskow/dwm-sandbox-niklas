using System;
using System.Collections.Generic;
using System.Linq;

namespace WineExhibitionQuestionnaire.Infrastructure
{
    
    public class DwmUser
    {
        public Guid Id { get; set; }
        public string LoginName { get; set; }

        public string FullName { get; set; }

        public IEnumerable<string> UserRoles { get; set; }

        public bool IsUserInRole(string role)
        {
            return UserRoles.Select(x => x.ToLower()).Contains(role.ToLower());
        }
    }
}