using System;
using Ice;
using Microsoft.Extensions.Logging;

/// <summary>
/// </summary>
namespace CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Controller {

    /// <summary>
    /// The Logger Factory
    /// </summary>

    public class ZeroIceCommunicator : IDisposable {

        /// <summary>
        /// The Logger.
        /// </summary>
        private readonly ILogger<ZeroIceCommunicator> _logger;

        /// <summary>
        /// The Communicator.
        /// </summary>
        private readonly Communicator _communicator;

        /// <summary>
        /// The BackendMain.
        /// </summary>
        private readonly BackendMain _backendMain;

        /// <summary>
        /// The BackendWeb.
        /// </summary>
        private readonly BackendWeb _backendWeb;

        /// <summary>
        /// The Constructor.
        /// </summary>
        public ZeroIceCommunicator(ILogger<ZeroIceCommunicator> logger,
            Communicator communicator,
            BackendMain backendMain,
            BackendWeb backendWeb) {
            _logger = logger;
            _communicator = communicator;
            _backendMain = backendMain;
            _backendWeb = backendWeb;
        }

        /// <summary>
        /// Initialize the Communicator.
        /// </summary>
        public void initialize(int port) {

            _logger.LogDebug("Initializing Ice v{0} ({1}) in port {2} ..", Ice.Util.stringVersion(), Ice.Util.intVersion(), port);

            // The Adapter
            var adapter = _communicator.createObjectAdapterWithEndpoints("TheZeroIceBackendServer", "default -z -p " + port);

            // The MainBackend.
            _logger.LogDebug("Loading BackendMain ..");
            adapter.add(_backendMain, Ice.Util.stringToIdentity("BackendMain"));

            // The MainBackend.
            _logger.LogDebug("Loading BackendWeb ..");
            adapter.add(_backendWeb, Ice.Util.stringToIdentity("BackendWeb"));

            // Activate!
            _logger.LogInformation("Activating Communicator ..");
            adapter.activate();

            // Wait for communicator to shut down
            // logger.LogDebug("Communicator OK, waiting to shutdown ..");
            // communicator.waitForShutdown();

        }

        /// <summary>
        /// Starting the server.
        /// </summary>
        public void start() {
            _communicator.waitForShutdown();
        }

        /// <summary>
        /// Dispose!
        /// </summary>
        public void Dispose() {
            _communicator.Dispose();
        }
    }

}