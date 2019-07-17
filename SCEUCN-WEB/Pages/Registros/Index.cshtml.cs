using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using CL.UCN.DISC.PDIS.SCE.Web.Controller;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace CL.UCN.DISC.PDIS.SCE.Web.Pages.Registros {

    public class IndexModel : PageModel
    {
        /// <summary>
        /// La instancia de la aplicacion de Ice.
        /// </summary>
        private IWebController webController;

        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="IceApplication"></param>
        public IndexModel([FromServices] IWebController webController) => this.webController = webController;

        /// <summary>
        /// La lista de registros.
        /// </summary>
        /// <typeparam name="Registros"></typeparam>
        /// <returns></returns>
        public IList<Registro> Registros { get; set; } = new List<Registro>();


        public void OnGet() {
            Registros = webController.GetRegistros();
            Registros.OrderByDescending(r => r.fecha);
        }
    }
}