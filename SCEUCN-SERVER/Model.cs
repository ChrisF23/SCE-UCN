using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;

namespace SCEUCN_SERVER.Model
{
    public class DatabaseContext : DbContext
    {

        public DatabaseContext(DbContextOptions<DatabaseContext> options) : base(options) { }
        public DbSet<Persona> Personas { get; set; }
    }

    public enum TipoVehiculo { Auto, Camioneta, Moto }
    
    public enum TipoLogo {Funcionario, Estudiante}
    
    public enum Planta {Oficial, Fija, Temporal}

    public class Persona
    {
        
        public int Id { get; set; }
        
        public string Rut { get; set; }

        public string Nombres { get; set; }
        
        public string Apellidos { get; set; }
    }

    public class Vehiculo
    {

        public Persona persona;
        
        public string patente;
        
        public string marca;
        
        public TipoVehiculo tipo;
    }

    public class Logo
    {

        public string id;
        
        public string fecha;
        
        public TipoLogo tipo;
    }

}