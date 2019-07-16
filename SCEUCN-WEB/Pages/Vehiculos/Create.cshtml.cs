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
        /// El vehiculo que se va a crear.
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
            
            webController.AddVehiculo(Vehiculo.Rut, Vehiculo.Placa, Vehiculo.Marca, Vehiculo.Tipo);

            return RedirectToPage("./Index");
        }
    }

    /// <summary>
    /// Clase de Validacion para el Modelo Vehiculo.
    /// </summary>
    public class VehiculoValidationModel
    {
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