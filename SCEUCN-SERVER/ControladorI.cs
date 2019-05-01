
using Ice;
using model;

namespace SCEUCN_SERVER
{
    class ControladorI : model.ControladorDisp_
    {
        private ISystem system;

        public ISystem System { get => system; set => system = value; }

        public override Vehiculo buscarVehiculo(string patente, Current current = null)
        {
            throw new System.NotImplementedException();
        }

        public override Vehiculo[] obtenerListadoVehiculos(Current current = null)
        {
            throw new System.NotImplementedException();
        }

        public override void registrarIngreso(string patente, Current current = null)
        {
            throw new System.NotImplementedException();
        }
    }
}