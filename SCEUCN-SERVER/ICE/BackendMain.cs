using System;
using System.Collections.Generic;
using Ice;
using CL.UCN.DISC.PDIS.SCE.Server.Controllers;
using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server.ZeroIce
{
    public class BackendMain : IBackendMainDisp_
    {
        private ILogger<BackendMain> Logger { get; } = Logging.CreateLogger<BackendMain>();

        private IMainController mainController;

        public BackendMain(IMainController iMainController)
        {
            mainController = iMainController;
        }

        public override List<Vehiculo> obtenerVehiculos(Current current = null)
        {
            List<Vehiculo> vehiculos = new List<Vehiculo>();

            foreach (var vehiculo in mainController.GetVehiculos())
            {
                vehiculos.Add(ModelConverter.Convert(vehiculo));
            }
            return vehiculos;
        }

        public override void registrarIngreso(string placa, Porteria porteria, Current current = null)
        {
            // 1.- Verificar si el vehiculo se encuentra registrado.
            Model.Vehiculo vehiculoRegistro = mainController.GetVehiculo(placa);

            if (vehiculoRegistro == null)
            {
                Logger.LogCritical(LE.Find, "Error: El vehiculo Placa [{placa}] no existe en el backend.", placa);
                return;
            }

            // 2.- Intentar parsear la porteria.
            Model.Porteria porteriaRegistro;

            try
            {
                // OLD: 
                // porteriaRegistro = (Model.Porteria) Enum.Parse(typeof(Model.Porteria), porteria.ToString());

                // NEW:
                porteriaRegistro = ModelConverter.Parse<Model.Porteria>(porteria.ToString());

            }
            catch (System.Exception)
            {
                Logger.LogCritical(LE.Converter, "Error: La porteria especificada [{0}] no es valida!", porteria);
                return;
            }

            // 3.- Crear la fecha del registro.
            string fechaRegistro = DateTime.UtcNow.ToLocalTime().ToString("yyyy-MM-ddTHH:mm:ssZ");

            // 4.- Crear el registro.
            Model.Registro nuevoRegistro = new Model.Registro
            {
                Vehiculo = vehiculoRegistro,
                Porteria = porteriaRegistro,
                Fecha = fechaRegistro
            };

            // 5.- Guardar el registro.
            mainController.Save(nuevoRegistro);

            Logger.LogInformation(LE.Save, "Ok: Se ha guardado el registro [Placa: {0}, Porteria: {1}, Fecha: {2}]", nuevoRegistro.Vehiculo.Placa, nuevoRegistro.Porteria, nuevoRegistro.Fecha);

        }

        public override void registrarIngresoOffline(string placa, Porteria porteria, string fecha, Current current = null)
        {
            throw new NotImplementedException();
        }
    }
}