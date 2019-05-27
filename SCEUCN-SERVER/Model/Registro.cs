
using System;
using System.Collections.Generic;

namespace SCEUCN_SERVER.Model
{
    public enum Porteria { Principal, Sur, Norte }
    public class Registro
    {

        public string Id { get; set; }

        public Vehiculo Vehiculo { get; set; }
        
        public Porteria Porteria{ get; set;}

        public string Fecha { get; set; }
    }
}