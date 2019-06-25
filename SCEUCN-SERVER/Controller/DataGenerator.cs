using GenFu;
using CL.UCN.DISC.PDIS.SCE.Server.Model;
using System.Collections.Generic;
using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server.Controllers
{
    /// <summary>
    /// Interface de generacion de datos.
    /// </summary>
    public interface IDataGenerator<T> where T : class
    {
        List<T> Collection();

        List<T> Collection(int length);

        T Instance();
    }

    /// <summary>
    /// Implementacion concreta, pero generica.
    /// </summary>
    public class DataGenerator<T> : IDataGenerator<T> where T : class, new()
    {
        public List<T> Collection() => A.ListOf<T>();

        public List<T> Collection(int length) => A.ListOf<T>(length);

        public T Instance() => A.New<T>();
    }

    /// <summary>
    /// Servicio de generacion de datos.
    /// </summary>
    public class DataGeneratorService
    {

        /// <summary>
        /// The Logger.
        /// </summary>
        private ILogger<DataGeneratorService> Logger { get; } = Logging.CreateLogger<DataGeneratorService>();

        /// <summary>
        /// Generador de datos de Personas.
        /// </summary>
        private readonly IDataGenerator<Persona> personaGenerator = new DataGenerator<Persona>();

        /// <summary>
        /// Constructor del generador de datos.
        /// </summary>
        public DataGeneratorService()
        {
            Logger.LogDebug(LE.Generate, "Configuring ..");
            // Contador para avanzar por los ruts.
            var i = 0;

            // Arreglo no tan al azar de ruts.
            string[] ruts = {
                "19691840K",
                "153331290",
                "203784904",
                "13489303K",
                "148294083",
                "160038895",
                "16992003K",
                "148961345"
            };

            Logger.LogDebug(LE.Generate, "Using {0} ruts.", ruts.Length);

            // Configuracion del POCO de Persona.
            Logger.LogDebug(LE.Generate, "Configuring ThePersona generator ..");
            A.Configure<Persona>()
                .Fill(x => x.Id, 0)
                .Fill(x => x.Rut, () => ruts[i++])
                .Fill(x => x.Nombres).AsFirstName()
                .Fill(x => x.Apellidos).AsLastName()
                .Fill(x => x.Email, x => { return string.Format("{0}.{1}@ucn.cl", x.Nombres, x.Apellidos).ToLower(); })
                .Fill(x => x.Movil).AsPhoneNumber()
                .Fill(x => x.Unidad).AsMusicGenreName()
                .Fill(x => x.Anexo).AsPhoneNumber();

        }

        /// <summary>
        /// Genera un listado de personas de forma dinamica.
        /// </summary>
        public List<Persona> GeneratePersonas()
        {
            return personaGenerator.Collection(8);
        }

    }
}