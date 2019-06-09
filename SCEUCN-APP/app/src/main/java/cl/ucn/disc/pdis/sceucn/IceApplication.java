/*
 *  Created by Christian Farias on 05-06-19 10:29
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 05-06-19 10:29
 *
 */

package cl.ucn.disc.pdis.sceucn;

import android.app.Application;

import java.util.List;

import cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo;
import cl.ucn.disc.pdis.sceucn.new_controller.Ice;

public class IceApplication extends Application {

    /**
     * La conexion con el Framework ICE.
     */
    private Ice ice;

    /**
     * Crea la instancia de la conexion.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Iniciar la conexion de ice.
        if (this.ice == null) {
            this.ice = new Ice();
        }
    }

    /**
     * Inicia la conexion con el servidor.
     * @param serverIP La ip del servidor.
     */
    public void initializeIce(final String serverIP) {
        if (this.ice != null){
            this.ice.init(serverIP);
            return;
        }

        throw new RuntimeException("ICE is null");
    }

    public List<Vehiculo> obtenerVehiculos(){
        if (this.ice != null){
            return this.ice.obtenerVehiculos();
        }

        throw new RuntimeException("ICE is null");
    }

    /**
     * Termina la conexion.
     */
    @Override
    public void onTerminate() {
        if (this.ice != null) {
            this.ice.destroy();
            this.ice = null;
        }

        super.onTerminate();
    }
}
