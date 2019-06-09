/*
 *  Created by Christian Farias on 05-06-19 10:13
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 05-06-19 1:33
 *
 */

/**
 * (c) 2019 Projecto Desarrollo e Integracion de Soluciones.
 */
package cl.ucn.disc.pdis.sceucn.new_controller;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.List;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Properties;

import cl.ucn.disc.pdis.sceucn.ice.model.ControladorPrx;
import cl.ucn.disc.pdis.sceucn.ice.model.Porteria;
import cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo;
import lombok.extern.slf4j.Slf4j;

/**
 * Main Ice Controller
 */
@Slf4j
public final class Ice {

    /**
     * The Communicator
     */
    private Communicator communicator;

    /**
     * La IP del Servidor.
     */
    private String serverIP;

    /**
     * Constructor
     */
    public Ice() {
    }

    /**
     * Initialize the Ice framewoork
     */
    public void init(String serverIP) {

        this.serverIP = serverIP;

        final InitializationData initializationData = new InitializationData();
        initializationData.properties = this.getProperties();

        this.communicator = Util.initialize(initializationData);
    }

    /**
     * Destroy the comunicator
     */
    public void destroy() {
        if (this.communicator != null) {
            communicator.close();
            communicator.destroy();
        }
    }

    /**
     * @return the {@link ControladorPrx}.
     */
    private ControladorPrx getControladorProxy() {

        String ipconfig = null;

        if (serverIP != null){
            ipconfig = "-h " + serverIP;
        }

        // The Proxy
        final ObjectPrx proxy = communicator.stringToProxy(String.format("Controlador:default %s -p 10000 -z", ipconfig));

        // The Specific Proxy
        return ControladorPrx.checkedCast(proxy);
    }

    /**
     * @return the Properties
     */
    private Properties getProperties() {

        // Properties
        final Properties properties = Util.createProperties();

        // FIXME: Need the same name of the Model.ice configuration
        properties.setProperty("Ice.Package.model", "cl.ucn.disc.pdis.sceucn.ice");

        // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
        properties.setProperty("Ice.Trace.Admin.Properties", "1");
        properties.setProperty("Ice.Trace.Locator", "2");
        properties.setProperty("Ice.Trace.Network", "3");
        properties.setProperty("Ice.Trace.Protocol", "1");
        properties.setProperty("Ice.Trace.Slicing", "1");
        properties.setProperty("Ice.Trace.ThreadPool", "1");
        properties.setProperty("Ice.Compression.Level", "9");
        properties.setProperty("Ice.Plugin.Slf4jLogger.java", "cl.ucn.disc.pdis.sceucn.new_controller.Slf4jLoggerPluginFactory");

        return properties;
    }


    public List<Vehiculo> obtenerVehiculos(){
        return this.getControladorProxy().obtenerVehiculos();
    }

    public void registrarIngreso(String placa, Porteria porteria){
        //getControladorProxy().registrarIngreso(placa, porteria);
        this.getControladorProxy().registrarIngreso(placa, porteria);
    }

    public void registrarIngreso(String placa, Porteria porteria, String fecha){
        this.getControladorProxy().registrarIngresoOffline(placa, porteria, fecha);
    }

    /**
     * Entry point!
     */
    public static void main(String[] args) {
        log.debug("Starting the client ..");

        final Ice ice = new Ice();
        ice.init("192.168.0.10");

        final List<Vehiculo> vehiculos = ice.getControladorProxy().obtenerVehiculos();
        log.debug("Vehiculos size: {}", vehiculos.size());

        for (Vehiculo vehiculo : vehiculos) {
            log.debug("Vehiculo: {}", vehiculo);
        }

        ice.destroy();

        log.debug("Client end.");
    }

}
