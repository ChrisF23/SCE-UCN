package cl.ucn.disc.pdis.sceucn;

import java.util.ArrayList;
import java.util.List;

import com.zeroc.Ice.Current;

import cl.ucn.disc.pdis.sceucn.ice.model.Controlador;
import cl.ucn.disc.pdis.sceucn.ice.model.Porteria;
import cl.ucn.disc.pdis.sceucn.ice.model.Registro;
import cl.ucn.disc.pdis.sceucn.ice.model.Tipo;
import cl.ucn.disc.pdis.sceucn.ice.model.Vehiculo;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementacion de las operaciones del Controlador.
 * 
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
public final class ControladorImpl implements Controlador {

    /**
     * The List of vehiculos.
     */
    private final List<Vehiculo> vehiculos = new ArrayList<>();

    public ControladorImpl() {
        final Vehiculo vehiculo = new Vehiculo();
        vehiculo.anio = "2019";
        vehiculo.marca = "Suzuki";
        vehiculo.placa = "FBXS44";
        vehiculo.tipo = Tipo.Auto;
        vehiculos.add(vehiculo);
    }

    @Override
    public void registrarIngreso(String placa, Porteria porteria, Current current) {

    }

    @Override
    public void registrarIngresoOffline(String placa, Porteria porteria, String fecha, Current current) {

    }

    @Override
    public List<Vehiculo> obtenerVehiculos(Current current) {
        return this.vehiculos;
    }

}