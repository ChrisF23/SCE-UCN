using SCEUCN_SERVER.Model;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace SCEUCN_SERVER
{

    interface ISystem
    {
        void Save(Persona persona);

        List<Persona> getPersonas();

    }

    class SystemImpl : ISystem
    {
        private readonly DatabaseContext databaseContext;

        public SystemImpl(DbContextOptions<DatabaseContext> options)
        {
            databaseContext = new DatabaseContext(options);

            // FIXME: Borrado solamente en tiempo de desarrollo.
            databaseContext.Database.EnsureDeleted();

            // Creacion de la base de datos
            databaseContext.Database.EnsureCreated();
        }

        public void Save(Persona persona)
        {
            // using (databaseContext)
            // {
            databaseContext.Personas.Add(persona);
            databaseContext.SaveChanges();
            // }
        }

        public List<Persona> getPersonas()
        {
            // using (databaseContext)
            // {
            return databaseContext.Personas.ToList();
            // }
        }

    }

}