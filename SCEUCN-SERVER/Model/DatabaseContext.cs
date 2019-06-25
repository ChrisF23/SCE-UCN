using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using CL.UCN.DISC.PDIS.SCE.Server.Model;

namespace CL.UCN.DISC.PDIS.SCE.Server.DAO
{
    /// <summary>
    /// Declaracion de acceso a la base de datos del backend.
    /// </summary>
    public class DatabaseContext : DbContext
    {
        public DatabaseContext(DbContextOptions<DatabaseContext> options) : base(options) { }

        public DbSet<Persona> Personas { get; set; }

        public DbSet<Vehiculo> Vehiculos { get; set; }

        public DbSet<Registro> Registros { get; set; }

        public DbSet<Logo> Logos { get; set; }

    }
}