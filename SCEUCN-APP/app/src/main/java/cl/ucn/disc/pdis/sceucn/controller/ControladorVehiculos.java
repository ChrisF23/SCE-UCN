/*
 * Created by Christian Farias on 22-04-19 2:31
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 22-04-19 2:31
 */

package cl.ucn.disc.pdis.sceucn.controller;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import java.util.ArrayList;
import java.util.List;

import cl.ucn.disc.pdis.sceucn.ice.model.Controlador;
import cl.ucn.disc.pdis.sceucn.ice.model.ControladorPrx;
import cl.ucn.disc.pdis.sceucn.model.Persona;
import cl.ucn.disc.pdis.sceucn.model.Vehiculo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControladorVehiculos {

    // Patron Singleton.
    private static final ControladorVehiculos INSTANCE = new ControladorVehiculos();

    private ControladorVehiculos(){
        establecerConexion();
    }

    public static ControladorVehiculos getInstance(){
        return INSTANCE;
    }

    private static ControladorPrx controladorPrx;

    private void establecerConexion(){
        // Properties
        final Properties properties = Util.createProperties();
        properties.setProperty("Ice.Package.model", "cl.disc.ucn.pdis.news.zeroice");
        // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
        properties.setProperty("Ice.Trace.Admin.Properties", "1");
        properties.setProperty("Ice.Trace.Locator", "2");
        properties.setProperty("Ice.Trace.Network", "3");
        properties.setProperty("Ice.Trace.Protocol", "1");
        properties.setProperty("Ice.Trace.Slicing", "1");
        properties.setProperty("Ice.Trace.ThreadPool", "1");
        properties.setProperty("Ice.Compression.Level", "9");

        InitializationData initializationData = new InitializationData();
        initializationData.properties = properties;

        log.debug("Estableciendo conexion...");

        try (Communicator communicator = Util.initialize(initializationData)) {

            final ObjectPrx proxy = communicator.stringToProxy(Controlador.class.getName() + ":default -p 10000 -z");
            final ControladorPrx ctrlPrx = ControladorPrx.checkedCast(proxy);

            if (ctrlPrx == null) {
                throw new IllegalStateException("Proxy invalido!");
            }

            log.debug("Conexion establecida.");

            controladorPrx = ctrlPrx;

            //final Article[] articles = controladorPrx.getTopArticles();
            //log.debug("Articles: {}", articles.length);
        }

        log.debug("Ok.");
    }

    public List<Vehiculo> obtenerListadoVehiculos(){
        if (controladorPrx == null) {
            log.debug("El proxy es nulo.");
            return null;
        }

        log.debug("Obteniendo listado de vehiculos...");
        List<Vehiculo> vehiculos = new ArrayList<>();

        cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo[] vehiculosIce = controladorPrx.obtenerListadoVehiculos();

        // Convertir vehiculos desde el modelo Ice al modelo Java y agregarlos a la lista.
        for (cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo vehiculoIce :
                vehiculosIce) {
            vehiculos.add(build(vehiculoIce));
        }

        return vehiculos;
    }

    private Persona build(cl.ucn.disc.pdis.sceucn.ice.model.Persona personaIce){

        Persona persona = new Persona(personaIce.rut, personaIce.nombre);

        // Sets
        persona.setTelefono(personaIce.telefono);

        return persona;
    }

    private Vehiculo build(cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo vehiculoIce){

        Vehiculo vehiculo = new Vehiculo(build(vehiculoIce.persona), vehiculoIce.patente);

        // Sets
        vehiculo.setMarca(vehiculoIce.marca);

        return vehiculo;
    }


}
