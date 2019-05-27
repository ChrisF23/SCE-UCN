using System;
using System.Collections.Generic;
using SCEUCN_SERVER.Model;

namespace SCEUCN_SERVER
{
    public class ModelConverter
    {
        public static Registro convert(model.Registro registroI)
        {

            Registro registroCS = new Registro();

            // Pasar patente.
            registroCS.Vehiculo.Placa = registroI.patente;

            try 
            {
                // https://stackoverflow.com/a/26225744

                // Transformar desde unix-milisegundos.
                /* long fechaL = long.Parse(registroI.fechaUnixMilisegundos);
                DateTimeOffset dateTimeOffset = DateTimeOffset.FromUnixTimeMilliseconds(fechaL); */

                // Pasar fecha. 
                //FIXME: La fecha es string
                //registroCS.Fecha = dateTimeOffset.ToLocalTime().DateTime;
            } 
            catch (System.Exception)
            {
                // Retornar nulo si ocurre un error.
                return null;
            }

            // Retornar el registro convertido.
            return registroCS;
        }
    }
}