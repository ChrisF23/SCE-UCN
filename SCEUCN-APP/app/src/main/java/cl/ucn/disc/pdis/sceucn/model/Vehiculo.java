/*
 * Created by Christian Farias on 22-04-19 2:02
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 22-04-19 2:02
 */

package cl.ucn.disc.pdis.sceucn.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Vehiculo {

    @NonNull
    private Persona persona;

    @NonNull
    private String patente;

    private String marca;

    // private String anio;

    private TipoVehiculo tipo;
}
