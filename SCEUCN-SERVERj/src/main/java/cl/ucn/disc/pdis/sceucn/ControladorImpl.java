package cl.ucn.disc.pdis.sceucn;

import java.util.ArrayList;
import java.util.List;

import com.zeroc.Ice.Current;

import cl.ucn.disc.pdis.sceucn.ice.model.Controlador;
import cl.ucn.disc.pdis.sceucn.ice.model.Registro;
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
     * 
     */
    @Override
    public void registrarIngreso(Registro registro, Current current) {
        log.debug("RegistrarIngreso: {}.", registro);
    }

    /**
     * 
     */
    @Override
    public List<Vehiculo> obtenerVehiculos(Current current) {
        log.debug("Obteniendo Vehiculos ..");
        return new ArrayList<>();
    }

}