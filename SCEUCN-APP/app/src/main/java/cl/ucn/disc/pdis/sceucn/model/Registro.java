package cl.ucn.disc.pdis.sceucn.model;

import lombok.Data;
import lombok.NonNull;

/**
 * El registro de ingreso.
 */
@Data
public class Registro {

    /**
     * El vehiculo al cual se refiere este registro.
     */
    @NonNull
    private Vehiculo vehiculo;

    /**
     * La fecha de ingreso del vehiculo.
     * Utiliza la norma ISO 8601. Ej: '2007-04-05T14:30Z'.
     */
    @NonNull
    private String fecha;

    /**
     * Por que porteria hizo ingreso el vehiculo.
     */
    @NonNull
    private Porteria porteria;
}
