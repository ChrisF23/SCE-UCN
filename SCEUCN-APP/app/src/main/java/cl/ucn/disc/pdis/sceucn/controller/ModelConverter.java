package cl.ucn.disc.pdis.sceucn.controller;


import cl.ucn.disc.pdis.sceucn.ice.model.Persona;
import cl.ucn.disc.pdis.sceucn.ice.model.Porteria;
import cl.ucn.disc.pdis.sceucn.ice.model.Registro;
import cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo;

/**
 * Clase encargada de convertir ciertos objetos del modelo java al modelo ice y viceversa.
 */
public class ModelConverter {

    /**
     * Convierte un registro java a un registro ice.
     * @param registroJ el registro java.
     * @return el registro ice.
     */
    public static Registro convert(cl.ucn.disc.pdis.sceucn.model.Registro registroJ){

        Registro registroI = new Registro();
        registroI.vehiculo = convert(registroJ.getVehiculo());
        registroI.porteria = convert(registroJ.getPorteria());

        return registroI;
    }

    /**
     * Convierte una porteria java a una porteria ice.
     * @param porteriaJ la porteria java.
     * @return la porteria ice.
     */
    private static Porteria convert(cl.ucn.disc.pdis.sceucn.model.Porteria porteriaJ) {
        return Porteria.valueOf(porteriaJ.name());
    }

    /**
     * Convierte un vehiculo java a un vehiculo ice.
     * @param vehiculoJ el vehiculo java.
     * @return el vehiculo ice.
     */
    private static Vehiculo convert(cl.ucn.disc.pdis.sceucn.model.Vehiculo vehiculoJ) {

        Vehiculo vehiculoI = new Vehiculo();
        vehiculoI.persona = convert(vehiculoJ.getPersona());

        return vehiculoI;
    }

    /**
     * Convierte un vehiculo ice a un vehiculo java.
     * @param vehiculoI el vehiculo ice.
     * @return el vehiculo java.
     */
    public static cl.ucn.disc.pdis.sceucn.model.Vehiculo convert (Vehiculo vehiculoI) {

        cl.ucn.disc.pdis.sceucn.model.Vehiculo vehiculoJ = new cl.ucn.disc.pdis.sceucn.model.Vehiculo(convert(vehiculoI.persona), vehiculoI.placa);

        return vehiculoJ;
    }

    /**
     * Convierte una persona java a una persona ice.
     * @param personaJ la persona java.
     * @return la persona ice.
     */
    public static Persona convert(cl.ucn.disc.pdis.sceucn.model.Persona personaJ) {

        Persona personaI = new Persona();

        personaI.rut = (personaJ.getRut());
        personaI.nombres = (personaJ.getNombres());
        personaI.apellidos = (personaJ.getApellidos());

        return personaI;
    }

    /**
     * Convierte una persona ice a una persona java.
     * @param personaI la persona ice.
     * @return la persona java.
     */
    public static cl.ucn.disc.pdis.sceucn.model.Persona convert(Persona personaI) {

        cl.ucn.disc.pdis.sceucn.model.Persona personaJ = new cl.ucn.disc.pdis.sceucn.model.Persona(personaI.rut, personaI.nombres, personaI.apellidos);
        return personaJ;
    }
}
