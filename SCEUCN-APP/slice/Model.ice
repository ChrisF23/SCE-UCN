["java:package:cl.disc.ucn.pdis.sceucn"]
module model
{
    enum TipoVehiculo {Auto, Camioneta, Moto}
    enum TipoLogo {Funcionario, Estudiante}
    enum Planta {Oficial, Fija, Temporal}

    class Vehiculo
    {
        string anio;
        string marca;
        string patente;
        TipoVehiculo tipo;
    }

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
}