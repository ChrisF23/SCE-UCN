package cl.ucn.disc.pdis.sceucn.controller;


import cl.ucn.disc.pdis.sceucn.ice.model.Registro;

/**
 * Clase encargada de convertir ciertos objetos del modelo java al modelo ice y viceversa.
 */
public class ModelConverter {

    public static Registro convert(cl.ucn.disc.pdis.sceucn.model.Registro registroJ){

        Registro registroI = new Registro();
        registroI.patente = registroJ.getPatente();
        registroI.fechaUnixMilisegundos = String.valueOf(registroJ.getFecha().getTime());

        return registroI;
    }
}
