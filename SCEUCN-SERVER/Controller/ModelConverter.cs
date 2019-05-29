using System;
using System.Collections.Generic;

namespace SCEUCN_SERVER
{
    public class ModelConverter
    {
        /// <summary>
        /// Convierte una instancia de vehiculo de este servidor, en una instancia de vehiculo de ice.
        /// </summary>
        /// <param name="vehiculoCS">La instancia del vehiculo a convertir.</param>
        /// <returns>El vehiculo convertido.</returns>
        public static model.Vehiculo Convert(Model.Vehiculo vehiculoCS)
        {
            return new model.Vehiculo 
            {
                anio = vehiculoCS.Anio,
                placa = vehiculoCS.Placa,
                persona = Convert(vehiculoCS.Persona),
                marca = vehiculoCS.Marca,
                tipo = Parse<model.Tipo>(vehiculoCS.Tipo.ToString()),
                //logos = vehiculoCS.Logos
            };
        }

        public static model.Persona Convert(Model.Persona personaCS)
        {
            return new model.Persona 
            {
                rut = personaCS.Rut,
                nombres = personaCS.Nombres,
                apellidos = personaCS.Apellidos,
                email = personaCS.Email,
                movil = personaCS.Movil,
                unidad = personaCS.Unidad,
                oficina = personaCS.Oficina,
                anexo = personaCS.Anexo,
                rol = Parse<model.Rol>(personaCS.Rol.ToString()),
                contrato = Parse<model.Contrato>(personaCS.Contrato.ToString())
            };
        }

        public static T Parse<T>(string value) where T : Enum, IConvertible 
        { 
            // https://stackoverflow.com/a/28527552.
            return (T) Enum.Parse(typeof(T), value); 
        }
    }
}