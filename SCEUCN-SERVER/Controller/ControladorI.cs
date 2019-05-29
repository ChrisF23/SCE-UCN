
using System;
using System.Collections.Generic;
using System.Linq;
using Ice;
using model;

namespace SCEUCN_SERVER
{
    class ControladorI : model.ControladorDisp_
    {
        private ISystem system;

        public ISystem System { get => system; set => system = value; }



        public override List<Vehiculo> obtenerVehiculos(Current current = null)
        {
            Program.PrintMessage("Enviando vehiculos...");

            List<Vehiculo> vehiculos = new List<Vehiculo>();

            foreach (var vehiculo in system.GetVehiculos())
            {
                vehiculos.Add(ModelConverter.Convert(vehiculo));        
            }

            Program.PrintMessage("Se enviaron los vehiculos!");
            return vehiculos;
        }

        public override void registrarIngreso(string placa, Porteria porteria, Current current = null)
        {
            Program.PrintMessage("Generando registro...");
            
            // 1.- Verificar si el vehiculo se encuentra registrado.
            Model.Vehiculo vehiculoRegistro = system.GetVehiculo(placa);

            if (vehiculoRegistro == null) {
                Program.PrintMessage("Error: El vehiculo [Placa:{0}] no existe en la base de datos.", placa);
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
                Program.PrintMessage("Error: La porteria especificada [{0}] no es valida.", porteria.ToString());
                return;
            }

            // 3.- Crear la fecha del registro.
            string fechaRegistro = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ssZ");

            // 4.- Crear el registro.
            Model.Registro nuevoRegistro = new Model.Registro 
            {
                Vehiculo = vehiculoRegistro,
                Porteria = porteriaRegistro,
                Fecha = fechaRegistro
            };

            // 5.- Guardar el registro.
            system.Save(nuevoRegistro);

            Program.PrintMessage("Ok: Se ha guardado el registro [Placa: {0}, Porteria: {1}, Fecha: {2}]", 
                nuevoRegistro.Vehiculo.Placa, nuevoRegistro.Porteria, nuevoRegistro.Fecha);
        }

        public override void registrarIngresoOffline(string placa, Porteria porteria, string fecha, Current current = null)
        {
            throw new NotImplementedException();
        }
    }
}