using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using CL.UCN.DISC.PDIS.SCE.Web.Controllers;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce;

namespace CL.UCN.DISC.PDIS.SCE.Web.Pages
{
    public class VehiculosModel : PageModel
    {
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

        public void OnGet()
        {
            Vehiculos = webController.GetVehiculos();
        }
    }
}