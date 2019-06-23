using System;
using System.Collections.Generic;
using System.Linq;
using Ice;
using model;

namespace SCEUCN_SERVER
{
    class ControladorWebI : model.ControladorWebDisp_
    {
        private ISystem system;

        public ISystem System { get => system; set => system = value; }

        public override void agregarVehiculo(string rutPersona, string placa, string marca, Tipo tipo, Current current = null)
        {
            throw new NotImplementedException();
        }

        public override List<Persona> obtenerPersonas(Current current = null)
        {
            Program.PrintMessage("Enviando personas...");

            List<Persona> personas = new List<Persona>();

            foreach (var persona in system.GetPersonas())
            {
                personas.Add(ModelConverter.Convert(persona));        
            }

            Program.PrintMessage("Se enviaron las personas! ({0} Personas)", personas.Count());
            return personas;
        }

        public override List<Registro> obtenerRegistros(Current current = null)
        {
            Program.PrintMessage("Enviando registros...");

            List<Registro> registros = new List<Registro>();

            foreach (var registro in system.GetRegistros())
            {
                registros.Add(ModelConverter.Convert(registro));        
            }

            Program.PrintMessage("Se enviaron los registros! ({0} Registros)", registros.Count());
            return registros;
        }

        public override Vehiculo obtenerVehiculo(string placa, Current current = null)
        {
            Program.PrintMessage("Identificando placa...");

            var vehiculo = System.GetVehiculo(placa);

            if (vehiculo == null){
                Program.PrintMessage("No se encontro al vehiculo con la placa [{0}]...", placa);
                return null;
            }

            Program.PrintMessage("Se envio el vehiculo con placa [{0}]!", placa);
            return ModelConverter.Convert(vehiculo);
        }

        public override List<Vehiculo> obtenerVehiculos(Current current = null)
        {
            Program.PrintMessage("Enviando vehiculos...");

            List<Vehiculo> vehiculos = new List<Vehiculo>();

            foreach (var vehiculo in system.GetVehiculos())
            {
                vehiculos.Add(ModelConverter.Convert(vehiculo));        
            }

            Program.PrintMessage("Se enviaron los vehiculos! ({0} Vehiculos)", vehiculos.Count());
            return vehiculos;
        }
    }
}