package cl.ucn.disc.pdis.sce.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.pdis.sce.app.R;
import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Logo;
import cl.ucn.disc.pdis.sce.app.ZeroIce.Model.Vehiculo;

/**
 * The Adapter of {@link Vehiculo}.
 */
public final class VehiculoAdapter extends BaseAdapter {

    /**
     * La lista de vehiculos.
     */
    private List<Vehiculo> vehiculos = new ArrayList<>();

    /**
     * El inflador.
     */
    private LayoutInflater inflater;

    /**
     * Constructor del adaptador
     *
     * @param contexto La instancia del activity.
     */
    public VehiculoAdapter(final Context contexto) {
        this.inflater = LayoutInflater.from(contexto);
    }

    /**
     * @param vehiculos a cargar.
     */
    public void setVehiculos(final List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    /**
     * @return the size of the vehicles.
     */
    @Override
    public int getCount() {
        return this.vehiculos.size();
    }

    /**
     * @param position to get.
     * @return the {@link Vehiculo}
     */
    @Override
    public Vehiculo getItem(int position) {
        return vehiculos.get(position);
    }

    /**
     * @param position
     * @return the position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position
     * @param view
     * @param parent
     * @return the View.
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // 0.- Obtener el vehiculo.
        final Vehiculo vehiculo = getItem(position);

        // 1.- Crear holder.
        VehiculoViewHolder holder;

        // 2.- Inflar la vista solo si es nula, si no, reutilizarla.
        if (view != null) {
            holder = (VehiculoViewHolder) view.getTag();
        } else {
            // TODO: Inflar una vista personalizada (ej: fila_vehiculo).
            view = inflater.inflate(R.layout.layout_card, parent, false);
            holder = new VehiculoViewHolder(view);
            view.setTag(holder);
        }

        // 3.- Llenar holder con los datos del vehiculo.

        // 3.1.- Vehiculo
        holder.patente.setText(vehiculo.placa);

        // 3.2.- Persona
        holder.nombrePersona.setText(String.format("%s %s",
                vehiculo.persona.nombres, vehiculo.persona.apellidos));

        // 3.3.- Logo.
        List<Logo> logos = vehiculo.logos;
        if (logos == null || logos.isEmpty()) {
            holder.idLogo.setText("Sin Logo (WIP)");
        } else {
            // FIXME: Cada vez que se despliegue la lista los logos se van a ordenar?
            Collections.sort(logos, (l1, l2) -> l1.anio.compareTo(l2.anio));
            holder.idLogo.setText(logos.get(0).identificador);
        }
        return view;
    }

    /**
     * View Holder de un vehiculo en este adaptador.
     */
    static class VehiculoViewHolder {

        @BindView(R.id.cv_tv_nombre_persona)
        TextView nombrePersona;

        @BindView(R.id.cv_tv_patente)
        TextView patente;

        @BindView(R.id.cv_tv_id_logo)
        TextView idLogo;

        VehiculoViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
