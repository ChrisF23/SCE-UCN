using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using SCEUCN_WEB.Model;

namespace SCEUCN_WEB.Pages
{
    public class VehiculosModel : PageModel
    {
        /// <summary>
        /// La instancia de la aplicacion de Ice.
        /// </summary>
        private IIceApplication _IceApplication;

        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="IceApplication"></param>
        public VehiculosModel([FromServices] IIceApplication IceApplication) => this._IceApplication = IceApplication;

        /// <summary>
        /// La lista de vehiculos.
        /// </summary>
        /// <typeparam name="Vehiculo"></typeparam>
        /// <returns></returns>
        public List<Vehiculo> Vehiculos { get; set; } = new List<Vehiculo>();


        public async Task OnGetAsync()
        {
            List<model.Vehiculo> vehiculosIce = await _IceApplication.obtenerVehiculos();
            
            foreach (var vehiculo in vehiculosIce)
            {
                Vehiculos.Add(Controller.ModelConverter.Convert(vehiculo));
            }
        }
    }
}