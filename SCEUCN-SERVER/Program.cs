using System;
using Microsoft.EntityFrameworkCore;
using SCEUCN_SERVER.Model;

namespace SCEUCN_SERVER
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");

            var optionsBuilder = new DbContextOptionsBuilder<DatabaseContext>();
            optionsBuilder.UseSqlite("Data Source=database.db");

            using (var db = new DatabaseContext(optionsBuilder.Options))
            {

                db.Personas.Add(new Persona { Rut = "130144918", Nombres = "Diego Patricio", Apellidos = "Urrutia Astorga" });

                var count = db.SaveChanges();

                Console.WriteLine("{0} Personas saved into database.", count);

                foreach (var persona in db.Personas)
                {
                    Console.WriteLine("{0}", persona.Rut);
                }

            }

        }
    }
}
