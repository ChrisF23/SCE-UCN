using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using CL.UCN.DISC.PDIS.SCE.Web.Controller;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Mvc.Rendering;


namespace CL.UCN.DISC.PDIS.SCE.Web.Pages.Vehiculos {
    /// <summary>
    /// Clase de Validacion para el Modelo Vehiculo.
    /// </summary>
    public class VehiculoValidationModel
    {
        public VehiculoValidationModel()
        {

        }

        //Usar en Personas/Create -> [RegularExpression(@"^(\d{1}|\d{2})\.(\d{3}\.\d{3}-)([K]{1}$|\d{1}$)", ErrorMessage = "El rut no cumple el formato requerido.")]
        [Required(ErrorMessage = "Campo requerido.")]
        public string Rut { get; set; }
        
        [RegularExpression(@"^([A-Z]{2}-)(([A-Z]{2}-)|([0-9]{2}-))([0-9]{2}$)", ErrorMessage = "La placa no cumple el formato requerido.")]
        [Required(ErrorMessage = "Campo requerido.")]
        public string Placa { get; set; }

        [Required(ErrorMessage = "Campo requerido.")]
        public Tipo Tipo { get; set; }

        [Required(ErrorMessage = "Campo requerido.")]
        public string Marca { get; set; }

        [Required(ErrorMessage = "Campo requerido.")]
        public int Anio { get; set; }
    }
}