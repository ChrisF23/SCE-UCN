using SCEUCN_SERVER.Model;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace SCEUCN_SERVER
{

    interface ISystem
    {
        // Personas
        void Save(Persona persona);

        List<Persona> GetPersonas();

        // Vehiculos
        void Save(Vehiculo vehiculo);

        Vehiculo GetVehiculo(string patente);

        List<Vehiculo> GetVehiculos();

        // Logos
        void Save(Logo logo);

        List<Logo> GetLogos();

        // Registros
        void Save(Registro registro);

        List<Registro> GetRegistros();
    }

    class SystemImpl : ISystem
    {
        private readonly DatabaseContext databaseContext;

        public SystemImpl(DbContextOptions<DatabaseContext> options)
        {
            databaseContext = new DatabaseContext(options);

            // FIXME: Borrado solamente en tiempo de desarrollo.
            databaseContext.Database.EnsureDeleted();

            // Creacion de la base de datos
            databaseContext.Database.EnsureCreated();
        }

        public void Save(Persona persona)
        {
            if (persona == null){
                throw new System.Exception("Persona fue null.");
            }

            databaseContext.Personas.Add(persona);
            databaseContext.SaveChanges();
        }



        public List<Persona> GetPersonas()
        {
            return databaseContext.Personas.ToList();
        }

        public void Save(Vehiculo vehiculo)
        {
            databaseContext.Vehiculos.Add(vehiculo);
            databaseContext.SaveChanges();
        }

        public Vehiculo GetVehiculo(string patente)
        {
            // Retorna la entidad si la encuentra. Nulo en otro caso.
            return databaseContext.Vehiculos.Where(v => v.Patente == patente).First();
        }

        public List<Vehiculo> GetVehiculos()
        {
            return databaseContext.Vehiculos.ToList();
        }

        public void Save(Logo logo)
        {
            databaseContext.Logos.Add(logo);
            databaseContext.SaveChanges();
        }

        public List<Logo> GetLogos()
        {
            return databaseContext.Logos.ToList();
        }

        public void Save(Registro registro)
        {
            databaseContext.Registros.Add(registro);
            databaseContext.SaveChanges();
        }

        public List<Registro> GetRegistros()
        {
            return databaseContext.Registros.ToList();
        }

    }

}