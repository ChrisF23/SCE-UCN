/*
 * Created by Christian Farias on 22-04-19 2:31
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 22-04-19 2:31
 */

package cl.ucn.disc.pdis.sceucn.controller;

import android.os.AsyncTask;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cl.ucn.disc.pdis.sceucn.ice.model.Controlador;
import cl.ucn.disc.pdis.sceucn.ice.model.ControladorPrx;
import cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo;
import cl.ucn.disc.pdis.sceucn.model.Persona;
import lombok.extern.slf4j.Slf4j;

import static cl.ucn.disc.pdis.sceucn.MainActivity.SERVER_IP;

public class ControladorVehiculos {

    // TODO: Propiedad 'vehiculos'. Aqui se guardara el resultado de la operacion obtenerListadoVehiculos().
    //List<>

    public static void obtenerListadoVehiculos() {
        AsyncTask.execute(() -> {

            try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize())
            {
                // Create a proxy.
                com.zeroc.Ice.ObjectPrx obj = communicator.stringToProxy("Controlador:tcp -h " + SERVER_IP + " -p 10000 -z");

                // Importante: Establecer tiempo de espera (10 segundos).
                obj = obj.ice_timeout(10000);

                // Downcast obj.
                ControladorPrx controlador = ControladorPrx.checkedCast(obj);

                if (controlador == null)
                {
                    throw new IllegalStateException("Invalid Proxy.");
                }

                // Obtener listado de vehiculos.
                // ...



            } catch (Exception e) {
            }

        });
    }
}
