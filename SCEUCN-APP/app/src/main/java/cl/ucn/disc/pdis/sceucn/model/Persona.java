/*
 * Created by Christian Farias on 21-04-19 1:30
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 21-04-19 1:29
 */

package cl.ucn.disc.pdis.sceucn.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
//@Builder
public class Persona {

    @NonNull
    private String rut;

    @NonNull
    private String nombre;

    private String depto;

    private String unidad;

    private String oficina;

    private String rol;

    private String cargo;

    private String telefono;

    private Planta planta;

    private Date inicioContrato;

    private Date terminoContrato;
}
