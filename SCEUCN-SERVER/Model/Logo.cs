using System;
using System.Collections.Generic;

namespace CL.UCN.DISC.PDIS.SCE.Server.Model
{
    public enum Rol
    {
        Academico,
        Funcionario,
        Pregrado,
        Posgrado
    }

    public class Logo
    {

        public string Id { get; set; }

        public string Identificador { get; set; }

        public Rol Rol { get; set; }

        public string Anio { get; set; }

    }
}