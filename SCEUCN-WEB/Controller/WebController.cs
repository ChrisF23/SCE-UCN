// (c) 2019 Proyecto Desarrollo e Integracion de Soluciones, I semestre 2019.

using System.Collections.Generic;
using Ice;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce;

// https://docs.microsoft.com/en-us/dotnet/standard/design-guidelines/naming-guidelines
namespace CL.UCN.DISC.PDIS.SCE.Web.Controllers
{

    /// <summary>
    /// Declaracion de las operaciones del sistema.
    /// </summary>
    public class WebController : IWebController
    {
        private Communicator communicator;

        private IBackendWebPrx backendWeb;

        private string server;

        public WebController(string server)
        {
            this.server = server;

            this.initializeCommunicator();

            ObjectPrx proxy = this.communicator.stringToProxy("BackendWeb:default -p 10000 -z -h " + this.server);

            this.backendWeb = CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.IBackendWebPrxHelper.checkedCast(proxy);

        }

        private void initializeCommunicator()
        {

            // ZeroC properties
            Properties properties = Util.createProperties();
            // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
            properties.setProperty("Ice.Trace.Admin.Properties", "1");
            properties.setProperty("Ice.Trace.Locator", "2");
            properties.setProperty("Ice.Trace.Network", "3");
            properties.setProperty("Ice.Trace.Protocol", "1");
            properties.setProperty("Ice.Trace.Slicing", "1");
            properties.setProperty("Ice.Trace.ThreadPool", "1");
            properties.setProperty("Ice.Compression.Level", "9");

            // The ZeroC framework!
            InitializationData initializationData = new InitializationData();
            initializationData.properties = properties;

            this.communicator = Util.initialize(initializationData);

        }

        public Vehiculo GetVehiculo(string patente)
        {
            return this.backendWeb.obtenerVehiculo(patente);
        }

        public List<Vehiculo> GetVehiculos()
        {
            return this.backendWeb.obtenerVehiculos();
        }
    }

}