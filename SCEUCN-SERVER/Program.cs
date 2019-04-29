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

            system.Save(new Persona { Rut = "130144918", Nombres = "Diego Patricio", Apellidos = "Urrutia Astorga" });

            foreach (var persona in system.getPersonas())
            {
                Console.WriteLine("{0}", persona.Rut);
            }


        }
    }
}
