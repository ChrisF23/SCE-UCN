using System;
using System.Collections.Generic;
using System.Linq;
using CL.UCN.DISC.PDIS.SCE.Server.Controller;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using Ice;
using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Controller {

    /// <summary>
    /// Implementacion concreta.
    /// </summary>
    public class BackendWeb : IBackendWebDisp_ {

        /// <summary>
        /// The Logger.
        /// </summary>
        private readonly ILogger<BackendWeb> _logger;

        /// <summary>
        /// The Main Controller.
        /// </summary>
        private readonly IMainController _mainController;

        /// <summary>
        /// The Constructor.
        /// </summary>
        public BackendWeb(ILogger<BackendWeb> logger, IMainController mainController) {
            _logger = logger;
            _mainController = mainController;
        }

        public override void agregarVehiculo(string rutPersona, string placa, string marca, Tipo tipo, Current current = null) {
            throw new NotImplementedException();
        }

        public override List<Persona> obtenerPersonas(Current current = null) {

            _logger.LogDebug(LE.Find, "Enviando personas...");

            List<Persona> personas = _mainController.GetPersonas();
            _logger.LogInformation(LE.Find, "Se enviaron las personas! ({0} Personas)", personas.Count());

            return personas;
        }

        public override List<Registro> obtenerRegistros(Current current = null) {
            _logger.LogDebug(LE.Find, "Enviando registros...");

            List<Registro> registros = _mainController.GetRegistros();

            _logger.LogInformation(LE.Find, "Se enviaron los registros! ({0} Registros)", registros.Count());
            return registros;

        }

        public override Vehiculo obtenerVehiculo(string placa, Current current = null) {
            _logger.LogDebug(LE.Find, "Identificando placa...");

            var vehiculo = _mainController.GetVehiculo(placa);

            if (vehiculo == null) {
                _logger.LogWarning(LE.Find, "No se encontro al vehiculo con la placa [{0}]...", placa);
                return null;
            }

            _logger.LogInformation(LE.Find, "Se envio el vehiculo con placa [{0}]!", placa);
            return vehiculo;
        }

        public override List<Vehiculo> obtenerVehiculos(Current current = null) {
            _logger.LogDebug(LE.Find, "Enviando vehiculos...");

            List<Vehiculo> vehiculos = _mainController.GetVehiculos();

            _logger.LogInformation(LE.Find, "Se enviaron los vehiculos! ({0} Vehiculos)", vehiculos.Count());
            return vehiculos;
        }
    }

}