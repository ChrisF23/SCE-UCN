
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

        public override void registrarIngreso(string patente, Current current = null)
        {
            SCEUCN_SERVER.Model.Vehiculo vehiculo = system.GetVehiculo(patente);
            
            
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