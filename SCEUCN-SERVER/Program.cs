using System;
using Microsoft.EntityFrameworkCore;
using SCEUCN_SERVER.Model;

namespace SCEUCN_SERVER
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Starting Main Program ..");

            // Opciones de la base de datos.
            var optionsBuilder = new DbContextOptionsBuilder<DatabaseContext>();
            optionsBuilder.UseSqlite("Data Source=database.db");

            // Controlador
            ISystem system = new SystemImpl(optionsBuilder.Options);

            // Datos demo
            
            system.Save(new Persona { Rut = "130144918", Nombres = "Diego Patricio", Apellidos = "Urrutia Astorga" });

            foreach (var persona in system.getPersonas())
            {
                Console.WriteLine("{0}", persona.Rut);
            }

            // Initialize Ice communicator
            using(var communicator = Ice.Util.initialize(ref args))
            {
                // Instantiate a new ControladorI servant - the implementation of your Controlador
                var servant = new ControladorI();

                // Configure servant
                servant.System = system;

                // Create object adapter - a container for your servants. Listens on port 10000
                var adapter = communicator.createObjectAdapterWithEndpoints("ControladorAdapter", "tcp -p 10000");

                // Add the servant object to the object adapter with identity "Controlador"
                adapter.add(servant, communicator.stringToIdentity("Controlador"));

                // Activate object adapter - accept incoming requests and dispatch them to servants
                adapter.activate();

                // Wait for communicator to shut down
                communicator.waitForShutdown();
            }


        }
    }
}
