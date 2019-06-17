using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using model;

namespace SCEUCN_WEB.Pages
{
    public class VehiculosModel : PageModel
    {

        public List<Vehiculo> vehiculos;
        
        IIceApplication _IceApplication;

        public void OnGet([FromServices] IIceApplication IceApplication)
        {
            _IceApplication = IceApplication;
            var vehiculosTask = _IceApplication.obtenerVehiculos();

            vehiculos = vehiculosTask.Result;
        }
    }
}