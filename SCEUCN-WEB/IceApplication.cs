using System.Collections.Generic;
using System.Threading.Tasks;
using Ice;
using model;

namespace SCEUCN_WEB
{
    public interface IIceApplication
    {
        Task<List<Vehiculo>> obtenerVehiculos();
        Task<Vehiculo> obtenerVehiculo(string placa);
    }

    public class IceApplication : IIceApplication
    {
        private Communicator communicator;

        private string serverIP;

        public IceApplication()
        {
            serverIP = "192.168.0.10";

            InitializationData initData = new InitializationData();

            communicator = Util.initialize(initData);
        }

        private ControladorWebPrx getControladorProxy() {

            // The Proxy
            ObjectPrx proxy = communicator.stringToProxy("ControladorWeb:default -h "+serverIP+" -p 10000 -z");

            // The Specific Proxy
            return ControladorWebPrxHelper.checkedCast(proxy);
        }

        public Task<List<Vehiculo>> obtenerVehiculos(){
            return getControladorProxy().obtenerVehiculosAsync();
        }

        public Task<Vehiculo> obtenerVehiculo(string placa)
        {
            return getControladorProxy().obtenerVehiculoAsync(placa);
        }
    }
}