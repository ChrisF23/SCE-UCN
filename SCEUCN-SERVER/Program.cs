using System;
using System.Net;
using Ice;
using Microsoft.EntityFrameworkCore;
using SCEUCN_SERVER.Model;

namespace SCEUCN_SERVER
{
    class Program
    {

        public static void PrintMessage(string message, params object[] args)
        {
            Console.WriteLine(message, args);
        }

        static void Main(string[] args)
        {
            
            Console.WriteLine("Starting Main Program...");

            // Opciones de la base de datos.
            var optionsBuilder = new DbContextOptionsBuilder<DatabaseContext>();
            optionsBuilder.UseSqlite("Data Source=database.db");

            // Controlador.
            ISystem system = new SystemImpl(optionsBuilder.Options);

            // Crear la data demo.
            DemoDataCreator demo = new DemoDataCreator();

            // Guardar personas en la base de datos.
            foreach (var persona in demo.Personas)
            {
                system.Save(persona);                
            }

            // Guardar vehiculos en la base de datos.
            foreach (var vehiculo in demo.Vehiculos)
            {
                system.Save(vehiculo);                
            }

            Console.WriteLine();

            Console.WriteLine("Mostrando Personas:");
            foreach (var persona in system.GetPersonas())
            {
                Console.WriteLine("Nombre Completo: {0}, Rut: {1}", (persona.
                Nombres + " " + persona.Apellidos), persona.Rut);
            }

            Console.WriteLine();

            Console.WriteLine("Mostrando Vehiculos:");
            foreach (var vehiculo in system.GetVehiculos())
            {
                Console.WriteLine("Placa: {0}, Marca: {1} (Rut Persona: {2})", vehiculo.Placa, vehiculo.Marca, vehiculo.Persona.Rut);
            }

            Console.WriteLine();

            //return;

            // Initialize Ice communicator
            
            var initData = new InitializationData();
            initData.properties = Util.createProperties();
            initData.properties.setProperty("Ice.Default.SlicedFormat", "1");
            
            using (var communicator = Ice.Util.initialize(initData))
            {
                
                // Instantiate a new ControladorI servant - the implementation of your Controlador
                var servant = new ControladorI();

                // Configure servant
                servant.System = system;

                // Create object adapter - a container for your servants. Listens on port 10000
                var adapter = communicator.createObjectAdapterWithEndpoints("ControladorAdapter", "default -p 10000 -z");

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
