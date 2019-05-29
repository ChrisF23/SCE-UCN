using SCEUCN_SERVER.Model;
using System;
using System.Collections.Generic;
using System.Linq;

namespace SCEUCN_SERVER
{
    /// <summary>
    /// Clase que permite crear datos demo.
    /// </summary>
    class DemoDataCreator
    {
        private Random rng;

        public List<Persona> Personas { get; set; }

        public List<Vehiculo> Vehiculos { get; set; }
        
        private List<string> Placas;

        private List<string> Marcas;

        private List<string> Ruts;

        private List<string> Nombres;

        private List<string> Apellidos;
        

        public DemoDataCreator()
        {
            // Crear RNG.
            this.rng = new Random();
            
            // Crear placas y marcas.
            InitPlacas();
            InitMarcas();

            // Crear ruts, nombres y apellidos.
            InitRuts();
            InitNombres();
            InitApellidos();

            // Crear personas.
            InitPersonas();

            // Crear vehiculos.
            InitVehiculos();
        }

        private void InitPersonas()
        {
            Personas = new List<Persona>();

            for (int i = 0; i < Ruts.Count(); i++)
            {
                Persona persona = new Persona { 
                    Rut = Ruts.ElementAt(i), 
                    Nombres = GenerarNombres(), 
                    Apellidos = GenerarApellidos() 
                };

                Personas.Add(persona);
            }
        }

        private void InitVehiculos()
        {
            Vehiculos = new List<Vehiculo>();
            
            for (int i = 0; i < Placas.Count(); i++)
            {
                Vehiculo vehiculo = new Vehiculo {
                    Persona = Personas.ElementAt(rng.Next(Personas.Count())),
                    Placa = Placas.ElementAt(i),
                    Marca = Marcas.ElementAt(rng.Next(Marcas.Count())),
                };

                Vehiculos.Add(vehiculo);
            }
        }

        private string GenerarNombres()
        {
            string nombre1 = Nombres.ElementAt(rng.Next(Nombres.Count()));
            string nombre2 = Nombres.ElementAt(rng.Next(Nombres.Count()));

            if (nombre1 == nombre2) {
                return nombre1;
            }

            return string.Concat(nombre1, " ", nombre2);
        }

        private string GenerarApellidos()
        {
            string apellido1 = Apellidos.ElementAt(rng.Next(Apellidos.Count()));
            string apellido2 = Apellidos.ElementAt(rng.Next(Apellidos.Count()));

            return string.Concat(apellido1, " ", apellido2);
        }


        private void InitPlacas()
        {
            Placas = new List<string>
            {
                "CA-FA-23",
                "FJ-CM-27",
                "DP-UA-13",
                "SW-R2-D2",
                "C3-PO-SW",
                "NG-E1-01",
                "UC-NA-C9",
                "UV-VD-U1"
            };
        }

        private void InitMarcas()
        {
            Marcas = new List<string>
            {
                "BMW",
                "Toyota",
                "Audi",
                "Ferrari",
                "Pagani",
                "Chevrolet",
                "Ford",
                "Kia",
                "Honda"
            };
        }

        private void InitRuts()
        {
            Ruts = new List<string>
            {
                "19691840K",
                "153331290",
                "203784904",
                "13489303K",
                "148294083",
                "160038895",
                "16992003K",
                "148961345"
            };
        }

        private void InitNombres()
        {
            Nombres = new List<string>
            {
                "Juan",
                "Pedro",
                "Diego",
                "Ramon",
                "Matias",
                "Fernando",
                "Gabriel",
                "Patricio",
                "Roberto",
                "Raul",
                "Maria",
                "Christian",
                "Juan"
            };
        }

        private void InitApellidos()
        {
            Apellidos = new List<string>
            {
                "Garcia",
                "Gonzales",
                "Rodriguez",
                "Fernandez",
                "Caimanque",
                "Lopez",
                "Martines",
                "Sanchez",
                "Perez",
                "Gomez",
                "Vazques",
                "Aguila",
                "Ruiz",
                "Hernandez",
                "Muñoz",
                "Astorga",
                "Romero",
                "Gutierrez",
                "Farias",
                "Barraza",
                "Ramos"
            };
        }
        
    }
}