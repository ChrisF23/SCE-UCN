/**
 * (c) 2019 Projecto Desarrollo e Integracion de Soluciones.
 */
package cl.ucn.disc.pdis.sceucn;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Properties;

import cl.ucn.disc.pdis.sceucn.ice.model.Controlador;
import lombok.extern.slf4j.Slf4j;

/**
 * Main Application
 */
@Slf4j
public class App {

    /**
     * Entry point!
     */
    public static void main(String[] args) {
        log.debug("Starting the server ..");

        // Properties
        final Properties properties = Util.createProperties(args);
        properties.setProperty("Ice.Package.model", "cl.disc.ucn.pdis.news.zeroice");
        // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
        properties.setProperty("Ice.Trace.Admin.Properties", "1");
        properties.setProperty("Ice.Trace.Locator", "2");
        properties.setProperty("Ice.Trace.Network", "3");
        properties.setProperty("Ice.Trace.Protocol", "1");
        properties.setProperty("Ice.Trace.Slicing", "1");
        properties.setProperty("Ice.Trace.ThreadPool", "1");
        properties.setProperty("Ice.Compression.Level", "9");
        properties.setProperty("Ice.Plugin.Slf4jLogger.java", "cl.ucn.disc.pdis.sceucn.Slf4jLoggerPluginFactory");

        InitializationData initializationData = new InitializationData();
        initializationData.properties = properties;

        try (Communicator communicator = Util.initialize(initializationData)) {

            log.debug("Communicator ok, building adapter ..");
            final ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("ControladorAdapter",
                    "default -p 10000 -z");
            log.debug("Name [{}].", adapter.getName());

            final Controlador controlador = new ControladorImpl();

            log.debug("Adding controlador to adapter ..");
            final Identity identity = Util.stringToIdentity("TheControlador");
            final ObjectPrx objectPrx = adapter.add(controlador, identity);
            log.debug("Identity Name: [{}].", objectPrx.ice_getIdentity().name);

            adapter.activate();

            log.debug("Waiting for data ..");
            communicator.waitForShutdown();

            log.debug("Something here?");

        }

        log.debug("Server end.");
    }

}
