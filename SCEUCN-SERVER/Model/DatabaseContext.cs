using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;

namespace SCEUCN_SERVER.Model
{
    public class DatabaseContext : DbContext
    {
        public DatabaseContext(DbContextOptions<DatabaseContext> options) : base(options) { }
        public DbSet<Persona> Personas { get; set; }

        public DbSet<Vehiculo> Vehiculos { get; set; }

        public DbSet<Registro> Registros { get; set; }

        public DbSet<Logo> Logos { get; set; }

    }
}