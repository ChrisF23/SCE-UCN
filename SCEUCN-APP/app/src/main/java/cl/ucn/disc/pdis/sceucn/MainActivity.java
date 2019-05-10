package cl.ucn.disc.pdis.sceucn;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cl.ucn.disc.pdis.sceucn.ice.model.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActivity extends AppCompatActivity {

    public static String SERVER_IP = "192.168.0.3";

    @BindView(R.id.actv_patente)
    AutoCompleteTextView actvPatente;

    @BindView(R.id.b_limpiar_campo)
    Button bLimpiarCampo;

    @BindView(R.id.b_registrar_ingreso)
    Button bRegistrarIngreso;

    @BindView(R.id.ll_detalles)
    LinearLayout llDetalles;

    @BindViews({R.id.tv_patente, R.id.tv_marca, R.id.tv_tipo_vehiculo})
    List<TextView> tvDetallesVehiculo;

    private Object itemSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Hacer invisibles los detalles.
        llDetalles.setVisibility(View.INVISIBLE);

        // Al seleccionar una patente de la lista, mostrar los detalles asociados a esta.
        actvPatente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Mostrar los detalles de la patente seleccionada.
                mostrarDetalles(parent.getAdapter().getItem(position));

                // Dejar de enfocar esta vista.
                actvPatente.clearFocus();

                // Cerrar el teclado.
                try {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        // Si se deja vacio el campo (manualmente), ocultar los detalles.
        actvPatente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (actvPatente.getText().toString().isEmpty()){
                    ocultarDetalles();
                }
            }
        });

        // Agregar metodo registrarIngreso boton.
        bRegistrarIngreso.setOnClickListener((v) -> {registrarIngreso(itemSeleccionado); limpiarCampo();});

        // Limpiar campo.
        bLimpiarCampo.setOnClickListener((v) -> limpiarCampo());

        // Crear una lista con datos demo.
        final List<String> patentes = new ArrayList<>();
        patentes.add("CA-FA-23");
        patentes.add("FJ-CM-27");
        patentes.add("DP-UA-13");

        // Crear adaptador y asignarle la lista.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patentes);

        // Asignar adaptador al actvPatente.
        actvPatente.setAdapter(adapter);

    }

    private void limpiarCampo() {
        actvPatente.setText("");
    }

    private void mostrarDetalles(Object item) {
        // Si esta invisible, hacerlo visible.
        if (llDetalles.getVisibility() != View.VISIBLE){
            llDetalles.setVisibility(View.VISIBLE);
        }

        // Asignar item.
        itemSeleccionado = item;

        if (itemSeleccionado != null) {
            // Cargar datos del vehiculo.
            tvDetallesVehiculo.get(0).setText(String.format("Patente: %s", itemSeleccionado.toString()));
            tvDetallesVehiculo.get(1).setText(String.format("Marca: %s", itemSeleccionado.toString()));
            tvDetallesVehiculo.get(2).setText(String.format("Tipo: %s", itemSeleccionado.toString()));

            // TODO: Cargar datos de la persona + logo.
        }
    }

    private void ocultarDetalles(){
        if (llDetalles.getVisibility() != View.INVISIBLE){
            llDetalles.setVisibility(View.INVISIBLE);
        }

        itemSeleccionado = null;
    }

    private void registrarIngreso(final Object patente){

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
                // String patenteIngreso = "DP-UA-13";

                // Requiere API 26, asi que usaremos Date.
                //LocalDateTime fecha = LocalDateTime.now();

                Date fecha = new Date();

                controlador.registrarIngreso(patente.toString(), String.valueOf(fecha.getTime()));

                runOnUiThread(() -> {
                    //log.debug("Fecha date: "+ fecha.toString());
                    //log.debug("Fecha unix millisec: " + fecha.getTime());
                    Toast.makeText(this, "Ok: Se ha registrado el ingreso del vehiculo '" + patente + "'.", Toast.LENGTH_LONG).show();
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
