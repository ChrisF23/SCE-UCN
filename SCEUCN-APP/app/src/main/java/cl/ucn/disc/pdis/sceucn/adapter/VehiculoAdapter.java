package cl.ucn.disc.pdis.sceucn.adapter;

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
import cl.ucn.disc.pdis.sceucn.R;
import cl.ucn.disc.pdis.sceucn.model.Logo;
import cl.ucn.disc.pdis.sceucn.model.Vehiculo;

public final class VehiculoAdapter extends BaseAdapter {

    /**
     * La lista de vehiculos.
     */
    private List<Vehiculo> vehiculos;

    /**
     * El inflador.
     */
    private LayoutInflater inflater;

    /**
     * Constructor del adaptador
     * @param contexto La instancia del activity.
     * @param vehiculos La lista de personas a manejar.
     */
    public VehiculoAdapter(Context contexto, List<Vehiculo> vehiculos) {
        inflater = LayoutInflater.from(contexto);
        this.vehiculos = vehiculos;
    }

    /**
     * Constructor del adaptador
     * @param contexto La instancia del activity.
     */
    public VehiculoAdapter(Context contexto){
        inflater = LayoutInflater.from(contexto);
        this.vehiculos = new ArrayList<>();
    }

    /**
     * Carga una lista de vehiculos a este adaptador.
     * @param vehiculos La lista de vehiculos.
     */
    public void cargar(List<Vehiculo> vehiculos){
        this.vehiculos = vehiculos;
    }

    @Override
    public int getCount() {
        return vehiculos.size();
    }

    @Override
    public Vehiculo getItem(int position) {
        return vehiculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return vehiculos.get(position).hashCode();
    }

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
        holder.patente.setText(vehiculo.getPlaca());

        // 3.2.- Persona
        holder.nombrePersona.setText(String.format("%s %s",
                vehiculo.getPersona().getNombres(), vehiculo.getPersona().getApellidos()));

        // 3.3.- Logo.
        List<Logo> logos = vehiculo.getLogos();
        if (logos == null || logos.isEmpty()){
            holder.idLogo.setText("Sin Logo (WIP)");
        } else {
            Collections.sort(logos, (l1, l2) -> l1.getAnio().compareTo(l2.getAnio()));
            holder.idLogo.setText(logos.get(0).getIdentificador());
        }

        return view;
    }

    /**
     * View Holder de un vehiculo en este adaptador.
     */
    static class VehiculoViewHolder {
        @BindView(R.id.cv_tv_nombre_persona) TextView nombrePersona;
        @BindView(R.id.cv_tv_patente) TextView patente;
        @BindView(R.id.cv_tv_id_logo) TextView idLogo;

        VehiculoViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
