using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;

namespace SCEUCN_SERVER.Model
{

    public class DatabaseContext : DbContext
    {

        public DatabaseContext(DbContextOptions<DatabaseContext> options) : base(options) { }
        public DbSet<Persona> Personas { get; set; }
    }


    public class Persona
    {
        public int Id { get; set; }
        public string Rut { get; set; }

        public string Nombres { get; set; }

        public string Apellidos { get; set; }
    }

}