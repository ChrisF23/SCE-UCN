package cl.ucn.disc.pdis.sce.app.activities;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zeroc.Ice.ConnectTimeoutException;
import com.zeroc.Ice.ConnectionRefusedException;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.pdis.sce.app.R;
import cl.ucn.disc.pdis.sce.app.ZeroIce.IMainController;
import cl.ucn.disc.pdis.sce.app.ZeroIce.IceController;
import cl.ucn.disc.pdis.sce.app.ZeroIce.MainController;
import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Porteria;
import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Vehiculo;
import cl.ucn.disc.pdis.sce.app.adapters.VehiculoAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class MainActivity extends AppCompatActivity {

    /**
     * The state of the controller.
     */
    enum State {
        Standby,
        Initializing,
        Initialized,
        Destroyed
    }

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

    /**
     * The MainController
     */
    private IMainController mainController;

    /**
     * Initialized?
     */
    private State state = State.Standby;

    /**
     * El adaptador de vehiculos.
     */
    private VehiculoAdapter vehiculoAdapter;

    /**
     * La direccion IP del servidor.
     */
    private String server = "192.168.0.16";

    /**
     * El puerto del servidor.
     */
    private int port = 10000;

    /**
     * La porteria en la que se encuentra el usuario actualmente, y en donde se registraran los ingresos.
     */
    private Porteria porteriaActual = Porteria.Principal;

    /**
     * On Create: Setup.
     *
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        // .. con mantequilla?
        ButterKnife.bind(this);

        this.state = State.Standby;

        // 0.- Asignar el toolbar.
        this.setSupportActionBar(myToolbar);

        // 1.- Verificar modo noche
        // setTheme(R.style.Theme_Primary_Base_Dark);

        // 2.- Crear el adaptador. Referencia a este contexto.
        this.vehiculoAdapter = new VehiculoAdapter(this);

        // 3.- Asignar el adaptador al listview de vehiculos.
        this.lvVehiculos.setAdapter(vehiculoAdapter);

        // 4.- Configurar el edit text de ingreso de placa.
        this.setupEditTextPlaca();

        // 5.- Configurar el list view de vehiculos.
        this.setupListViewVehiculos();

        // 6.- Create the controller in background
        AsyncTask.execute(() -> {
            this.setServer(this.server);
            this.obtenerVehiculos();
        });

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
                /*
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

                 */
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

        /*
        holder.tvPatente.setText(vehiculo.getPlaca());
        holder.tvNombrePersona.setText(String.format("%s %s", vehiculo.getPersona().getNombres(),
                vehiculo.getPersona().getApellidos()));
        holder.tvRut.setText(vehiculo.getPersona().getRut());


         */
    }

    /**
     * Actualiza la direccion IP del servidor.
     *
     * @param server to use.
     */
    private void setServer(final String server) {

        // FIXME: Agregar mensaje en caso de que server sea null o vacio
        if (StringUtils.isEmpty(server)) {
            return;
        }

        if (this.state == State.Initializing) {
            // FIXME: Indicar mediante un mensaje que se esta inicializando
            return;
        }

        // Destruir el controlador
        if (this.mainController != null) {
            this.mainController.destroy();
            this.mainController = null;
            this.state = State.Destroyed;
        }

        // Setter the server
        this.server = server;

        try {

            this.state = State.Initializing;

            // Build the controller in background
            this.mainController = new MainController(
                    IceController.buildCommunicator(),
                    this.server,
                    this.port
            );

            this.state = State.Initialized;

        } catch (final ConnectTimeoutException ex) {
            this.state = State.Standby;
        } catch (final ConnectionRefusedException ex) {
            this.state = State.Standby;
        }

        // Mostrar mensaje.
        // Toast.makeText(this, "Estableciendo conexion... (" + this.server + ")", Toast.LENGTH_LONG).show();

    }

    /**
     * Obtiene todos los vehiculos desde el server.
     */
    private void obtenerVehiculos() {

        if (this.mainController != null) {

            final List<Vehiculo> vehiculos = this.mainController.obtenerVehiculos();
            this.vehiculoAdapter.setVehiculos(vehiculos);

            runOnUiThread(() -> {
                this.vehiculoAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Vehiculos obtenidos!", Toast.LENGTH_LONG).show();
            });

        }

        /*
        AsyncTask.execute(() -> {
            try {

                List<Vehiculo> _vehiculos = iceApplication.obtenerVehiculos();
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

         */
    }

    private void registrarIngreso(Vehiculo vehiculo) {
        /*
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

         */
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_tema_oscuro:
                Toast.makeText(this, "Aplicando Modo Noche...", Toast.LENGTH_SHORT).show();
                setTheme(R.style.Theme_Primary_Base_Dark);
                return true;

            case R.id.item_verificar_conexion:
                Toast.makeText(this, "Reconectando a " + this.server + " ..", Toast.LENGTH_SHORT).show();
                AsyncTask.execute(() -> {
                    this.setServer(this.server);
                    this.obtenerVehiculos();
                });
                return true;

//            case R.id.item_tema_desert:
//                Toast.makeText(this, "Aplicando Tema Desert...", Toast.LENGTH_SHORT).show();
//                setTheme(R.style.Theme_Primary_Base_lightTheme);

            case R.id.item_configurar_servidor:
                abrirDialogConfigurarServidor();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     *
     */
    private void abrirDialogConfigurarServidor() {

        // FIXME: Enviar mensaje que el proceso de inicializacion se encuentra en marcha.
        if (this.state == State.Initializing) {
            return;
        }

        // 0.- Obtener el viewgroup del activity actual.
        final ViewGroup viewGroup = findViewById(android.R.id.content);

        // 1.- Inflar el dialog de configuracion del servidor
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_server_config, viewGroup, false);

        // 2.- Crear un Builder de AlertDialogs.
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 3.- Asignar dialog a la vista del builder.
        builder.setView(dialogView);

        // 4.- Configurar dialog.
        final EditText etServer = dialogView.findViewById(R.id.et_server_ip);
        etServer.setText(this.server);

        // 5.- Crear el alert dialog a traves del builder..
        final AlertDialog alertDialog = builder.create();

        // 6.- Configurar el boton de registrar ingreso.
        dialogView.findViewById(R.id.b_conectar).setOnClickListener(view -> {

            // Cancelar dialog.
            alertDialog.dismiss();

            // Obtener edit text.
            final EditText etServerIP = dialogView.findViewById(R.id.et_server_ip);

            // Running in background
            AsyncTask.execute(() -> {
                this.setServer(etServerIP.getText().toString());
                this.obtenerVehiculos();
            });


        });

        // 7.- Configurar el boton de cancelar.
        dialogView.findViewById(R.id.b_config_cancelar).setOnClickListener(view -> alertDialog.dismiss());

        // 8.- Mostrar el alert dialog.
        alertDialog.show();
    }

    /**
     * The ViewHolder.
     */
    static class VehiculoDetalleViewHolder {

        @BindView(R.id.tv_patente)
        TextView tvPatente;

        @BindView(R.id.tv_nombre_persona)
        TextView tvNombrePersona;

        @BindView(R.id.tv_rut)
        TextView tvRut;

        VehiculoDetalleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
