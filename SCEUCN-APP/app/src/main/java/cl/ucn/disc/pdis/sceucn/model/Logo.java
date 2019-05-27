/*
 *  Created by Christian Farias on 27-05-19 1:15
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 27-05-19 1:15
 *
 */

package cl.ucn.disc.pdis.sceucn.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Autoadhesivo pegado en el parabrisas del vehiculo.
 */
@Data
public class Logo {

    /**
     * Identificador del logo. Ej: 'L1-1210, 19PS0182'.
     */
    @NonNull
    private String identificador;

    /**
     * El rol del duenio del vehiculo al que esta asociado este logo.
     */
    @NonNull
    private Rol rol;

    /**
     * Anio en que se emitio este logo.
     */
    private String anio;
}
