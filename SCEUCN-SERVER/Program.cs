using System;
using System.Collections.Generic;
using CL.UCN.DISC.PDIS.SCE.Server.Controller;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Controller;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

namespace CL.UCN.DISC.PDIS.SCE.Server {

    /// <summary>
    /// Main class
    /// </summary>
    public class Program {

        /// <summary>
        /// Main program!
        /// </summary>
        static void Main(string[] args) {

            // Logger.LogDebug(LE.Main, "Starting Program ..");

            // DI
            ServiceProvider serviceProvider = Startup.BuildServiceProvider();

            // The Logger
            ILogger<Program> logger = serviceProvider.GetService<ILoggerFactory>().CreateLogger<Program>();
            logger.LogInformation("Starting the Program ..");

            // Generate the data!
            {
                IMainController mainController = serviceProvider.GetService<IMainController>();
                DataGeneratorService gen = serviceProvider.GetService<DataGeneratorService>();

                logger.LogDebug("Saving Personas ..");
                List<Persona> personas = gen.GeneratePersonas();
                foreach (var persona in personas) {
                    mainController.Save(persona);
                    logger.LogDebug(LE.Generate, JsonConvert.SerializeObject(persona));
                }

                logger.LogDebug("Saving Vehiculo ..");
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.anio = "2019";
                vehiculo.marca = "Suzuki";
                vehiculo.placa = "FBXS44";
                vehiculo.persona = personas[0];
                vehiculo.tipo = Tipo.Auto;
                mainController.Save(vehiculo);

            }

            // The Netscape Communicator
            try {

                logger.LogDebug("Configuring ZeroIce Communicator ..");
                using(var communicator = serviceProvider.GetService<ZeroIceCommunicator>()) {

                    // Initialize in port 10000
                    communicator.initialize(10000);

                    // Wait for communicator to shut down
                    logger.LogDebug("Communicator OK, starting the server ..");
                    communicator.start();

                }
            } catch (System.Exception e) {
                logger.LogError("Error", e);
            }

            logger.LogInformation("Done.");

        }
    }
}