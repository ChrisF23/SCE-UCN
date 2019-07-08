using System.Collections.Generic;
using System.Linq;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Data;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

/// <summary>
/// The Controller
/// </summary>
namespace CL.UCN.DISC.PDIS.SCE.Server.Controller {

    /// <summary>
    /// Implementacion concreta.
    /// </summary>
    public class MainController : IMainController {

        /// <summary>
        /// The Logger.
        /// </summary>
        private readonly ILogger<MainController> _logger;

        /// <summary>
        /// The access to the database.
        /// </summary>
        private readonly ZeroIceContext _databaseContext;

        /// <summary>
        /// The Constructor
        /// </summary>
        public MainController(ILogger<MainController> logger, ZeroIceContext zeroIceContext) {

            _logger = logger;
            _databaseContext = zeroIceContext;

        }

        public void Save(Persona persona) {
            _logger.LogDebug("Saving Persona: {0}", JsonConvert.SerializeObject(persona));
            _databaseContext.Personas.Add(persona);
            _databaseContext.SaveChanges();
        }

        public List<Persona> GetPersonas() {
            return _databaseContext.Personas.ToList();
        }

        public void Save(Vehiculo vehiculo) {
            _logger.LogDebug("Saving Vehiculo: {0}", JsonConvert.SerializeObject(vehiculo));
            _databaseContext.Vehiculos.Add(vehiculo);
            _databaseContext.SaveChanges();
        }

        public Vehiculo GetVehiculo(string placa) {

            // Retorna la entidad si la encuentra. Nulo en otro caso.
            var results = _databaseContext.Vehiculos.Where(v => v.placa == placa);

            if (results.Count() == 1) {
                return results.First();
            }

            _logger.LogWarning(LE.Find, "Can't find Vehiculo con placa: {placa}", placa);
            return null;
        }

        public List<Vehiculo> GetVehiculos() {
            return _databaseContext.Vehiculos.ToList();
        }

        public void Save(Logo logo) {
            _logger.LogDebug("Saving Logo: {0}", JsonConvert.SerializeObject(logo));
            _databaseContext.Logos.Add(logo);
            _databaseContext.SaveChanges();
        }

        public void Update(Logo logo){
            _logger.LogDebug("Updating Logo: {0}", JsonConvert.SerializeObject(logo));
            _databaseContext.Logos.Update(logo);
            _databaseContext.SaveChanges();
        }

        public List<Logo> GetLogos() {
            return _databaseContext.Logos.ToList();
        }

        public void Save(Registro registro) {
            _logger.LogDebug("Saving Registro: {0}", JsonConvert.SerializeObject(registro));
            _databaseContext.Registros.Add(registro);
            _databaseContext.SaveChanges();
        }

        public List<Registro> GetRegistros() {
            return _databaseContext.Registros.ToList();
        }

    }

}