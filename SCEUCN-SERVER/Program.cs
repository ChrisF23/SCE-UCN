using System;
using Ice;
using Microsoft.EntityFrameworkCore;
using CL.UCN.DISC.PDIS.SCE.Server.DAO;
using CL.UCN.DISC.PDIS.SCE.Server.Controllers;
using CL.UCN.DISC.PDIS.SCE.Server.Model;
using System.Collections.Generic;
using Newtonsoft.Json;
using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server
{

    /// <summary>
    /// Main class
    /// </summary>
    public class Program
    {

        // The current logger
        private static ILogger<Program> Logger { get; } = Logging.CreateLogger<Program>();

        /// <summary>
        /// Main program!
        /// </summary>
        static void Main(string[] args)
        {
            Logger.LogDebug(LE.Main, "Starting Program ..");

            // Configuracion de la base de datos.
            var optionsBuilder = new DbContextOptionsBuilder<DatabaseContext>();
            optionsBuilder.UseSqlite("Data Source=database.db");

            // The Controller.
            IMainController controller = new MainController(optionsBuilder.Options);

            // Generador de data
            {
                DataGeneratorService dg = new DataGeneratorService();
                Logger.LogDebug(LE.Generate, "Populating the backend ..");

                // Saving the Personas
                {
                    List<Persona> personas = dg.GeneratePersonas();
                    foreach (var persona in personas)
                    {
                        controller.Save(persona);
                        Logger.LogDebug(LE.Generate, JsonConvert.SerializeObject(persona));
                    }
                }

                // TODO: Agregar los vehiculos
                {

                }
            }

            // ZeroC properties
            Properties properties = Util.createProperties(ref args);
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

            try
            {
                // The Netscapte Communicator
                using (var communicator = Ice.Util.initialize(initializationData))
                {
                    // The Adapter
                    var adapter = communicator.createObjectAdapterWithEndpoints("BackendAdapter", "default -p 10000 -z");

                    // The MainBackend.
                    var backendMain = new CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.BackendMain(controller);
                    adapter.add(backendMain, Ice.Util.stringToIdentity("BackendMain"));

                    // The WebBackend
                    // var backendWeb = new CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.BackendWeb(controller);
                    // adapter.add(backendWeb, Ice.Util.stringToIdentity("BackendWeb"));

                    // Activate!
                    adapter.activate();

                    Logger.LogDebug(LE.Ice, "OK, esperando peticiones...");

                    // Wait for communicator to shut down
                    communicator.waitForShutdown();
                }
            }
            catch (System.Exception e)
            {
                Logger.LogError(LE.Ice, "Error", e);
            }

        }
    }
}
