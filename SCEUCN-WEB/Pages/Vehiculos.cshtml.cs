using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using SCEUCN_WEB.Model;
using SCEUCN_WEB.Controller;

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


        /// <summary>
        /// Estado de la peticion al servidor. Waiting es el estado por defecto.
        /// </summary>
        /// <value></value>
        public IceRequestState RequestState { get; set; } = IceRequestState.Waiting;

        public async Task OnGetAsync()
        {
            try
            {
                List<model.Vehiculo> vehiculosIce = await this._IceApplication.obtenerVehiculos();
            
                foreach (var vehiculo in vehiculosIce)
                {
                    Vehiculos.Add(Controller.ModelConverter.Convert(vehiculo));
                }

                RequestState = IceRequestState.Completed;
            }
            catch (System.Exception)
            {
                RequestState = IceRequestState.Error;
            }
        }
    }
}