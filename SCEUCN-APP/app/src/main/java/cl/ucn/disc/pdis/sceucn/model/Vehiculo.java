/*
 * Created by Christian Farias on 22-04-19 2:02
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 22-04-19 2:02
 */

package cl.ucn.disc.pdis.sceucn.model;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class Vehiculo {

    /**
     * La persona a la cual le pertenece este vehiculo.
     */
    @NonNull
    private Persona persona;

    /**
     * La placa del vehiculo. Ej: FBXS22.
     */
    @NonNull
    private String placa;

    /**
     * El anio de...?
     */
    private String anio;

    /**
     * La marca del vehiculo. Ej: Suzuki.
     */
    private String marca;

    /**
     * El tipo de vehiculo. Ej: Auto.
     */
    private Tipo tipo;

    /**
     * Los logos de este vehiculo.
     */
    private List<Logo> logos;
}
