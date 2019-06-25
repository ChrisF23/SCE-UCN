using System;
using System.Collections.Generic;
using CL.UCN.DISC.PDIS.SCE.Server.Model;

namespace CL.UCN.DISC.PDIS.SCE.Server.Controllers
{
    public class ModelConverter
    {
        /// <summary>
        /// Convierte una instancia de vehiculo de este servidor, en una instancia de vehiculo de ice.
        /// </summary>
        /// <param name="vehiculo">La instancia del vehiculo a convertir.</param>
        /// <returns>El vehiculo convertido.</returns>
        public static CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Vehiculo Convert(Vehiculo vehiculo)
        {
            return new CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Vehiculo
            {
                anio = vehiculo.Anio,
                placa = vehiculo.Placa,
                persona = Convert(vehiculo.Persona),
                marca = vehiculo.Marca,
                tipo = Parse<CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Tipo>(vehiculo.Tipo.ToString()),
                //logos = vehiculoCS.Logos
            };
        }

        /// <summary>
        /// </summary>
        public static CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Persona Convert(Persona persona)
        {
            return new CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Persona
            {
                rut = persona.Rut,
                nombres = persona.Nombres,
                apellidos = persona.Apellidos,
                email = persona.Email,
                movil = persona.Movil,
                unidad = persona.Unidad,
                oficina = persona.Oficina,
                anexo = persona.Anexo,
                rol = Parse<CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Rol>(persona.Rol.ToString()),
                contrato = Parse<CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Contrato>(persona.Contrato.ToString())
            };
        }

        /// <summary>
        /// </summary>
        public static CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Registro Convert(Registro registro)
        {
            return new CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Registro
            {
                vehiculo = Convert(registro.Vehiculo),
                porteria = Parse<CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Porteria>(registro.Porteria.ToString()),
                fecha = registro.Fecha
            };
        }

        /// <summary>
        /// </summary>
        public static T Parse<T>(string value) where T : Enum, IConvertible
        {
            // https://stackoverflow.com/a/28527552.
            return (T)Enum.Parse(typeof(T), value);
        }
    }
}