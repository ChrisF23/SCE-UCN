using Microsoft.EntityFrameworkCore;
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

    public enum TipoVehiculo { Auto, Camioneta, Moto }
    
    public enum TipoLogo { Funcionario, Estudiante }
    
    public enum Planta { Oficial, Fija, Temporal }

    public class Persona
    {
        
        public int Id { get; set; }
        
        public string Rut { get; set; }

        public string Nombres { get; set; }

        public string Apellidos { get; set; }
    }

    public class Vehiculo
    {
        public string Id { get; set; }
        public string Patente { get; set; }

        public Persona Persona { get; set; }
                
        public string Marca { get; set; }
        
        public TipoVehiculo Tipo { get; set; }
    }

    public class Logo
    {

        public string Id { get; set; }
        
        public string Fecha { get; set; }
        
        public TipoLogo Tipo { get; set; }
    }

    public class Registro
    {

        public string Id { get; set; }

        public Vehiculo Vehiculo { get; set; }
        
        public string Fecha { get; set; }
    }

}