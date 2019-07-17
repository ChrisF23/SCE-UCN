// (c) 2019 Proyecto Desarrollo e Integracion de Soluciones, I semestre 2019.

using System.Collections.Generic;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Controller;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using Ice;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

// https://docs.microsoft.com/en-us/dotnet/standard/design-guidelines/naming-guidelines
namespace CL.UCN.DISC.PDIS.SCE.Web.Controller {

    /// <summary>
    /// Declaracion de las operaciones del sistema.
    /// </summary>
    public class WebController : IWebController {

        private readonly ILogger<WebController> _logger;

        private readonly Communicator _communicator;

        private readonly IBackendWebPrx _backendWeb;

        public WebController(ILogger<WebController> logger, IConfiguration config, Communicator communicator) {

            _logger = logger;
            _communicator = communicator;

            var port = config["CommunicatorPort"];
            var server = config["CommunicatorServer"];
            _logger.LogInformation("Using Server {0}:{1}", server, port);

            ObjectPrx proxy = _communicator.stringToProxy("BackendWeb:default -p " + port + " -z -h " + server);

            _backendWeb = IBackendWebPrxHelper.checkedCast(proxy);

        }

        public void AddOrUpdateVehiculo(string rutPersona, string placa, string marca, Tipo tipo, string anio)
        {
            this._backendWeb.agregarOActualizarVehiculoAsync(rutPersona, placa, marca, tipo, anio);
        }

        public List<Persona> GetPersonas()
        {
            return this._backendWeb.obtenerPersonas();
        }

        public Vehiculo GetVehiculo(string patente) {
            return this._backendWeb.obtenerVehiculo(patente);
        }

        public List<Vehiculo> GetVehiculos() {
            return this._backendWeb.obtenerVehiculos();
        }

        public List<Registro> GetRegistros()
        {
            return this._backendWeb.obtenerRegistros();
        }
    }

}