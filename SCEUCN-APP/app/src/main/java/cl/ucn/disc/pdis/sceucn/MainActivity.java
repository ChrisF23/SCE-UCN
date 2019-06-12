package cl.ucn.disc.pdis.sceucn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.pdis.sceucn.adapter.VehiculoAdapter;
import cl.ucn.disc.pdis.sceucn.controller.ModelConverter;
import cl.ucn.disc.pdis.sceucn.model.Persona;
import cl.ucn.disc.pdis.sceucn.model.Porteria;
import cl.ucn.disc.pdis.sceucn.model.Vehiculo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActivity extends AppCompatActivity {

    // Vistas:

    /**
     * El edit text donde se buscan las placas.
     */
    @BindView(R.id.et_placa)
    EditText etPlaca;

    /**
     * El list view que muestra los vehiculos.
     */
    @BindView(R.id.lv_vehiculos)
    ListView lvVehiculos;

    /**
     * El toolbar de la aplicacion.
     */
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;

    // Attribs:

    /**
     * La aplicacion de que se comunica con el framework ICE.
     */
    IceApplication iceApplication;

    /**
     * El adaptador de vehiculos.
     */
    VehiculoAdapter adapter;

    /**
     * La direccion IP del servidor.
     */
    public static String SERVER_IP = "192.168.0.3";

    /**
     * El listado de vehiculos.
     */
    List<Vehiculo> listadoVehiculos = new ArrayList<>();

    /**
     * La porteria en la que se encuentra el usuario actualmente, y en donde
     * se registraran los ingresos.
     */
    Porteria porteriaActual = Porteria.Principal;

    /**
     * On Create; Setup.
     *
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 0.- Asignar el toolbar.
        setSupportActionBar(myToolbar);

        // 1.- Obtener Ice Application.
        iceApplication = (IceApplication) getApplicationContext();

        // 2.- Crear el adaptador. Referencia a este contexto.
        adapter = new VehiculoAdapter(this);

        // 3.- Asignar el adaptador al listview de vehiculos.
        lvVehiculos.setAdapter(adapter);

        // 4.- Configurar el edit text de ingreso de placa.
        setupEditTextPlaca();

        // 5.- Configurar el list view de vehiculos.
        setupListViewVehiculos();
    }

    /**
     * Configura el list view de vehiculos.
     */
    private void setupListViewVehiculos() {

        // 0.- Al seleccionar un vehiculo, abrir dialog.

        lvVehiculos.setOnItemClickListener((parent, view, position, id) -> {
            Vehiculo v = (Vehiculo) lvVehiculos.getAdapter().getItem(position);
            abrirDialogoDetalleVehiculo(v);
        });
    }

    /**
     * Configura el edit text de ingreso de placa.
     */
    private void setupEditTextPlaca() {

        // 0.- Filtrado en el ingreso de patente.
        // TODO: 1.- Formateo de la placa mientras es escrita (Ej: CAFA23 -> CA-FA-23).

        etPlaca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter == null) return;

                String query = String.valueOf(s);

                // Si la busqueda esta vacia, entonces mostrar todos los vehiculos.
                if (query.isEmpty()) {
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
                //TODO: Punto 1 de este metodo se realiza aqui.
            }
        });
    }

    /**
     * Abre el dialog para mostrar el detalle del vehiculo seleccionado.
     *
     * @param vehiculo El vehiculo seleccionado.
     */
    private void abrirDialogoDetalleVehiculo(Vehiculo vehiculo) {
        // 0.- Obtener el viewgroup del activity actual.
        ViewGroup viewGroup = findViewById(android.R.id.content);

        // 1.- Inflar el dialog de detalle del vehiculo.
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_vehiculo, viewGroup, false);

        // 2.- Crear un Builder de AlertDialogs.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 3.- Asignar dialog a la vista del builder.
        builder.setView(dialogView);

        // 4.- Configurar dialog.
        setupDialogView(dialogView, vehiculo);

        // 5.- Crear el alert dialog a traves del builder..
        AlertDialog alertDialog = builder.create();

        // 6.- Configurar el boton de registrar ingreso.
        dialogView.findViewById(R.id.b_registrar).setOnClickListener(view -> {

            // Registrar el ingreso.
            registrarIngreso(vehiculo);

            // Cancelar dialog.
            alertDialog.dismiss();
        });

        // 7.- Configurar el boton de cancelar.
        dialogView.findViewById(R.id.b_cancelar).setOnClickListener(view -> alertDialog.dismiss());

        // 8.- Mostrar el alert dialog.
        alertDialog.show();
    }

    /**
     * Configura el dialog para mostrar el detalle de un vehiculo.
     */
    private void setupDialogView(View dialogView, Vehiculo vehiculo) {
        LinearLayout llDialog = dialogView.findViewById(R.id.ll_dialog);
        VehiculoDetalleViewHolder holder = new VehiculoDetalleViewHolder(llDialog);

        /*
        holder.tvPatente.setText(String.format("Patente: %s", vehiculo.getPlaca()));
        holder.tvNombrePersona.setText(String.format("Nombre: %s",
                String.format("%s %s", vehiculo.getPersona().getNombres(), vehiculo.getPersona().getApellidos())));
        holder.tvRut.setText(String.format("Rut: %s", vehiculo.getPersona().getRut()));
        */

        holder.tvPatente.setText(vehiculo.getPlaca());
        holder.tvNombrePersona.setText(String.format("%s %s", vehiculo.getPersona().getNombres(),
                vehiculo.getPersona().getApellidos()));
        holder.tvRut.setText(vehiculo.getPersona().getRut());
    }

    /**
     * Actualiza la direccion IP del servidor.
     */
    private void setServerIP(String serverIP) {
        // Establecer la direccion ip del servidor.
        SERVER_IP = serverIP;

        // Mostrar mensaje.
        Toast.makeText(this, "Estableciendo conexion... (" + SERVER_IP + ")", Toast.LENGTH_LONG).show();

        // Iniciar aplicacion de ice.
        iceApplication.initializeIce(SERVER_IP);
    }

    private void obtenerVehiculos(){

        AsyncTask.execute(() -> {
            try {

                List<cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo> _vehiculos = iceApplication.obtenerVehiculos();
                log.debug("Vehiculos size: {}", _vehiculos.size());

                listadoVehiculos.clear();

                for (cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo vehiculo : _vehiculos) {
                    listadoVehiculos.add(ModelConverter.convert(vehiculo));
                }

                runOnUiThread(() -> {
                    adapter.cargar(listadoVehiculos);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Vehiculos obtenidos!", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void registrarIngreso(Vehiculo vehiculo){
        AsyncTask.execute(() -> {
            try {
                iceApplication.registrarIngreso(vehiculo.getPlaca(), ModelConverter.convertPorteria(porteriaActual));

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this,
                            String.format("Se ha registrado el ingreso del vehiculo [%s]", vehiculo.getPlaca()),
                            Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_tema_stiletto:
                Toast.makeText(this, "Aplicando Tema Stiletto...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_tema_desert:
                Toast.makeText(this, "Aplicando Tema Desert...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_tema_shipcove:
                Toast.makeText(this, "Aplicando Tema Ship Cove...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_tema_oscuro:
                Toast.makeText(this, "Aplicando Tema Oscuro...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_configurar_servidor:
                abrirDialogConfigurarServidor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void abrirDialogConfigurarServidor() {
        // 0.- Obtener el viewgroup del activity actual.
        ViewGroup viewGroup = findViewById(android.R.id.content);

        // 1.- Inflar el dialog de configuracion del servidor
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_server_config, viewGroup, false);

        // 2.- Crear un Builder de AlertDialogs.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 3.- Asignar dialog a la vista del builder.
        builder.setView(dialogView);

        // 4.- Configurar dialog.

        // 5.- Crear el alert dialog a traves del builder..
        AlertDialog alertDialog = builder.create();

        // 6.- Configurar el boton de registrar ingreso.
        dialogView.findViewById(R.id.b_conectar).setOnClickListener(view -> {

            // Obtener edit text.
            EditText etServerIP = dialogView.findViewById(R.id.et_server_ip);

            // Establecer ip.
            setServerIP(etServerIP.getText().toString());

            // Obtener listado de vehiculos.
            obtenerVehiculos();

            // Cancelar dialog.
            alertDialog.dismiss();
        });

        // 7.- Configurar el boton de cancelar.
        dialogView.findViewById(R.id.b_cancelar).setOnClickListener(view -> alertDialog.dismiss());

        // 8.- Mostrar el alert dialog.
        alertDialog.show();
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
