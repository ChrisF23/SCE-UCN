using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using CL.UCN.DISC.PDIS.SCE.Web.Controller;

namespace CL.UCN.DISC.PDIS.SCE.Web.Pages.Vehiculos
{
    public class DetailsModel : PageModel
    {

        /// <summary>
        /// La instancia de la aplicacion de Ice.
        /// </summary>
        private IWebController webController;

        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="IceApplication"></param>
        public DetailsModel([FromServices] IWebController webController) => this.webController = webController;


        /// <summary>
        /// El identificador de este vehiculo, es decir, su placa.
        /// NO.CAMBIAR.NOMBRE.
        /// </summary>
        /// <value></value>
        [BindProperty(SupportsGet = true)]
        public string Id { get; set; }

        public Vehiculo Vehiculo { get; set; }

        public IActionResult OnGet() 
        {
            if (this.Id == null)
            {
                return NotFound();
            }

            Vehiculo = this.webController.GetVehiculo(this.Id);

            if (Vehiculo == null)
            {
                return NotFound();
            }

            return Page();
        }
    }
}