/*
 * Created by Christian Farias on 22-04-19 2:31
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 22-04-19 2:31
 */

package cl.ucn.disc.pdis.sceucn.controller;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Connection;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import java.util.ArrayList;
import java.util.List;
import cl.ucn.disc.pdis.sceucn.ice.model.ControladorPrx;
import cl.ucn.disc.pdis.sceucn.model.Porteria;
import cl.ucn.disc.pdis.sceucn.model.Vehiculo;

import static cl.ucn.disc.pdis.sceucn.MainActivity.SERVER_IP;

/**
 * Clase encargada de manejar la interaccion entre el cliente y el servidor.
 * Basada en el ejemplo de zeroc-ice:
 * android/library/src/main/java/com/zeroc/library/controller/QueryController.java.
 */
public class ControladorVehiculos {

    /**
     * El listener de eventos.
     */
    public interface EventListener {
        /**
         * Evento que se levanta al obtener el listado de vehiculos desde el servidor.
         */
        void onListObtained(List<Vehiculo> listVehiculos);

        /**
         * Evento que se levanta cuando ocurre un error al comunicarse con el servidor.
         * @param error El error.
         */
        void onError(String error);

        /**
         * Evento que se levanta cuando se obtiene el proxy desde el servidor.
         */
        void onProxyObtained();
    }

    /**
     * El listado de vehiculos que obtiene desde el servidor.
     */
    private List<Vehiculo> listadoVehiculos = new ArrayList<>();

    /**
     * El listener de eventos de este controlador.
     */
    private EventListener listener;

    /**
     * El handler.
     */
    private Handler handler;

    /**
     * El proxy.
     */
    private ControladorPrx controladorPrx;

    private Communicator iceCommunicator;


    /**
     * El constructor de este controlador de vehiculos.
     * @param listener El Listener.
     */
    public ControladorVehiculos(EventListener listener){
        // Config:
        this.handler = new Handler();
        this.listener = listener;

        InitializationData initData = new InitializationData();
        initData.properties = Util.createProperties();
        // FIXED: Packages.
        initData.properties.setProperty("Ice.Package.model", "cl.ucn.disc.pdis.sceucn.ice");

        // ..?
        initData.dispatcher = (Runnable runnable, Connection connection) ->
        {
            handler.post(runnable);
        };

        iceCommunicator = Util.initialize(initData);

        // Ice init:
        AsyncTask.execute(()->{
            try {

                //iceCommunicator = Util.initialize(initData);

                // Obtener proxy desde el servidor.
                ObjectPrx proxy = iceCommunicator.stringToProxy("Controlador:tcp -h " + SERVER_IP + " -p 10000 -z");

                // Establecer timeout (10 segundos).
                proxy = proxy.ice_timeout(10000);

                // Obtener controlador a partir del proxy.
                controladorPrx = ControladorPrx.checkedCast(proxy);

                if(controladorPrx == null)
                {
                    postError("El proxy fue nulo.");
                }

                // Para realizar las operaciones del proxy, estas deben ser Synchronized.
                // Una vez se obtiene el proxy, se pueden realizar las operaciones.
                postProxyObtained();


            } catch (Exception e){
                postError("No se pudo establecer la conexion...");
            }
        });

    }

    /**
     * Levanta el evento de obtencion del listado de vehiculos.
     */
    synchronized protected void postListObtained(List<Vehiculo> listVehiculos){
        if (listener != null){
            handler.post(() -> listener.onListObtained(listVehiculos));
        }
    }

    /**
     * Levanta el evento de error.
     */
    synchronized protected void postError(final String error){
        if (listener != null){
            handler.post(() -> listener.onError(error));
        }
    }

    synchronized protected void postProxyObtained(){
        if (listener != null){
            handler.post(() -> listener.onProxyObtained());
        }
    }

    /**
     * Asigna una implementacion de listener a este controlador.
     * @param listener El Listener.
     */
    synchronized public void setListener(EventListener listener){
        this.listener = listener;
    }

    /**
     * Obtiene el listado de vehiculos desde el servidor.
     */
    synchronized public void obtenerListadoVehiculos(){
        if (controladorPrx != null){
            controladorPrx.obtenerVehiculosAsync().whenComplete((res, ex) -> {
                if (ex != null){
                    // Hubo un error; Levantar evento.
                    postError("Hubo un error al obtener el listado de vehiculos...");
                    postError(ex.toString());

                    // ERROR: NO VALUE FACTORY EXCEPTION...
                    // Causa: En Model.ice -> ["java:package:cl.ucn.disc.pdis.sceucn.ice"]
                    // Solucion: Reemplazar por ["Ice.Package.model=cl.ucn.disc.pdis.sceucn.ice"]

                } else {
                    synchronized (this){
                        // Llenar el listado de vehiculos.
                        for (cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo vehiculo : res) {
                            Log.d(">>>>>>>>>>>", "Placa: " + vehiculo.placa);
                            this.listadoVehiculos.add(ModelConverter.convert(vehiculo));
                        }

                        // Se obtuvo la lista; Levantar evento.
                        postListObtained(this.listadoVehiculos);
                    }
                }
            });
        } else {
            postError("No hay conexion...");
        }
    }

    synchronized public void registrarIngreso(String placa, Porteria porteria){
        if (controladorPrx != null){
            // FIXME: Crear convertidor de enums.
            //model.Porteria porteriaIce = model.Porteria.Norte;
            controladorPrx.registrarIngreso(placa, cl.ucn.disc.pdis.sceucn.ice.model.Porteria.Norte);
        } else {
            postError("No hay conexion...");
        }
    }

}
