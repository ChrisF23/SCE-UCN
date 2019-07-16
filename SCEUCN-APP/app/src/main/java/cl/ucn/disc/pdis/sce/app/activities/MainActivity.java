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
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.pdis.sce.app.R;
import cl.ucn.disc.pdis.sce.app.ZeroIce.IMainController;
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
     * El adaptador de vehiculos.
     */
    private VehiculoAdapter vehiculoAdapter;

    /**
     * La direccion IP del servidor.
     */
    //private String host = "172.16.34.126";
    private String host = "192.168.0.14";

    List<Vehiculo> vehiculos = new ArrayList<>();

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

        // 6.- Creacion del main controller.
        this.mainController = new MainController();

        this.initializeAndLoad();

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
                if (vehiculos == null) return;

                if (vehiculoAdapter == null) return;

                String query = String.valueOf(s);

                // Si la busqueda esta vacia, entonces mostrar todos los vehiculos.
                if (query.isEmpty()) {
                    vehiculoAdapter.setVehiculos(vehiculos);

                } else {
                    // Si contiene algo, buscar todas las personas que coincidan.
                    List<Vehiculo> tempVehiculos = new ArrayList<>();

                    //s.toString().replace(" ","-");

                    for (Vehiculo v : vehiculos) {

                        // Ambos en UPPERCASE.
                        if (v.placa.toUpperCase().startsWith(query.toUpperCase())) {
                            tempVehiculos.add(v);
                        }
                    }
                    vehiculoAdapter.setVehiculos(tempVehiculos);
                }

                // Notificar al adaptador que los datos han cambiado.
                vehiculoAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO: Borrar despues de insertar guion

                int largoEt = etPlaca.getText().length();

                if(largoEt==2||largoEt==5)
                    etPlaca.setText(etPlaca.getText().insert(largoEt, "-"));
                    etPlaca.setSelection(etPlaca.getText().length());
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


        holder.tvPatente.setText(String.format(vehiculo.placa));
        holder.tvNombrePersona.setText(String.format("Nombre: %s",
                String.format("%s %s", vehiculo.persona.nombres, vehiculo.persona.apellidos)));
        holder.tvRut.setText(String.format("Rut: %s", vehiculo.persona.rut));
        holder.tvMarca.setText(vehiculo.marca);
        holder.tvTipo.setText(String.format(String.valueOf(vehiculo.tipo)));
        //holder.tvLogo.setText(vehiculo.);


        /*
        holder.tvPatente.setText(vehiculo.getPlaca());
        holder.tvNombrePersona.setText(String.format("%s %s", vehiculo.getPersona().getNombres(),
                vehiculo.getPersona().getApellidos()));
        holder.tvRut.setText(vehiculo.getPersona().getRut());


         */
    }

    /**
     * Actualiza la direccion IP del servidor.
     */
    private void initializeAndLoad() {

        if (this.mainController.getState() == IMainController.State.Connecting) {
            Toast.makeText(this, "Already trying to connect !!", Toast.LENGTH_LONG).show();
            log.warn("Already trying to connect!!");
            return;
        }

        Toast.makeText(this, "Connecting to " + this.host + " ..", Toast.LENGTH_SHORT).show();

        AsyncTask.execute(() -> {
            try {

                // Destroy!
                this.mainController.destroy();

                this.mainController.initialize(this.host, this.port);
                this.obtenerVehiculos();
            } catch (final ConnectTimeoutException ex) {
                runOnUiThread(() -> Toast.makeText(this, "ConnectTimeoutException! to host " + this.host, Toast.LENGTH_LONG).show());
                log.warn("Error", ex);
            } catch (final ConnectionRefusedException ex) {
                runOnUiThread(() -> Toast.makeText(this, "ConnectionRefusedException! to host " + this.host, Toast.LENGTH_LONG).show());
                log.warn("Error", ex);
            }
        });


        // Mostrar mensaje.
        // Toast.makeText(this, "Estableciendo conexion... (" + this.server + ")", Toast.LENGTH_LONG).show();

    }

    /**
     * @return if the controller is ready.
     */
    private boolean isControllerReady() {

        // Main controller don't exists.
        if (this.mainController == null) {
            log.warn("MainController is null !!!");
            return false;
        }

        // Not ready?
        if (this.mainController.getState() != IMainController.State.Ready) {
            log.warn("Can't get Vehiculos, state: {}", this.mainController.getState());
            return false;
        }

        return true;

    }

    /**
     * Obtiene todos los vehiculos desde el server.
     * Warn: DEBE ejecutarse en segundo plano.
     */
    private void obtenerVehiculos() {

        if (!this.isControllerReady()) {
            return;
        }

        final StopWatch stopWatch = StopWatch.createStarted();

        try {
            this.vehiculos = this.mainController.obtenerVehiculos();
            this.vehiculoAdapter.setVehiculos(vehiculos);

        } catch (ConnectTimeoutException ex) {
            runOnUiThread(() -> Toast.makeText(this, "Error de Timeout!", Toast.LENGTH_LONG).show());
            return;
        } catch (final ConnectionRefusedException ex) {
            runOnUiThread(() -> Toast.makeText(this, "Error de conexion!", Toast.LENGTH_LONG).show());
            return;
        }

        runOnUiThread(() -> {
            this.vehiculoAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Vehiculos obtenidos in " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms.", Toast.LENGTH_LONG).show();
        });


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
        AsyncTask.execute(() -> {
            try {
                this.mainController.registrarIngreso(vehiculo.placa,porteriaActual);

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this,
                            String.format("Se ha registrado el ingreso del vehiculo [%s]", vehiculo.placa),
                            Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * @param menu menu.
     * @return true if use menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * @param item menu.
     * @return true si fue aceptada.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // The Theme
            case R.id.item_tema_oscuro: {
                Toast.makeText(this, "Aplicando Modo Noche...", Toast.LENGTH_SHORT).show();
                setTheme(R.style.Theme_Primary_Base_Dark);
                return true;
            }

            // Verificando connection
            case R.id.item_verificar_conexion: {

                switch (this.mainController.getState()) {
                    case Ready: {
                        Toast.makeText(this, "Getting Vehiculos from host " + this.host + " ..", Toast.LENGTH_SHORT).show();
                        AsyncTask.execute(this::obtenerVehiculos);
                        return true;
                    }
                    case Connecting: {
                        Toast.makeText(this, "Already trying to connect !!", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    case Idle:
                    case Destroyed: {
                        initializeAndLoad();
                        return true;
                    }

                }
                return true;
            }

//            case R.id.item_tema_desert:
//                Toast.makeText(this, "Aplicando Tema Desert...", Toast.LENGTH_SHORT).show();
//                setTheme(R.style.Theme_Primary_Base_lightTheme);

            // Configurar servidor
            case R.id.item_configurar_servidor: {
                abrirDialogConfigurarServidor();
                return true;
            }

            // The default
            default: {
                return super.onOptionsItemSelected(item);
            }

        }

    }

    /**
     *
     */
    private void abrirDialogConfigurarServidor() {

        // FIXME: Enviar mensaje que el proceso de inicializacion se encuentra en marcha.
        if (this.mainController == null || this.mainController.getState() == IMainController.State.Connecting) {
            log.warn("Connection, please try later!");
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
        final EditText etHost = dialogView.findViewById(R.id.et_server_ip);
        etHost.setText(this.host);

        // 5.- Crear el alert dialog a traves del builder..
        final AlertDialog alertDialog = builder.create();

        // 6.- Configurar el boton de registrar ingreso.
        dialogView.findViewById(R.id.b_conectar).setOnClickListener(view -> {

            // Cancelar dialog.
            alertDialog.dismiss();

            // Change the host
            this.host = StringUtils.trimToEmpty(etHost.getText().toString());

            this.initializeAndLoad();

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

        @BindView(R.id.tv_marca)
        TextView tvMarca;

        @BindView(R.id.tv_tipo_vehiculo)
        TextView tvTipo;

        @BindView(R.id.tv_id_logo)
        TextView tvLogo;


        VehiculoDetalleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
