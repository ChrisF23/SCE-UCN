using System;
using System.ComponentModel.DataAnnotations;

namespace SCEUCN_WEB.Model
{
    public enum Tipo { Auto, Camioneta, Moto }

    public class Vehiculo
    {

        public string Id { get; set; }

        public string Anio{ get; set;}

        public string Placa { get; set; }

        public Persona Persona { get; set; }
                
        public string Marca { get; set; }
        
        public Tipo Tipo { get; set; }

        //public List<Logo> Logos{ get; set;}
    }
}