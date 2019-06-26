using System.Collections.Generic;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using GenFu;
using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server.Controller {

    /// <summary>
    /// Interface de generacion de datos.
    /// </summary>
    public interface IDataGenerator<T> where T : class {
        List<T> Collection();

        List<T> Collection(int length);

        T Instance();
    }

    /// <summary>
    /// Implementacion concreta, pero generica.
    /// </summary>
    public class DataGenerator<T> : IDataGenerator<T> where T : class, new() {

        public List<T> Collection() => A.ListOf<T>();

        public List<T> Collection(int length) => A.ListOf<T>(length);

        public T Instance() => A.New<T>();

    }

    /// <summary>
    /// Servicio de generacion de datos.
    /// </summary>
    public class DataGeneratorService {

        /// <summary>
        /// The Logger.
        /// </summary>
        private readonly ILogger<DataGeneratorService> _logger;

        /// <summary>
        /// Generador de datos de Personas.
        /// </summary>
        private readonly IDataGenerator<Persona> _personaGen = new DataGenerator<Persona>();

        /// <summary>
        /// Constructor del generador de datos.
        /// </summary>
        public DataGeneratorService(ILogger<DataGeneratorService> logger) {

            _logger = logger;

            _logger.LogDebug(LE.Generate, "Configuring ..");
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

            _logger.LogDebug(LE.Generate, "Using {0} ruts.", ruts.Length);

            // Configuracion del POCO de Persona.
            _logger.LogDebug(LE.Generate, "Configuring ThePersona generator ..");
            A.Configure<Persona>()
                .Fill(x => x.id, 0)
                .Fill(x => x.rut, () => ruts[i++])
                .Fill(x => x.nombres).AsFirstName()
                .Fill(x => x.apellidos).AsLastName()
                .Fill(x => x.email, x => { return string.Format("{0}.{1}@ucn.cl", x.nombres, x.apellidos).ToLower(); })
                .Fill(x => x.movil).AsPhoneNumber()
                .Fill(x => x.unidad).AsMusicGenreName()
                .Fill(x => x.anexo).AsPhoneNumber();

        }

        /// <summary>
        /// Genera un listado de personas de forma dinamica.
        /// </summary>
        public List<Persona> GeneratePersonas() {
            return _personaGen.Collection(8);
        }

    }
}