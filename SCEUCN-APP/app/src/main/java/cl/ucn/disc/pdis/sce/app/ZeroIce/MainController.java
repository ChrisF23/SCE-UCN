package cl.ucn.disc.pdis.sce.app.ZeroIce;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;

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
     * The {@link Communicator}.
     */
    private Communicator communicator;

    /**
     * The {@link IBackendMainPrx}
     */
    private IBackendMainPrx backenMain;

    /**
     * @param communicator to use.
     * @param server       to use.
     * @param port         to use.
     */
    public MainController(final Communicator communicator, final String server, final int port) {

        this.communicator = communicator;

        log.debug("Using Server {}:{}", server, port);

        // The Proxy
        final ObjectPrx proxy = communicator.stringToProxy(String.format("BackendMain:default -h %s -p %s -z", server, port));

        this.backenMain = IBackendMainPrx.checkedCast(proxy);

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
        this.communicator.destroy();
        this.communicator = null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        log.debug("The Main!");

        final IMainController mainController = new MainController(IceController.buildCommunicator(), "localhost", 10000);
        for (Vehiculo vehiculo : mainController.obtenerVehiculos()) {
            log.debug("Vehiculo: {}", vehiculo);
        }

        log.debug("End.");

    }

}
