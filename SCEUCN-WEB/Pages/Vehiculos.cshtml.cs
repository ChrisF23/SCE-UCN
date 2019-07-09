using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using CL.UCN.DISC.PDIS.SCE.Web.Controller;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace CL.UCN.DISC.PDIS.SCE.Web.Pages {

    public class VehiculosModel : PageModel {

        /// <summary>
        /// La instancia de la aplicacion de Ice.
        /// </summary>
        private IWebController webController;

        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="IceApplication"></param>
        public VehiculosModel([FromServices] IWebController webController) => this.webController = webController;

        /// <summary>
        /// La lista de vehiculos.
        /// </summary>
        /// <typeparam name="Vehiculo"></typeparam>
        /// <returns></returns>
        public List<Vehiculo> Vehiculos { get; set; } = new List<Vehiculo>();


        /// <summary>
        /// La lista de personas.
        /// </summary>
        /// <typeparam name="Persona"></typeparam>
        /// <returns></returns>
        public List<Persona> Personas { get; set; } = new List<Persona>();

        public void OnGet() {
            Vehiculos = webController.GetVehiculos();
            Personas = webController.GetPersonas();
        }
    }
}