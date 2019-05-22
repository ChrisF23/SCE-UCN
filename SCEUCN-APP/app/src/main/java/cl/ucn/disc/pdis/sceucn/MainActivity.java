package cl.ucn.disc.pdis.sceucn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.pdis.sceucn.adapter.VehiculoAdapter;
import cl.ucn.disc.pdis.sceucn.controller.ModelConverter;
import cl.ucn.disc.pdis.sceucn.ice.model.*;
import cl.ucn.disc.pdis.sceucn.model.Persona;
import cl.ucn.disc.pdis.sceucn.model.Registro;
import cl.ucn.disc.pdis.sceucn.model.Vehiculo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActivity extends AppCompatActivity {

    // Attribs:

    /**
     * La direccion IP del servidor.
     */
    public static String SERVER_IP = "192.168.0.3";

    /**
     * El item seleccionado.
     */
    private Object itemSeleccionado;

    // Vistas:

    @BindView(R.id.et_patente)
    EditText etPatente;

    /*
    @BindView(R.id.b_limpiar_campo)
    Button bLimpiarCampo;

    @BindView(R.id.b_registrar_ingreso)
    Button bRegistrarIngreso;

    @BindView(R.id.ll_detalles)
    LinearLayout llDetalles;

    @BindViews({R.id.tv_patente, R.id.tv_marca, R.id.tv_tipo_vehiculo})
    List<TextView> tvDetallesVehiculo;
    */

    // DEV ONLY:

    @BindView(R.id.et_server_ip)
    EditText etServerIP;

    @BindView(R.id.b_set_server_ip)
    Button bSetServerIP;


    //------------------------------------------

    @BindView(R.id.lv_vehiculos)
    ListView lvVehiculos;

    VehiculoAdapter adapter;

    static List<Vehiculo> vehiculos = new ArrayList<>();

    //------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //---------------------------------------------------

        // 1.- Crear el adaptador.
        adapter = new VehiculoAdapter(this, vehiculos);

        // 2.- Asignar el adaptador.
        lvVehiculos.setAdapter(adapter);

        // 3.- Actualizar lista.
        Persona persona = new Persona("19691840K", "Christian Farias");

        vehiculos.add(new Vehiculo(persona, "CA-FA-23"));
        vehiculos.add(new Vehiculo(persona, "CA-ES-99"));
        vehiculos.add(new Vehiculo(persona, "CA-89-23"));
        vehiculos.add(new Vehiculo(persona, "DC-MC-U1"));
        vehiculos.add(new Vehiculo(persona, "FJ-CM-27"));
        vehiculos.add(new Vehiculo(persona, "UC-N1-10"));

        // 4.- Filtrado en el ingreso de patente.
        etPatente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter == null) return;

                String query = String.valueOf(s);

                // Si la busqueda esta vacia, entonces mostrar todos los vehiculos.
                if (query.isEmpty()){
                    adapter.cargar(vehiculos);

                } else {
                    // Si contiene algo, buscar todas las personas que coincidan.

                    List<Vehiculo> tempVehiculos = new ArrayList<>();

                    for (Vehiculo v : vehiculos) {

                        // Ambos en UPPERCASE.
                        if (v.getPatente().toUpperCase().startsWith(query.toUpperCase())) {
                            tempVehiculos.add(v);
                        }
                    }
                    adapter.cargar(tempVehiculos);
                }

                // Notificar al adaptador que los datos han cambiado.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        // 5.- Al seleccionar un vehiculo, abrir dialog.
        lvVehiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Vehiculo v = (Vehiculo) lvVehiculos.getAdapter().getItem(position);

                abrirDialogo(v);
            }
        });
        
        //---------------------------------------------------

        // Agregar metodo setServerIP al boton.
        bSetServerIP.setOnClickListener((v) -> setServerIP());
    }

    private void abrirDialogo(Vehiculo v) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_vehiculo, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        // Configurar dialogo:
        LinearLayout llDialog = dialogView.findViewById(R.id.ll_dialog);
        VehiculoDetalleViewHolder holder = new VehiculoDetalleViewHolder(llDialog);

        holder.tvPatente.setText(String.format("Patente: %s", v.getPatente()));
        holder.tvNombrePersona.setText(String.format("Nombre: %s", v.getPersona().getNombre()));
        holder.tvRut.setText(String.format("Rut: %s", v.getPersona().getRut()));

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, "Cargando listado de vehiculos...", Toast.LENGTH_LONG).show();
        //ControladorVehiculos.obtenerListadoVehiculos();
    }

    /**
     * Actualiza la direccion IP del servidor.
     */
    private void setServerIP() {
        SERVER_IP = etServerIP.getText().toString();
        Toast.makeText(this, "Server IP actualizada.", Toast.LENGTH_SHORT).show();
    }

    private void registrarIngreso(final Object patente){

        // DEV ONLY.

        //log.debug("Estableciendo conexion...");
        //Toast.makeText(this, "Estableciendo conexion... ("+SERVER_IP+")", Toast.LENGTH_LONG).show();

        // TODO: Mover esto a ControladorVehiculos.

        AsyncTask.execute(() -> {
            // Properties
            final Properties properties = Util.createProperties();

            // https://doc.zeroc.com/ice/latest/property-reference/ice-trace


            // properties.setProperty("Ice.Trace.Admin.Properties", "1");
            // properties.setProperty("Ice.Trace.Locator", "2");
            // properties.setProperty("Ice.Trace.Network", "3");
            // properties.setProperty("Ice.Trace.Protocol", "1");
            // properties.setProperty("Ice.Trace.Slicing", "1");
            // properties.setProperty("Ice.Trace.ThreadPool", "1");
            // properties.setProperty("Ice.Compression.Level", "9");


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

                // TODO: Los registros creados deben ser almacenados localmente, para posteriormente ser guardados en el servidor.
                Registro registro = new Registro(patente.toString(), new Date());

                controlador.guardarRegistro(ModelConverter.convert(registro));

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

    static class VehiculoDetalleViewHolder {

        @BindView(R.id.tv_patente)
        TextView tvPatente;

        @BindView(R.id.tv_nombre_persona)
        TextView tvNombrePersona;

        @BindView(R.id.tv_rut)
        TextView tvRut;

        public VehiculoDetalleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
