["java:package:cl.ucn.disc.pdis.sceucn.ice"]

module model
{
    enum TipoVehiculo {Auto, Camioneta, Moto}
    enum TipoLogo {Funcionario, Estudiante}
    enum Planta {Oficial, Fija, Temporal}


    class Logo
    {
        string id;
        TipoLogo tipo;
        string fecha;
    }

    class Persona
    {
        string rut;
        string nombre;
        string depto;
        string unidad;
        string oficina;
        string rol;
        string cargo;
        string telefono;
        Planta planta;

        string inicioContrato;
        string terminoContrato;
    }

    // Una persona tiene 1..* vehiculos. -> Lista Vehiculos en Persona.
    // Un vehiculo pertenece a 1 persona. -> Atributo Persona en Vehiculo.

    class Vehiculo
    {
        Persona persona;
        string anio;
        string marca;
        string patente;
        TipoVehiculo tipo;
    }

    class Registro
    {
        string patente;
        string fechaUnixMilisegundos;
    }


    sequence<Registro> Registros;
    sequence<Vehiculo> Vehiculos;

    interface Controlador
    {
        // Obtiene un listado con todos los vehiculos registrados en la base de datos.
        Vehiculos obtenerListadoVehiculos();

        // Guarda en la base de datos el ingreso de un vehiculo.
        void guardarRegistro(string patente, string fecha);

        // Guarda en la base de datos el ingreso de multiples vehiculos.
        void guardarRegistros(Registros registros);
    }
}