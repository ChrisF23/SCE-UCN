package cl.ucn.disc.pdis.sce.app.ZeroIce;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * The Ice Controller
 */
@Slf4j
public final class IceController {


    /**
     * @return the {@link Communicator}.
     */
    public static Communicator buildCommunicator() {

        // ZeroC properties
        Properties properties = Util.createProperties();
        // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
        properties.setProperty("Ice.Trace.Admin.Properties", "1");
        properties.setProperty("Ice.Trace.Locator", "2");
        properties.setProperty("Ice.Trace.Network", "3");
        properties.setProperty("Ice.Trace.Protocol", "1");
        properties.setProperty("Ice.Trace.Slicing", "1");
        properties.setProperty("Ice.Trace.ThreadPool", "1");
        properties.setProperty("Ice.Compression.Level", "9");
        properties.setProperty("Ice.Package.ZeroIce", "cl.ucn.disc.pdis.sce.app");

        // The ZeroC framework!
        InitializationData initializationData = new InitializationData();
        initializationData.properties = properties;

        final Communicator communicator = Util.initialize(initializationData);

        return communicator;

    }

}
