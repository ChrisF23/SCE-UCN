
using System;
using Ice;
using model;

namespace SCEUCN_SERVER
{
    class ControladorI : model.ControladorDisp_
    {
        private ISystem system;

        public ISystem System { get => system; set => system = value; }
        
        public override model.Vehiculo[] obtenerListadoVehiculos(Current current = null)
        {
            throw new System.NotImplementedException();
        }      

        public override void guardarRegistro(Registro registro, Current current = null)
        {
            
            Program.PrintMessage("Convirtiendo registro...");
            
            // Convertir registro.
            SCEUCN_SERVER.Model.Registro nuevoRegistro = ModelConverter.convert(registro);

            // Si hubo un error en la conversion, cancelar guardado.
            if (nuevoRegistro == null){
                Program.PrintMessage("Hubo un error al convertir el registro...");
                return;
            }

            Program.PrintMessage("Verificando patente '" + nuevoRegistro.PatenteVehiculo + "'...");
            
            // Verificar si la patente existe en la base de datos.
            if (system.GetVehiculo(nuevoRegistro.PatenteVehiculo) == null){
                Program.PrintMessage("Error: El vehiculo no existe en la base de datos.");
                return;
            }

            // Si todo salio bien, guardar el registro.
            system.Save(nuevoRegistro);

            Program.PrintMessage("Ok: Se ha guardado el registro [Patente: {0}, Fecha: {1}]", 
                nuevoRegistro.PatenteVehiculo, nuevoRegistro.Fecha.ToString());

        }

        public override void guardarRegistros(Registro[] registros, Current current = null)
        {
            throw new NotImplementedException();
        }
    }
}