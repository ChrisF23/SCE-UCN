package cl.ucn.disc.pdis.sceucn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import java.time.LocalDateTime;
import java.util.Date;

import cl.ucn.disc.pdis.sceucn.controller.ControladorVehiculos;
import cl.ucn.disc.pdis.sceucn.ice.model.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActivity extends AppCompatActivity {

    public static String SERVER_IP = "192.168.0.3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        log.debug("Estableciendo conexion...");
        Toast.makeText(this, "Estableciendo conexion...", Toast.LENGTH_LONG).show();


        AsyncTask.execute(() -> {

            // Properties
            final Properties properties = Util.createProperties();

            // https://doc.zeroc.com/ice/latest/property-reference/ice-trace

            /*
            properties.setProperty("Ice.Trace.Admin.Properties", "1");
            properties.setProperty("Ice.Trace.Locator", "2");
            properties.setProperty("Ice.Trace.Network", "3");
            properties.setProperty("Ice.Trace.Protocol", "1");
            properties.setProperty("Ice.Trace.Slicing", "1");
            properties.setProperty("Ice.Trace.ThreadPool", "1");
            properties.setProperty("Ice.Compression.Level", "9");
            */

            InitializationData initializationData = new InitializationData();
            initializationData.properties = properties;


            try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize())
            {
                // Create a proxy to the remote Printer object
                com.zeroc.Ice.ObjectPrx obj = communicator.stringToProxy("Controlador:tcp -h " + SERVER_IP + " -p 10000 -z");

                // Importante: Establecer tiempo de espera (10 segundos).
                obj = obj.ice_timeout(10000);

                // Downcast obj to Printer proxy
                ControladorPrx controlador = ControladorPrx.checkedCast(obj);

                if (controlador == null)
                {
                    throw new IllegalStateException("Invalid Proxy.");
                }

                // Patente del vehiculo que se desea registrar.
                String patenteIngreso = "DP-UA-13";

                // Requiere API 26, asi que usaremos Date.
                //LocalDateTime fecha = LocalDateTime.now();

                Date fecha = new Date();

                controlador.registrarIngreso(patenteIngreso, String.valueOf(fecha.getTime()));

                runOnUiThread(() -> {
                    //log.debug("Fecha date: "+ fecha.toString());
                    //log.debug("Fecha unix millisec: " + fecha.getTime());
                    Toast.makeText(this, "Ok: Se ha registrado el ingreso del vehiculo '" + patenteIngreso + "'.", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    log.debug("No se pudo establecer la conexion...");
                    Toast.makeText(this, "Error: No se pudo establecer la conexion...", Toast.LENGTH_LONG).show();
                });
            }

        });
    }
}
