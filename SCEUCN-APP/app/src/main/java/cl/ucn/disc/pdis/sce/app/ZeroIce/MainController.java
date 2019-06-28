package cl.ucn.disc.pdis.sce.app.ZeroIce;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ConnectTimeoutException;
import com.zeroc.Ice.ConnectionRefusedException;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.Logger;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import java.util.List;

import cl.ucn.disc.pdis.sce.app.ZeroIce.Controller.IBackendMainPrx;
import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Porteria;
import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Vehiculo;
import lombok.extern.slf4j.Slf4j;

/**
 * The Main Controller.
 */
@Slf4j
public final class MainController implements IMainController {

    /**
     * Timeout time.
     */
    private static final int TIMEOUT_MILLIS = 10000;

    /**
     * The current status
     */
    private State state = State.Idle;

    /**
     * The {@link Communicator}.
     */
    private Communicator communicator;

    /**
     * The {@link IBackendMainPrx}
     */
    private IBackendMainPrx backenMain;

    /**
     * The Constructor.
     */
    public MainController() {
    }

    /**
     * @return the {@link Communicator}.
     */
    private static Communicator buildCommunicator() {

        // ZeroC properties
        Properties properties = Util.createProperties();

        // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
        properties.setProperty("Ice.Trace.Admin.Properties", "1");
        properties.setProperty("Ice.Trace.Locator", "2");
        properties.setProperty("Ice.Trace.Network", "3");
        properties.setProperty("Ice.Trace.Protocol", "1");
        properties.setProperty("Ice.Trace.Slicing", "1");
        // https://doc.zeroc.com/ice/3.7/client-side-features/automatic-retries
        properties.setProperty("Ice.Trace.Retry", "2");
        properties.setProperty("Ice.Trace.ThreadPool", "1");
        properties.setProperty("Ice.Compression.Level", "9");
        properties.setProperty("Ice.Package.ZeroIce", "cl.ucn.disc.pdis.sce.app");

        // The ZeroC framework!
        InitializationData initializationData = new InitializationData();
        initializationData.properties = properties;
        initializationData.logger = new Logger() {

            @Override
            public void print(String message) {
                log.debug(message);
            }

            @Override
            public void trace(String category, String message) {
                log.trace(category + " -> " + message);
            }

            @Override
            public void warning(String message) {
                log.warn(message);
            }

            @Override
            public void error(String message) {
                log.error(message);
            }

            @Override
            public String getPrefix() {
                return "ZeroIceLogger";
            }

            @Override
            public Logger cloneWithPrefix(String prefix) {
                return this;
            }
        };

        return Util.initialize(initializationData);

    }

    /**
     * @return The State.
     */
    @Override
    public State getState() {
        return this.state;
    }

    /**
     * Initialize the ZeroIce code.
     *
     * @param host to use.
     * @param port to use.
     */
    public synchronized void initialize(final String host, final int port) {

        // Connecting -> error
        if (this.state == State.Connecting) {
            throw new RuntimeException("Can't re-initialice while it's connecting!");
        }

        // Si ya estoy connectado, destruir!
        if (this.state == State.Ready) {
            this.destroy();
        }

        log.debug("Using Server {}:{}", host, port);

        this.state = State.Connecting;

        // The communicator
        this.communicator = buildCommunicator();

        // The Proxy
        // https://doc.zeroc.com/ice/3.7/client-side-features/proxies/proxy-and-endpoint-syntax
        final String proxyString = String.format("BackendMain:tcp -h %s -p %s -t %s -z", host, port, TIMEOUT_MILLIS);
        final ObjectPrx proxy = communicator.stringToProxy(proxyString);

        // Contact the server
        try {
            this.backenMain = IBackendMainPrx.checkedCast(proxy);
            this.state = State.Ready;
        } catch (final ConnectTimeoutException ex) {
            this.destroy();
            throw ex;
        } catch (final ConnectionRefusedException ex) {
            this.destroy();
            throw ex;
        }

    }

    /**
     * @return the {@link List} of {@link Vehiculo}.
     */
    @Override
    public List<Vehiculo> obtenerVehiculos() {
        return this.backenMain.obtenerVehiculos();
    }

    /**
     * @param placa    a registrar.
     * @param porteria por donde ingreso.
     */
    @Override
    public void registrarIngreso(String placa, Porteria porteria) {
        this.backenMain.registrarIngreso(placa, porteria);
    }

    /**
     * Destroy the Controller.
     */
    @Override
    public void destroy() {
        this.backenMain = null;
        if (this.communicator != null) {
            this.communicator.destroy();
            this.communicator = null;
        }
        this.state = State.Destroyed;
    }


}
