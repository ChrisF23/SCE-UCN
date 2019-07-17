
using System.Collections.Generic;
using System.Threading.Tasks;
using CL.UCN.DISC.PDIS.SCE.Web.Controller;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;

namespace CL.UCN.DISC.PDIS.SCE.Web.Pages.Vehiculos {

    public class CreateModel : PageModel
    {
        /// <summary>
        /// La instancia de la aplicacion de Ice.
        /// </summary>
        private IWebController webController;

        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="IceApplication"></param>
        public CreateModel([FromServices] IWebController webController) => this.webController = webController;

        
        /// <summary>
        /// El modelo de validacion para el vehiculo que se quiere crear.
        /// </summary>
        /// <value></value>
        [BindProperty]
        public VehiculoValidationModel Vehiculo { get; set; }


        public List<SelectListItem> Ruts { get; } = new List<SelectListItem>();


        /// <summary>
        /// On Get.
        /// </summary>
        /// <returns></returns>
        public IActionResult OnGet()
        {
            var personas = webController.GetPersonas();
            

            foreach (var persona in personas)
            {
                Ruts.Add(new SelectListItem(persona.rut, persona.rut));
            }

            return Page();
        }

        /// <summary>
        /// On Post.
        /// If there are no model errors, the data is saved, and the browser is redirected to the Index page.
        /// </summary>
        /// <returns></returns>
        public async Task<IActionResult> OnPostAsync()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }
            
            webController.AddOrUpdateVehiculo(Vehiculo.Rut, Vehiculo.Placa, Vehiculo.Marca, Vehiculo.Tipo, Vehiculo.Anio.ToString());

            return RedirectToPage("./Index");
        }
    }
}