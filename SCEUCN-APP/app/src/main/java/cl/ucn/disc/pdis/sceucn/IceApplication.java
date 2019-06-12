/*
 *  Created by Christian Farias on 05-06-19 10:29
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 05-06-19 10:29
 *
 */

package cl.ucn.disc.pdis.sceucn;

import android.app.Application;
import android.util.Log;

import java.util.List;

import cl.ucn.disc.pdis.sceucn.ice.model.Porteria;
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

    /**
     *
     * @return
     */
    public List<Vehiculo> obtenerVehiculos(){
        if (this.ice != null){
            return this.ice.obtenerVehiculos();
        }

        throw new RuntimeException("Ice is null");
    }

    /**
     *
     * @param placa
     * @param porteria
     */
    public void registrarIngreso(String placa, Porteria porteria){
        if (this.ice != null){
            this.ice.registrarIngreso(placa, porteria);
            return;
        }

        throw new RuntimeException("Ice is null");
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
