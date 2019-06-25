using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using CL.UCN.DISC.PDIS.SCE.Server.DAO;
using CL.UCN.DISC.PDIS.SCE.Server.Model;
using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server.Controllers
{
    /// <summary>
    /// Implementacion concreta.
    /// </summary>
    public class MainController : IMainController
    {
        /// <summary>
        /// The Logger
        /// </summary>
        private ILogger<MainController> Logger { get; } = Logging.CreateLogger<MainController>();

        /// <summary>
        /// </summary>
        private readonly DatabaseContext databaseContext;

        /// <summary>
        /// </summary>
        public MainController(DbContextOptions<DatabaseContext> options)
        {
            databaseContext = new DatabaseContext(options);

            Logger.LogWarning(LE.Generate, "Delete and create database!!");

            // FIXME: Borrado solamente en tiempo de desarrollo.
            databaseContext.Database.EnsureDeleted();

            // Creacion de la base de datos
            databaseContext.Database.EnsureCreated();
        }

        /// <summary>
        /// </summary>
        public void Save(Persona persona)
        {
            if (persona == null)
            {
                Logger.LogError(LE.Save, "Can't save Persona null");
                throw new System.Exception("Persona fue null.");
            }

            databaseContext.Personas.Add(persona);
            databaseContext.SaveChanges();
        }

        /// <summary>
        /// </summary>
        public List<Persona> GetPersonas()
        {
            return databaseContext.Personas.ToList();
        }

        /// <summary>
        /// </summary>
        public void Save(Vehiculo vehiculo)
        {
            databaseContext.Vehiculos.Add(vehiculo);
            databaseContext.SaveChanges();
        }

        /// <summary>
        /// </summary>
        public Vehiculo GetVehiculo(string placa)
        {
            // Retorna la entidad si la encuentra. Nulo en otro caso.

            var results = databaseContext.Vehiculos.Where(v => v.Placa == placa);

            if (results.Count() == 1)
            {
                return results.First();
            }

            Logger.LogWarning(LE.Find, "Can't find Vehiculo con placa: {placa}", placa);
            return null;
        }

        /// <summary>
        /// </summary>
        public List<Vehiculo> GetVehiculos()
        {
            return databaseContext.Vehiculos.ToList();
        }

        /// <summary>
        /// </summary>
        public void Save(Logo logo)
        {
            databaseContext.Logos.Add(logo);
            databaseContext.SaveChanges();
        }

        /// <summary>
        /// </summary>
        public List<Logo> GetLogos()
        {
            return databaseContext.Logos.ToList();
        }

        /// <summary>
        /// </summary>
        public void Save(Registro registro)
        {
            databaseContext.Registros.Add(registro);
            databaseContext.SaveChanges();
        }

        /// <summary>
        /// </summary>
        public List<Registro> GetRegistros()
        {
            return databaseContext.Registros.ToList();
        }

    }

}