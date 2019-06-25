// (c) 2019 Proyecto Desarrollo e Integracion de Soluciones, I semestre 2019.

using System.Collections.Generic;
using CL.UCN.DISC.PDIS.SCE.Server.Model;

// https://docs.microsoft.com/en-us/dotnet/standard/design-guidelines/naming-guidelines
namespace CL.UCN.DISC.PDIS.SCE.Server.Controllers
{

    /// <summary>
    /// Declaracion de las operaciones del sistema.
    /// </summary>
    public interface IMainController
    {
        /// <summary>
        /// Almacena una Persona en el sistema.
        /// </summary>
        void Save(Persona persona);

        /// <summary>
        /// Obtiene todas las Personas presentes en el sistema.
        /// </summary>
        List<Persona> GetPersonas();

        /// <summary>
        /// Almacena un Vehiculo en el sistema.
        /// </summary>
        void Save(Vehiculo vehiculo);

        /// <summary>
        /// Obtiene un Vehiculo a partir de su placa patente.
        /// </summary>
        /// <returns>
        /// El Vehiculo asociado a la patente.
        /// </returns>
        Vehiculo GetVehiculo(string patente);

        /// <summary>
        /// Obtiene todos los Vehiculos presentes en el sistema.
        /// </summary>
        List<Vehiculo> GetVehiculos();

        /// <summary>
        /// Almacena un Logo en el sistema.
        /// </summary>
        void Save(Logo logo);

        /// <summary>
        /// Obtiene todos los Logos presentes en el sistema.
        /// </summary>
        List<Logo> GetLogos();

        /// <summary>
        /// Almacena un Registro en el sistema.
        /// </summary>
        void Save(Registro registro);

        /// <summary>
        /// Obtiene todos los Registros presentes en el sistema.
        /// </summary>
        List<Registro> GetRegistros();

    }

}