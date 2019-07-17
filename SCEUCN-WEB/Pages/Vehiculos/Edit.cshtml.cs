using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using CL.UCN.DISC.PDIS.SCE.Web.Controller;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;

namespace CL.UCN.DISC.PDIS.SCE.Web.Pages.Vehiculos
{
    public class EditModel : PageModel
    {
         /// <summary>
        /// La instancia de la aplicacion de Ice.
        /// </summary>
        private IWebController webController;

        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="IceApplication"></param>
        public EditModel([FromServices] IWebController webController) => this.webController = webController;

        
        /// <summary>
        /// Validacion del vehiculo.
        /// </summary>
        /// <value></value>
        [BindProperty]
        public VehiculoValidationModel Vehiculo { get; set; }

        public List<SelectListItem> Ruts { get; } = new List<SelectListItem>();

        /// <summary>
        /// El identificador de este vehiculo, es decir, su placa.
        /// NO.CAMBIAR.NOMBRE.
        /// </summary>
        /// <value></value>
        [BindProperty(SupportsGet = true)]
        public string Id { get; set; }

        public async Task<IActionResult> OnGetAsync()
        {
            var personas = webController.GetPersonas();

            foreach (var persona in personas)
            {
                Ruts.Add(new SelectListItem(persona.rut, persona.rut));
            }

            // Obtener el vehiculo.
            Server.ZeroIce.Model.Vehiculo _vehiculo = this.webController.GetVehiculo(Id);

            if (_vehiculo == null)
            {
                return RedirectToPage("/Index");
            }

            // Si no es nulo, convertirlo al modelo con validacion.
            // Esto es necesario para utilizar las data annotations que permiten
            // Validar el vehiculo antes de enviarlo al backend.
            Vehiculo = new VehiculoValidationModel() {
                Rut = _vehiculo.persona.rut,
                Placa = _vehiculo.placa,
                Tipo = _vehiculo.tipo,
                Marca = _vehiculo.marca,
                Anio = Int32.Parse(_vehiculo.anio)
            };

            return Page();
        }

        /// <summary>
        /// FIXME: Por algun motivo, al hacer post, no puedo hacer que se redirija a otra pagina (pero si puedo en OnPost de Vehiculos/Create).!--..
        /// </summary>
        /// <returns></returns>
        public async Task<IActionResult> OnPostAsync()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }
            
            webController.AddOrUpdateVehiculo(Vehiculo.Rut, Vehiculo.Placa, Vehiculo.Marca, Vehiculo.Tipo, Vehiculo.Anio.ToString());

            return RedirectToPage();
        }       
    }
}