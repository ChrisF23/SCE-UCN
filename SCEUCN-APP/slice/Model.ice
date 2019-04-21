//["java:package:cl.disc.ucn.pdis.sceucn"]
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

    sequence<Vehiculo> Vehiculos;

    interface Controlador
    {
        // Obtiene un listado con todos los vehiculos registrados en la BD.
        Vehiculos obtenerListadoVehiculos();

        // Busca y retorna un vehiculo dada su patente.
        Vehiculo buscarVehiculo(string patente);

        // Registra el ingreso de un vehiculo dada su patente.
        void registrarIngreso(string patente);
    }
}