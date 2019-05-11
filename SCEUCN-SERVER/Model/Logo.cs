using System;
using System.Collections.Generic;

namespace SCEUCN_SERVER.Model
{
    public enum TipoLogo { Funcionario, Estudiante }

    public class Logo
    {

        public string Id { get; set; }
        
        public DateTime Fecha { get; set; }
        
        public TipoLogo Tipo { get; set; }
    }
}