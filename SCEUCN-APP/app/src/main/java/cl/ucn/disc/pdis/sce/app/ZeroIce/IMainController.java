/*
 *  Created by Christian Farias on 6/26/19 6:25 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 6/26/19 6:25 PM
 *
 */

package cl.ucn.disc.pdis.sce.app.ZeroIce;

import java.util.List;

import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Porteria;
import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Vehiculo;

/**
 *
 */
public interface IMainController {

    /**
     *
     * @return the {@link List} of {@link Vehiculo}.
     */
    List<Vehiculo> obtenerVehiculos();

    /**
     * Registra un ingreso en el sistema.
     * @param placa a registrar.
     * @param porteria por donde ingreso.
     */
    void registrarIngreso(String placa, Porteria porteria);

    /**
     * Destroy the world!
     */
    void destroy();

}
