using System;
using System.Collections.Generic;

namespace SCEUCN_WEB.Controller
{
    public class ModelConverter
    {
        /// <summary>
        /// Convierte una instancia de vehiculo de ice, en una instancia de vehiculo de cs.
        /// </summary>
        /// <param name="vehiculo">La instancia del vehiculo a convertir.</param>
        /// <returns>El vehiculo convertido.</returns>
        public static Model.Vehiculo Convert(model.Vehiculo vehiculo)
        {
            return new Model.Vehiculo
            {
                Anio = vehiculo.anio,
                Placa = vehiculo.placa,
                Persona = Convert(vehiculo.persona),
                Marca = vehiculo.marca,
                Tipo = Parse<Model.Tipo>(vehiculo.tipo.ToString()),
                //logos = vehiculoCS.Logos
            };
        }

        /// <summary>
        /// Convierte una instancia de persona de ice, en una instancia de persona de cs.
        /// </summary>
        /// <param name="persona">La persona a convertir.</param>
        /// <returns>La persona convertida.</returns>
        public static Model.Persona Convert(model.Persona persona)
        {
            return new Model.Persona
            {
                Rut = persona.rut,
                Nombres = persona.nombres,
                Apellidos = persona.apellidos,
                Email = persona.email,
                Movil = persona.movil,
                Unidad = persona.unidad,
                Oficina = persona.oficina,
                Anexo = persona.anexo,
                //rol = Parse<model.Rol>(persona.Rol.ToString()),
                Contrato = Parse<Model.Contrato>(persona.contrato.ToString())
            };
        }

        public static T Parse<T>(string value) where T : Enum, IConvertible
        {
            // https://stackoverflow.com/a/28527552.
            return (T)Enum.Parse(typeof(T), value);
        }
    }
}