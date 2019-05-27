/*
 * Created by Christian Farias on 21-04-19 1:30
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 21-04-19 1:29
 */

package cl.ucn.disc.pdis.sceucn.model;

import lombok.Data;
import lombok.NonNull;

/**
 * La persona duenia de un vehiculo.
 */
@Data
public class Persona {

    /**
     * El rut de la persona. Ej: '12.123.132-3'.
     */
    @NonNull
    private String rut;

    /**
     * Los nombres de la persona. Ej: 'Juan Andres'.
     */
    @NonNull
    private String nombres;

    /**
     * Los apellidos de la persona. Ej: 'Visalovic Terreas'.
     */
    @NonNull
    private String apellidos;

    /**
     * El correo electronico de la persona. Ej: 'jisalovic@ucn.cl'.
     */
    private String email;

    /**
     * El numero telefonico. Ej. '+569 1234 5678'.
     */
    private String telefono;

    /**
     * Unidad a la que pertenece la persona.
     * Ej: 'Departamento de Ingenieria de Sistemas y Computacion'.
     */
    private String unidad;

    /**
     * Oficina de esta persona. Ej: 'Pabellon Y1, Oficina 305'.
     */
    private String oficina;

    /**
     * El rol que desempenia esta persona.
     */
    private Rol rol;

    /**
     * Tipo de contrato.
     */
    private Contrato contrato;
}
