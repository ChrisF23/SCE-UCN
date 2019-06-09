package cl.ucn.disc.pdis.sceucn;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.pdis.sceucn.adapter.VehiculoAdapter;
import cl.ucn.disc.pdis.sceucn.controller.ControladorVehiculos;
import cl.ucn.disc.pdis.sceucn.controller.ModelConverter;
// import cl.ucn.disc.pdis.sceucn.ice.model.*;
import cl.ucn.disc.pdis.sceucn.model.Persona;
import cl.ucn.disc.pdis.sceucn.model.Porteria;
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

    @BindView(R.id.et_server_ip)
    EditText etServerIP;

    @BindView(R.id.b_set_server_ip)
    Button bSetServerIP;


    //------------------------------------------

    @BindView(R.id.lv_vehiculos)
    ListView lvVehiculos;

    VehiculoAdapter adapter;

    List<Vehiculo> listadoVehiculos = new ArrayList<>();
    //------------------------------------------

    ControladorVehiculos controladorVehiculos;
    ControladorVehiculos.EventListener listener = new ControladorVehiculos.EventListener() {

        @Override
        public void onListObtained(List<Vehiculo> listVehiculos) {
            // Notificar al adaptador.
            listadoVehiculos = listVehiculos;
            adapter.cargar(listadoVehiculos);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onError(String error) {
            // TODO: Mover siguiente linea a un nuevo metodo 'MostrarError()'.
            Toast.makeText(MainActivity.this, "Error: "+error, Toast.LENGTH_LONG).show();
            log.debug("Error: " + error);
        }

        @Override
        public void onProxyObtained() {
            Toast.makeText(MainActivity.this, "Ok: Se ha establecido la conexion.", Toast.LENGTH_LONG).show();
            log.debug("Ok: Se ha establecido la conexion");

            controladorVehiculos.obtenerListadoVehiculos();

            // Ejemplo, registrar el ingreso de un vehiculo que NO se encuentra en la base de datos.
            controladorVehiculos.registrarIngreso("ER-RR-01", Porteria.Norte);
            // Ejemplo, registrar el ingreso de un vehiculo que SI se encuentra en la base de datos.
            controladorVehiculos.registrarIngreso("CA-FA-23", Porteria.Norte);
        }
    };


    // ------- USING ICE APPLICATION.

    IceApplication iceApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // 0.- Obtener Ice Application.
        iceApplication = (IceApplication) getApplicationContext();

        //Typeface typeface = Typeface.createFromAsset(getAssets(), "fe_font.ttf");
        //---------------------------------------------------

        // 1.- Crear el adaptador.
        adapter = new VehiculoAdapter(this, listadoVehiculos);

        // 2.- Asignar el adaptador.
        lvVehiculos.setAdapter(adapter);

        // 3.- Actualizar lista.
        //Persona persona = new Persona("19691840K", "Christian Antonio", "Farias Aguila");

        // vehiculos.add(new Vehiculo(persona, "CA-FA-23"));
        // vehiculos.add(new Vehiculo(persona, "CA-ES-99"));
        // vehiculos.add(new Vehiculo(persona, "CA-89-23"));
        // vehiculos.add(new Vehiculo(persona, "DC-MC-U1"));
        // vehiculos.add(new Vehiculo(persona, "DP-UA-13"));
        // vehiculos.add(new Vehiculo(persona, "FJ-CM-27"));
        // vehiculos.add(new Vehiculo(persona, "UC-N1-10"));

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
                    adapter.cargar(listadoVehiculos);

                } else {
                    // Si contiene algo, buscar todas las personas que coincidan.
                    List<Vehiculo> tempVehiculos = new ArrayList<>();

                    for (Vehiculo v : listadoVehiculos) {

                        // Ambos en UPPERCASE.
                        if (v.getPlaca().toUpperCase().startsWith(query.toUpperCase())) {
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

        holder.tvPatente.setText(String.format("Patente: %s", v.getPlaca()));
        holder.tvNombrePersona.setText(String.format("Nombre: %s",
               String.format("%s %s", v.getPersona().getNombres(), v.getPersona().getApellidos())));
        holder.tvRut.setText(String.format("Rut: %s", v.getPersona().getRut()));


        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        // Boton registrar.
        dialogView.findViewById(R.id.b_registrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        // Boton cancelar
        dialogView.findViewById(R.id.b_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    /**
     * Actualiza la direccion IP del servidor.
     */
    private void setServerIP() {
        SERVER_IP = etServerIP.getText().toString();
        // Controlador de vehiculos:
        // controladorVehiculos = new ControladorVehiculos(listener);
        iceApplication.initializeIce(SERVER_IP);
        Toast.makeText(this, "Estableciendo conexion... ("+SERVER_IP+")", Toast.LENGTH_LONG).show();

        AsyncTask.execute(()->{
            try {

                List<cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo> _vehiculos = iceApplication.obtenerVehiculos();
                log.debug("Vehiculos size: {}", _vehiculos.size());

                listadoVehiculos.clear();

                for (cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo vehiculo : _vehiculos) {
                    listadoVehiculos.add(ModelConverter.convert(vehiculo));
                }

                runOnUiThread(()->{
                    adapter.cargar(listadoVehiculos);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Vehiculos obtenidos!", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e){
                log.debug(e.toString());
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
