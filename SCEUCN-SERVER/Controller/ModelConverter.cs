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

            // Pasar atributos
            //registroCS.Vehiculo = convert(registroI.vehiculo);
            //registroCS.Fecha = registroI.fecha;

            // Retornar el registro convertido.
            return registroCS;
        }
    }
}