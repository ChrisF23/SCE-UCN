/**
 * (c) 2019 Projecto Desarrollo e Integracion de Soluciones.
 */
package cl.ucn.disc.pdis.sceucn;

import java.util.List;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Properties;

import cl.ucn.disc.pdis.sceucn.ice.model.Controlador;
import cl.ucn.disc.pdis.sceucn.ice.model.ControladorPrx;
import cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo;
import jdk.internal.jline.internal.Log;
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
     * Initialize the Ice framewoork
     */
    public void init() {

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
    public ControladorPrx getControladorProxy() {

        // The Proxy
        final ObjectPrx proxy = communicator.stringToProxy("TheControlador:default -p 10000 -z");

        // The Specific Proxy
        final ControladorPrx controlador = ControladorPrx.checkedCast(proxy);

        return controlador;

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
        properties.setProperty("Ice.Plugin.Slf4jLogger.java", "cl.ucn.disc.pdis.sceucn.Slf4jLoggerPluginFactory");

        return properties;
    }

    /**
     * Entry point!
     */
    public static void main(String[] args) {
        log.debug("Starting the client ..");

        final Ice ice = new Ice();
        ice.init();

        final List<Vehiculo> vehiculos = ice.getControladorProxy().obtenerVehiculos();
        log.debug("Vehiculos size: {}", vehiculos.size());

        for (Vehiculo vehiculo : vehiculos) {
            log.debug("Vehiculo: {}", vehiculo);
        }

        ice.destroy();

        log.debug("Client end.");
    }

}
