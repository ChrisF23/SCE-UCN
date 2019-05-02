
using System;
using Ice;
using model;

namespace SCEUCN_SERVER
{
    class ControladorI : model.ControladorDisp_
    {
        private ISystem system;

        public ISystem System { get => system; set => system = value; }

        public override model.Vehiculo buscarVehiculo(string patente, Current current = null)
        {
            throw new System.NotImplementedException();
        }

        public override model.Vehiculo[] obtenerListadoVehiculos(Current current = null)
        {
            throw new System.NotImplementedException();
        }

        public override void registrarIngreso(string patente, string fecha, Current current = null)
        {
            Program.PrintMessage("Verificando si existe el vehiculo con la patente '" + patente + "'...");
            SCEUCN_SERVER.Model.Vehiculo vehiculoR = system.GetVehiculo(patente);

            if (vehiculoR == null){
                Program.PrintMessage("Error: El vehiculo no existe en la base de datos.");
                return;
            }

            DateTime fechaR;
            try 
            {
                // https://stackoverflow.com/a/26225744

                long fechaL = long.Parse(fecha);
                DateTimeOffset dateTimeOffset = DateTimeOffset.FromUnixTimeMilliseconds(fechaL);
                fechaR = dateTimeOffset.ToLocalTime().DateTime;
            } 
            catch (System.Exception)
            {
                Program.PrintMessage("Error: Hubo un problema al registrar la fecha.");
                return;
            }
            
            SCEUCN_SERVER.Model.Registro registro = new SCEUCN_SERVER.Model.Registro{
                Fecha = fechaR,
                Vehiculo = vehiculoR
            };

            system.Save(registro);

            Program.PrintMessage("Ok: Se ha registrado el ingreso del vehiculo.");

            // Mostrar todos los registros.
            foreach (var r in system.GetRegistros())
            {
                Program.PrintMessage("{0} - {1}", r.Fecha, r.Vehiculo.Patente);
            }
        }

        // Adaptadores

        model.Vehiculo TranformToIceModel(SCEUCN_SERVER.Model.Vehiculo vehiculo)
        {
            // FIXME.
            model.Persona persona = new model.Persona {
                rut = vehiculo.Persona.Rut,
                nombre = (vehiculo.Persona.Nombres + " " + vehiculo.Persona.Apellidos),               
            };

            return new model.Vehiculo {
                persona = persona,
                marca = vehiculo.Marca,
                patente = vehiculo.Patente
            };
        }
        

    }
}