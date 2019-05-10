package cl.ucn.disc.pdis.sceucn.model;

import java.util.Date;

import lombok.Data;
import lombok.NonNull;

@Data
public class Registro {

    @NonNull
    private String patente;

    @NonNull
    private Date fecha;

}
