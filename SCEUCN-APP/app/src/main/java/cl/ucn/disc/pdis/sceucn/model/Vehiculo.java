/*
 * Created by Christian Farias on 22-04-19 2:02
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 22-04-19 2:02
 */

package cl.ucn.disc.pdis.sceucn.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity(tableName="vehiculo")
public class Vehiculo {

    /**
     * La persona a la cual le pertenece este vehiculo.
     */
    @Embedded(prefix = "persona_")
    @NonNull
    private Persona persona;

    /**
     * La placa del vehiculo. Ej: FBXS22.
     */
    @PrimaryKey
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
    @Embedded(prefix = "logo_")
    private List<Logo> logos;
}
