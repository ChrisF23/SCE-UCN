using System;
using System.Collections.Generic;
using System.Linq;
using Ice;
using CL.UCN.DISC.PDIS.SCE.Server.Controllers;
using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server.ZeroIce
{
    public class BackendWeb : IBackendWebDisp_
    {
        // The Logger
        private ILogger<BackendWeb> Logger { get; } = Logging.CreateLogger<BackendWeb>();

        // The Controller
        private readonly IMainController mainController;

        public BackendWeb(IMainController iMainController)
        {
            mainController = iMainController;
        }

        public override List<Registro> obtenerRegistros(Current current = null)
        {
            Logger.LogDebug(LE.Find, "Enviando registros...");

            List<Registro> registros = new List<Registro>();

            foreach (var registro in mainController.GetRegistros())
            {
                registros.Add(ModelConverter.Convert(registro));
            }

            Logger.LogInformation(LE.Find, "Se enviaron los registros! ({0} Registros)", registros.Count());
            return registros;
        }

        public override List<Vehiculo> obtenerVehiculos(Current current = null)
        {
            Logger.LogDebug(LE.Find, "Enviando vehiculos...");

            List<Vehiculo> vehiculos = new List<Vehiculo>();

            foreach (var vehiculo in mainController.GetVehiculos())
            {
                vehiculos.Add(ModelConverter.Convert(vehiculo));
            }

            Logger.LogInformation(LE.Find, "Se enviaron los vehiculos! ({0} Vehiculos)", vehiculos.Count());
            return vehiculos;
        }

        public override Vehiculo obtenerVehiculo(string placa, Current current = null)
        {
            Logger.LogDebug(LE.Find, "Identificando placa...");

            var vehiculo = mainController.GetVehiculo(placa);

            if (vehiculo == null)
            {
                Logger.LogWarning(LE.Find, "No se encontro al vehiculo con la placa [{0}]...", placa);
                return null;
            }

            Logger.LogInformation(LE.Find, "Se envio el vehiculo con placa [{0}]!", placa);
            return ModelConverter.Convert(vehiculo);
        }

        public override List<Persona> obtenerPersonas(Current current = null)
        {
            Logger.LogDebug(LE.Find, "Enviando personas...");

            List<Persona> personas = new List<Persona>();

            foreach (var persona in mainController.GetPersonas())
            {
                personas.Add(ModelConverter.Convert(persona));
            }

            Logger.LogInformation(LE.Find, "Se enviaron las personas! ({0} Personas)", personas.Count());
            return personas;
        }

        public override void agregarVehiculo(string rutPersona, string placa, string marca, Tipo tipo, Current current = null)
        {
            throw new NotImplementedException();
        }
    }

}