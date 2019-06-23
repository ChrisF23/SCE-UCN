using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using SCEUCN_WEB.Controller;


namespace SCEUCN_WEB.Pages
{
    public class VehiculoModel : PageModel
    {

        /// <summary>
        /// La instancia de la aplicacion de Ice.
        /// </summary>
        private IIceApplication _IceApplication;


        /// <summary>
        /// El identificador de este vehiculo, es decir, su placa.
        /// NO.CAMBIAR.NOMBRE.
        /// </summary>
        /// <value></value>
        [BindProperty(SupportsGet = true)]
        public string Id { get; set; }

        public Model.Vehiculo Vehiculo { get; set; }

        /// <summary>
        /// Estado de la peticion al servidor. Waiting es el estado por defecto.
        /// </summary>
        /// <value></value>
        public IceRequestState RequestState { get; set; } = IceRequestState.Waiting;

        public VehiculoModel([FromServices] IIceApplication IceApplication) => this._IceApplication = IceApplication;

        public async Task OnGetAsync() 
        {
            try
            {
                // Conseguir vehiculo de ice.
                var vehiculoIce = await this._IceApplication.obtenerVehiculo(this.Id);

                // Convertirlo a cs.
                this.Vehiculo = ModelConverter.Convert(vehiculoIce);

                // Si todo salio bien, cambiar a estado completado.
                RequestState = IceRequestState.Completed;
            }
            catch (System.Exception)
            {
                // En caso de error, cambiar a estado de error.
                RequestState = IceRequestState.Error;                
            }
        }
    }
}